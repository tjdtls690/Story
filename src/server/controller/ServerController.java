package server.controller;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import ClientController.SignUpDialogController;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;
import protocol.Protocol;
import server.Room.Room;
import server.serverService.MemberService;
import server.serverService.MemberServiceCon;

public class ServerController implements Initializable{
	
	// JavzFX 필드
	
		// 일반 필드
	
		public static Parent parent;
		public static Stage primaryStage;
		
		// 로그인 창
		
		@FXML private TextField managerId;
		@FXML private TextField managerPw;
		@FXML private Button btnLogin;
		@FXML private Button btnExit;
		
	
	
	public ServerController() {}
		
	
	// 통신 연결 필드
	
	static Selector selector; // 넌블록킹 핵심 객체인 선택자.
	static ServerSocketChannel serverSocketChannel; // 클라의 연결요청을 수락하는 객체
	static List<ClientSocket> allUserList = new Vector<ClientSocket>(); // 연결된 클라와 통신하는 클라이언트 객체를 관리하는 객체
	static List<Room> allRoomManager = new Vector<Room>();
	static int ch = 0;
	
	
	public static void startServer() { // 서버 시작 코드
		try {
			selector = Selector.open();
			serverSocketChannel = ServerSocketChannel.open(); // 서버소켓채널 생성
			serverSocketChannel.configureBlocking(false); // 넌블록킹
			serverSocketChannel.bind(new InetSocketAddress(5001)); // 서버소켓 바인딩 해서 5001번 포트로 서버 열기
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT); // 클라 접속 수락 대기용으로 만듬
			System.out.println("서버준비 완료(한번만)");
		} catch (IOException e) {
			e.printStackTrace();
			return;
		}
		
		// 셀렉트의 관심 키셋에서 작업처리 준비된 키를 가지고 와서 처리하는 멀티 스레드
		ExecutorService executorService = Executors.newFixedThreadPool(3); // 스레드 풀
		Runnable runnable = new Runnable() {
			@Override
			public void run() {
				while(true) {
					try {
						Set<SelectionKey> selectionKeys;
						Iterator<SelectionKey> iterator;
						if(!serverSocketChannel.isOpen()) {
							selector.close();
							break;
						}else {
							System.out.println("서버준비 완료(계속)");
							selector.select(); // 작업 처리 준비된 키 감지.
							
							selectionKeys = selector.selectedKeys(); // 선택된 키셋 얻기
							iterator = selectionKeys.iterator(); // 반복자 얻기
						}
						
						while(iterator.hasNext()) {
							SelectionKey selectionKey = iterator.next(); // 키를 하나씩 꺼내옴
							if(selectionKey.isAcceptable()) { // 연결 수락 작업 처리
								accept(selectionKey);
							}else if(selectionKey.isReadable()){ // 읽기 작업 처리
								ClientSocket cl = (ClientSocket)selectionKey.attachment();
								
								cl.receive(selectionKey);
								String[] msg = cl.getStr();
								switch(msg[0]) {
								case Protocol.EXIT: exit(cl); break;
								case Protocol.SIGNUP: signUp(msg, cl, selectionKey); break;
								case Protocol.SIGNUP_CHECKID: signUpCheckId(msg[1], cl, selectionKey); break;
								case Protocol.LOGIN: loginCheck(msg[1], msg[2], cl, selectionKey); break;
								case Protocol.SEARCH_FRIEND: searchFriend(msg[1], cl, selectionKey); break;
								case Protocol.ADD_FRIEND: addFriend(msg, cl, selectionKey); break; 
								case Protocol.SET_FRIENDLIST: setFriendList(msg, cl, selectionKey);; break;
								case Protocol.DEL_FRIENDLIST: delFriend(msg, cl, selectionKey); break;
								case Protocol.CHAT_UNI: applyChatQuestion(msg, cl, selectionKey); break;
								case Protocol.CHAT_ROOM: chatRoom(msg, cl, selectionKey);  break;
								case Protocol.ROOM_EXIT: roomExit(msg[1], msg[2], cl); break;
								case Protocol.INVITE_FRIEND: inviteFriend(msg, cl, selectionKey); break;
								case Protocol.CHAT_REFUSE: chatRefuse(msg); break;
								case Protocol.CHAT_ACCEPT: chatAccept(msg); break;
								case Protocol.CHAT_MESSAGE: chatMessage(msg, cl); break;
								case Protocol.SET_TEXTAREA: setTextArea(msg[1], cl, selectionKey);  break;
								case Protocol.SET_ROOM: setRoom(msg[1], cl, selectionKey); break;
								case Protocol.UPDATE_TEL: updateTel(msg[1], msg[2], cl, selectionKey); break;
								}
								
							}else if(selectionKey.isWritable()) { // 쓰기 작업 처리
								ClientSocket cl = (ClientSocket)selectionKey.attachment();
								cl.send(selectionKey);
							}
							iterator.remove();
							
						}
					}catch(Exception e) {}
				} // end while
			}
		};
		executorService.execute(runnable);
	} // startServer 메소드 끝
	
	public static void stopServer() { // 서버 종료 코드
		try {
			Iterator<ClientSocket> it = allUserList.iterator();
			while(it.hasNext()) {
				ClientSocket client = it.next();
				client.getSocketChannel().close();
				it.remove();
				selector.close();
				serverSocketChannel.close();
			}
			if(selector.isOpen()) selector.close();
			if(serverSocketChannel.isOpen()) serverSocketChannel.close();
			System.out.println(serverSocketChannel.isOpen() + "   서버 종료 완료");
			System.exit(0);
		}catch(Exception e) {}
	}

	public static void accept(SelectionKey selectionKey) { // 새로운 클라의 연결 요청 시
		try {
			ServerSocketChannel serverSocketChannel = (ServerSocketChannel) selectionKey.channel();
			SocketChannel socketChannel = serverSocketChannel.accept();
			System.out.println("연결 수락 : " + socketChannel.getRemoteAddress() + " : " + Thread.currentThread().getName());
			System.out.println();
			
			ClientSocket client = new ClientSocket(socketChannel, selector, allUserList);
			allUserList.add(client);
			ServerMainController.setRequestLog("[" + socketChannel.getRemoteAddress() + "]" + " 님께서 접속 하셨습니다.");
		}catch(Exception e) {}
	}
	
	
	// ========================================================================================================
	
	// 클라 요청 처리 메서드
	
	public static void signUp(String[] msg, ClientSocket cl, SelectionKey sk) { // 회원가입 성공, 실패 유무 메서드
		MemberService mc = new MemberServiceCon();
		String pro = mc.signUpCheckMember(msg);
		String str = "";
		if(pro.equals(Protocol.SIGNUP_SUCCESS)) {
			str = Protocol.SIGNUP_SUCCESS;
			ServerMainController.allUserChangeSet(mc.serverAllUserList());
		}else {
			str = Protocol.SIGNUP_FAIL;
		}
		cl.setSendData(str);
		sk.interestOps(SelectionKey.OP_WRITE);
		
		try {
			ServerMainController.setRequestLog("[" + cl.getSocketChannel().getRemoteAddress() + "]" + " 님께서 회원가입 하셨습니다.");
		} catch (IOException e) {}
	}
	
	public static void signUpCheckId(String id, ClientSocket cl, SelectionKey sk) { // 아이디 체크 메서드
		MemberService mc = new MemberServiceCon();
		if(mc.isId(id) == true) {
			cl.setSendData(Protocol.SIGNUP_CHECKID_SUCCESS);
		}else {
			cl.setSendData(Protocol.SIGNUP_CHECKID_FAIL);
		}
		sk.interestOps(SelectionKey.OP_WRITE);
	}
	
	public static void loginCheck(String id, String pw, ClientSocket cl, SelectionKey sk) { // 로그인 체크 메서드
		
		MemberService mc = new MemberServiceCon();
		String[] msg = mc.login(id, pw).split(":;:");
		if(msg[0].equals(Protocol.LOGIN_SUCCESS)) {
			cl.setSendData(mc.login(id, pw));
			cl.setName(msg[1]);
			cl.setJumin(Long.parseLong(msg[2]));
			cl.setId(msg[3]);
			cl.setPassword(msg[4]);
			cl.setAge(Integer.parseInt(msg[5]));
			cl.setGender(Boolean.parseBoolean(msg[6]));
			cl.setTel(msg[7]);
			for(int i = 0; i < allUserList.size(); i++) {
				if(allUserList.get(i).getId() != null) {
					if(allUserList.get(i).getId().equals(id)) {
						ch++;
					}
				}
				if(ch == 2) {
					cl.setName("");
					cl.setJumin(0);
					cl.setId("");
					cl.setPassword("");
					cl.setAge(0);
					cl.setTel("");
					ch = 0;
					cl.setSendData(Protocol.LOGIN_DIF);
					sk.interestOps(SelectionKey.OP_WRITE);
					return;
				}
			}
			String all = "";
			for(int i = 0; i < allUserList.size(); i++) {
				if(allUserList.get(i).getId() == null) continue;
				all += allUserList.get(i).getId() + ":;:";
			}
			all = all.substring(0, all.length()-3);
			ServerMainController.connectingUserSet(all);
			ServerMainController.setLoginLogoutText( "["+ cl.getId() + "]" + " 님이 로그인 하셨습니다.");
			
		}else if(msg[0].equals(Protocol.LOGIN_FAIL)) {
			cl.setSendData(Protocol.LOGIN_FAIL);
		}
		ch = 0;
		sk.interestOps(SelectionKey.OP_WRITE);
	}
	
	public static void updateTel(String tel, String id, ClientSocket cl, SelectionKey sk) { // 전화번호 수정
		MemberService mc = new MemberServiceCon();
		cl.setSendData(mc.updateTel(tel, id));
		sk.interestOps(SelectionKey.OP_WRITE);
		ServerMainController.setRequestLog("[" + cl.getId() + "]" + " 님이 " + "전화번호를 수정하셨습니다.");
	}
	
	public static void searchFriend(String id, ClientSocket cl, SelectionKey sk) { // 새로운 친구 찾기 메서드
		MemberService mc = new MemberServiceCon();
		cl.setSendData(mc.searchId(id));
		sk.interestOps(SelectionKey.OP_WRITE);
	}
	
	public static void addFriend(String[] str, ClientSocket cl, SelectionKey sk) { // 친구 추가 메서드
		MemberService mc = new MemberServiceCon();
		String str1 = str[0] + ":;:" + cl.getId() + ":;:" + str[1];
		String[] str2 = str1.split(":;:");
		mc.addFriend(str2);
		cl.setSendData(Protocol.ADD_FRIEND_SUCCESS + ":;:" + str2[1] + ":;:" + str2[2]);
		sk.interestOps(SelectionKey.OP_WRITE);
		ServerMainController.setRequestLog("[" + cl.getId() + "]" + "님이 " + "[" + str[1] + "]" + " 님을 친구추가 하셨습니다.");
	}
	
	public static void setFriendList(String[] str, ClientSocket cl, SelectionKey sk) { // 로그인 시 친구목록 셋팅
		MemberService mc = new MemberServiceCon();
		cl.setSendData(mc.setFriend(str[1]));
		sk.interestOps(SelectionKey.OP_WRITE);
	}
	
	public static void delFriend(String[] str, ClientSocket cl, SelectionKey sk) { // 친구 삭제
		MemberService mc = new MemberServiceCon();
		cl.setSendData(mc.delFriend(str));
		sk.interestOps(SelectionKey.OP_WRITE);
		ServerMainController.setRequestLog("[" + str[1] + "]" + "님이 " + "[" + str[2] + "]" + " 님을 친구목록에서 삭제하셨습니다.");
	}
	
	public static void setRoom(String id, ClientSocket cl, SelectionKey sk) { // 로그인 시 채팅방 복구
		int roomSu = 0;
		int check = 0;
		String userSu = "";
		String userId = "";
		
		for(int i = 0; i < allRoomManager.size(); i++) {
			for(int j = 0; j < allRoomManager.get(i).getUserList().size(); j++) {
				if(allRoomManager.get(i).getUserList().get(j).getId().equals(id)) {
					check++;
					break;
				}
			}
			if(check == 1) break;
		}
		
		if(check != 0) {
			for(int i = 0; i < allRoomManager.size(); i++) {
				for(int j = 0; j < allRoomManager.get(i).getUserList().size(); j++) {
					if(allRoomManager.get(i).getUserList().get(j).getId().equals(id)) {
						cl.getRoomList().add(allRoomManager.get(i));
						allRoomManager.get(i).getUserList().add(cl);
						allRoomManager.get(i).getUserList().get(j).getRoomList().remove(allRoomManager.get(i).getUserList().get(j).getRoomList().indexOf(allRoomManager.get(i)));
						allRoomManager.get(i).getUserList().remove(j);
						userSu += allRoomManager.get(i).getUserList().size() - 1 + ":;:";
						for(int k = 0; k < allRoomManager.get(i).getUserList().size(); k++) {
							if(allRoomManager.get(i).getUserList().get(k).getId().equals(id)) continue;
							userId += allRoomManager.get(i).getUserList().get(k).getId() + ":;:";
						} // end for
						roomSu++;
						break;
					}
				} // end for
			}// end for
			String roomTot = Protocol.SET_ROOM_SUCCESS + ":;:" + roomSu + ":;:" + userSu + userId;
			roomTot = roomTot.substring(0, roomTot.length()-3);
			cl.setSendData(roomTot);
			sk.interestOps(SelectionKey.OP_WRITE);
		}
	}
	
	public static void applyChatQuestion(String[] str, ClientSocket cl, SelectionKey sk) { // 대화 신청 확인 메세지 전달
		int check = 0;
		for(int i = 0; i < allRoomManager.size(); i++) {
			if(allRoomManager.get(i).getUserList().size() == 2) {
				for(int j = 0; j < allRoomManager.get(i).getUserList().size(); j++) {
					for(int k = 1; k < str.length; k++) {
						if(allRoomManager.get(i).getUserList().get(j).getId().equals(str[k])) check++;
					} // end for
					if(check == 2) break;
				} // end for
				if(check == 2) {
					cl.setSendData(Protocol.CHAT_ALREADY);
					sk.interestOps(SelectionKey.OP_WRITE);
					return;
				}
				check = 0;
			}
		} // end for
		
		int su = -1;
		for(int i = 0; i < allUserList.size(); i++) {
			if(allUserList.get(i).getId() == null) continue;
			if(allUserList.get(i).getId().equals(str[2])) {
				allUserList.get(i).setSendData(Protocol.CHAT_QUESTION + ":;:" + str[1] + ":;:" + str[2]);
				SelectionKey key = allUserList.get(i).getSocketChannel().keyFor(selector);
				key.interestOps(SelectionKey.OP_WRITE);
				su = 2;
				break;
			}
		}
		
		if(su == -1) {
			cl.setSendData(Protocol.NOTHING);
			sk.interestOps(SelectionKey.OP_WRITE);
		}
	}
	
	public static void chatRefuse(String[] str) {  // 대화 신청 거절
		for(int i = 0; i < allUserList.size(); i++) {
			if(allUserList.get(i).getId() == null) continue;
			if(allUserList.get(i).getId().equals(str[1])) {
				allUserList.get(i).setSendData(Protocol.CHAT_REFUSE + ":;:" + str[1] + ":;:" + str[2]);
				SelectionKey key = allUserList.get(i).getSocketChannel().keyFor(selector);
				key.interestOps(SelectionKey.OP_WRITE);
				break;
			}
		}
	}
	
	public static void chatRoom(String[] str, ClientSocket cl, SelectionKey sk) { // 그룹 채팅방 만들기
		int su2 = 0;
		for(int i = 2; i < str.length; i++) {
			int su1 = -1;
			for(int j = 0; j < allUserList.size(); j++) {
				if(allUserList.get(j).getId() == null) continue;
				if(str[i].equals(allUserList.get(j).getId())) {
					su1 = 2;
					break;
				}
			}
			if(su1 == -1) {
				su2 = -1;
				break;
			}
		}
		
		if(su2 == -1) {
			cl.setSendData(Protocol.GROUP_NOTHING);
			sk.interestOps(SelectionKey.OP_WRITE);
		}else {
			Room room = new Room();
			int su = 1;
			String msg = "";
			for(int i = 1; i < str.length; i++) {
				msg += ":;:" + str[i];
			}
			for(int i = 0; i < allUserList.size(); i++) {
				for(int j = 1; j < str.length; j++) {
					if(allUserList.get(i).getId() == null) continue;
					if(allUserList.get(i).getId().equals(str[j])) {
						room.getUserList().add(allUserList.get(i));
						allUserList.get(i).getRoomList().add(room);
						allUserList.get(i).setSendData(Protocol.GROUPROOM_SUCCESS + msg);
						SelectionKey key = allUserList.get(i).getSocketChannel().keyFor(selector);
						key.interestOps(SelectionKey.OP_WRITE);
						su++;
						break;
					}
				}
				if(su == str.length) break;	
			}
			allRoomManager.add(room);
			
			String friends = "";
			for(int i = 2; i < str.length; i++) friends += "[" + str[i] + "]" + ", ";
			friends = friends.substring(0, friends.length()-2);
			
			ServerMainController.setRequestLog("[" + str[1] + "]" + "님이 " + friends + " 님들과 함께 그룹방을 개설하셨습니다.");
		}
	}
	
	public static void chatAccept(String[] str) { // 대화신청 수락
		Room room = new Room();
		int su = 1;
		String msg = "";
		for(int i = 1; i < str.length; i++) {
			msg += ":;:" + str[i];
		}
		for(int i = 0; i < allUserList.size(); i++) {
			for(int j = 1; j < str.length; j++) {
				if(allUserList.get(i).getId() == null) continue;
				if(allUserList.get(i).getId().equals(str[j])) {
					room.getUserList().add(allUserList.get(i));
					allUserList.get(i).getRoomList().add(room);
					allUserList.get(i).setSendData(Protocol.MAKEROOM_SUCCESS + msg);
					SelectionKey key = allUserList.get(i).getSocketChannel().keyFor(selector);
					key.interestOps(SelectionKey.OP_WRITE);
					su++;
					break;
				}
			}
			if(su == str.length) break;	
		}
		allRoomManager.add(room);
		ServerMainController.setRequestLog("[" + str[1] + "]" + "님과 " + "[" + str[2] + "]" + " 님이 1:1 채팅방을 개설하셨습니다.");
	}
	
	public static void chatMessage(String[] str, ClientSocket cl) { // 방에 메세지 보내기
		String message = str[3];
		if(message.length() > 24) {
			str[3] = "";
			int check = 0;
			for(int i = 0; i < message.length() - 24; i += 24) {
				check = 0;
				str[3] += message.substring(i, i+24) + "\n";
				check = i + 24;
			}
			if(check != message.length()) {
				str[3] += message.substring(check, message.length());
				str[3] += "\n";
			}
		}
		cl.getRoomList().get(Integer.parseInt(str[1])).setRoomMassage("[" + str[2] + "님의 메세지]\n" + str[3]);
		String friends = "";
		for(int i = 0; i < cl.getRoomList().get(Integer.parseInt(str[1])).getUserList().size(); i++) {
			if(cl.getRoomList().get(Integer.parseInt(str[1])).getUserList().get(i).getSocketChannel().isOpen()) {
				cl.getRoomList().get(Integer.parseInt(str[1])).getUserList().get(i).setSendData(Protocol.CHAT_RECEIVE + ":;:" + 
									cl.getRoomList().get(Integer.parseInt(str[1])).getUserList().get(i).getRoomList().indexOf(cl.getRoomList().get(Integer.parseInt(str[1]))) + ":;:" + 
									cl.getRoomList().get(Integer.parseInt(str[1])).getCommunication());
				if(!cl.getRoomList().get(Integer.parseInt(str[1])).getUserList().get(i).getId().equals(cl.getId()))
				friends += "[" + cl.getRoomList().get(Integer.parseInt(str[1])).getUserList().get(i).getId() + "]" + ", ";
				SelectionKey key = cl.getRoomList().get(Integer.parseInt(str[1])).getUserList().get(i).getSocketChannel().keyFor(selector);
				
				key.interestOps(SelectionKey.OP_WRITE);
			}
		}
		
		friends = friends.substring(0, friends.length()-2);
		ServerMainController.setRequestLog("[" + str[2] + "]" + "님이 같은방인 " + friends + " 님에게 메세지를 전송하셨습니다.");
	}
	
	public static void inviteFriend(String[] str, ClientSocket cl, SelectionKey sk) { // 방에서 친구들 초대
		int check = 0;
		for(int i = 0; i < allUserList.size(); i++) {
			if(allUserList.get(i).getId() == null) continue;
			for(int j = 4 + Integer.parseInt(str[2]); j < str.length; j++) {
				if(allUserList.get(i).getId().equals(str[j])) {
					check++;
					break;
				}	
			}
			if(check == str.length - (4 + Integer.parseInt(str[2]))) break;
		}
		if(check != str.length - (4 + Integer.parseInt(str[2]))) {
			cl.setSendData(Protocol.INVITE_FAIL);
			sk.interestOps(SelectionKey.OP_WRITE);
			return;
		}
		
		String fr = "";
		String friends = "";
		for(int k = 3; k < str.length; k++) fr += str[k] + ":;:";
		
		int su = 4 + Integer.parseInt(str[2]);
		for(int i = 0; i < allUserList.size(); i++) {
			if(allUserList.get(i).getId() == null) continue;
			for(int j = 4 + Integer.parseInt(str[2]); j < str.length; j++) {
				if(allUserList.get(i).getId().equals(str[j])) {
					cl.getRoomList().get(Integer.parseInt(str[1])).getUserList().add(allUserList.get(i));
					allUserList.get(i).getRoomList().add(cl.getRoomList().get(Integer.parseInt(str[1])));
					friends += "[" + str[j] + "]" + ", ";
					
					allUserList.get(i).setSendData(Protocol.INVITE_FRIEND + ":;:" + fr);
					SelectionKey key = allUserList.get(i).getSocketChannel().keyFor(selector);
					key.interestOps(SelectionKey.OP_WRITE);
					su++;
					break;
				}
			}
			if(su == str.length) break;
		}
		friends = friends.substring(0, friends.length()-2);
		
		int su1 = 3;
		for(int i = 0; i < allUserList.size(); i++) { // 초대 보낸 자신 제외한 올드 유저들 처리
			if(allUserList.get(i).getId() == null) continue;
			for(int j = 4; j < 4 + Integer.parseInt(str[2]); j++) {
				if(allUserList.get(i).getId().equals(str[j])) {
					allUserList.get(i).setSendData(Protocol.INVITE_OLD + ":;:" + allUserList.get(i).getRoomList().indexOf(cl.getRoomList().get(Integer.parseInt(str[1]))) + ":;:" + fr);
					SelectionKey key = allUserList.get(i).getSocketChannel().keyFor(selector);
					key.interestOps(SelectionKey.OP_WRITE);
					su1++;
					break;
				}
			}
			if(su1 == 4 + Integer.parseInt(str[2])) break;
		}
		cl.setSendData(Protocol.INVITE_OWN);
		sk.interestOps(SelectionKey.OP_WRITE);
		
		
		ServerMainController.setRequestLog("[" + cl.getId() + "]" + "님이 채팅방에 " + friends + " 님을 초대하셨습니다.");
	}
	
	public static void setTextArea(String roomList, ClientSocket cl, SelectionKey sk) { // 대화창 내용 적용
		if(cl.getRoomList().get(Integer.parseInt(roomList)).getCommunication().length() == 0){
			cl.setSendData(Protocol.SET_TEXTAREA + ":;:" + " ");
		}else {
			cl.setSendData(Protocol.SET_TEXTAREA + ":;:" + String.valueOf(cl.getRoomList().get(Integer.parseInt(roomList)).getCommunication()));
		}
		sk.interestOps(SelectionKey.OP_WRITE);
	}
	
	public static void roomExit(String roomListIndex, String id, ClientSocket cl) { // 방 나가기, 그리고 방 인원수 한명 남을시 방 폭파
		int su = -1;
		cl.getRoomList().get(Integer.parseInt(roomListIndex)).getUserList().remove(cl.getRoomList().get(Integer.parseInt(roomListIndex)).getUserList().indexOf(cl));
		if(cl.getRoomList().get(Integer.parseInt(roomListIndex)).getUserList().size() == 1) {
			ServerMainController.setRequestLog("[" + cl.getId() + "]" + "님이 " + "[" + cl.getRoomList().get(Integer.parseInt(roomListIndex)).getUserList().get(0).getId() + "]" + 
																																	" 님과의 1:1방을 나가셔서 방이 삭제되었습니다.");
			int index1 = cl.getRoomList().get(Integer.parseInt(roomListIndex)).getUserList().get(0).getRoomList().indexOf(cl.getRoomList().get(Integer.parseInt(roomListIndex)));
			cl.getRoomList().get(Integer.parseInt(roomListIndex)).getUserList().get(0).setSendData(Protocol.ROOM_DELETE + ":;:" + index1 + ":;:" + id);
			SelectionKey key = cl.getRoomList().get(Integer.parseInt(roomListIndex)).getUserList().get(0).getSocketChannel().keyFor(selector);
			key.interestOps(SelectionKey.OP_WRITE);
			cl.getRoomList().get(Integer.parseInt(roomListIndex)).getUserList().get(0).getRoomList().remove(index1);
			cl.getRoomList().get(Integer.parseInt(roomListIndex)).getUserList().remove(0);
			allRoomManager.remove(allRoomManager.indexOf(cl.getRoomList().get(Integer.parseInt(roomListIndex))));
			su = 2;
		}else {
			String friends = "";
			for(int i = 0; i < cl.getRoomList().get(Integer.parseInt(roomListIndex)).getUserList().size(); i++) {
				int index = cl.getRoomList().get(Integer.parseInt(roomListIndex)).getUserList().get(i).getRoomList().indexOf(cl.getRoomList().get(Integer.parseInt(roomListIndex)));
				cl.getRoomList().get(Integer.parseInt(roomListIndex)).getUserList().get(i).setSendData(Protocol.ROOM_EXIT + ":;:" + index + ":;:" + id + ":;:" + ("[" + id + "]" + 
																																							"님이 나가셨습니다.\n"));
				friends += "[" + cl.getRoomList().get(Integer.parseInt(roomListIndex)).getUserList().get(i).getId() + "]" + ", ";
				SelectionKey key = cl.getRoomList().get(Integer.parseInt(roomListIndex)).getUserList().get(i).getSocketChannel().keyFor(selector);
				key.interestOps(SelectionKey.OP_WRITE);
			}
			friends = friends.substring(0, friends.length()-2);
			ServerMainController.setRequestLog("[" + cl.getId() + "]" + "님이 " + friends + " 님과 함께 있던 방에서 나가셨습니다.");
		}
		if(su != 2) cl.getRoomList().get(Integer.parseInt(roomListIndex)).setRoomMassage("[" + id + "]" + "님이 나가셨습니다.\n");
		cl.getRoomList().remove(Integer.parseInt(roomListIndex));
	}
	
	public static void exit(ClientSocket cl) { // 클라 종료
		try {
			if(cl.getId() != null) ServerMainController.setLoginLogoutText( "["+ cl.getId() + "]" + " 님이 로그아웃 하셨습니다.");
			
			allUserList.remove(cl);
			cl.getSocketChannel().close();
			String all = "";
			for(int i = 0; i < allUserList.size(); i++) {
				if(allUserList.get(i).getId() == null) continue;
				all += allUserList.get(i).getId() + ":;:";
			}
			if(allUserList.size() != 0 && all.length() != 0) all = all.substring(0, all.length()-3);
			ServerMainController.connectingUserSet(all);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//===========================================================================================================================================
	
	// JavaFX

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		btnExit.setOnAction(event->handleBtnExit(event));	// 로그인 창 취소 버튼
		btnLogin.requestFocus();							// 로그인 창 열릴 시 로그인 텍스트 필드에 포커스
		btnLogin.setOnAction(event->handleBtnLogin(event));	// 로그인 버튼
		managerPw.setOnKeyPressed(new EventHandler<KeyEvent> () {	// 비밀번호 텍스트 필드 엔터키 이벤트
		    @Override
		      public void handle(KeyEvent event) {
		    	if (event.getCode().equals(KeyCode.ENTER)) handleManagerPw(event);
		      }
		});
	}
	
	public void handleBtnExit(ActionEvent event) { // 로그인 창 exit 버튼
		Platform.exit();
	}
	
	public void handleManagerPw(KeyEvent event) { // 비번 텍스트 필드 엔터키 처리
		MemberService mc = new MemberServiceCon();
		if(mc.serverLogin(managerId.getText().trim(), managerPw.getText().trim()).equals(Protocol.SERVER_LOGIN_SUCCESS)) {
			Thread thread = new Thread() {
				public void run() {
					Platform.runLater(()->{
						try {
							ServerMainController.primaryStage = primaryStage;
							Parent Main = FXMLLoader.load(getClass().getResource("/application/ServerMain.fxml"));
							Scene scene = new Scene(Main);
							Button btnLogin = (Button)parent.lookup("#btnLogin");
							Stage primaryStage = (Stage)btnLogin.getScene().getWindow();
							primaryStage.setScene(scene);
							ServerMainController.parent = Main;
							ServerMainController.allUserSet(mc.serverAllUserList());
						}catch(IOException e) {
							e.printStackTrace();
						}
					});
				} 
			};
			thread.start();
		}else {
			SignUpDialogController.serverLoginFail();
		}
	}
	
	public void handleBtnLogin(ActionEvent event) { // 로그인 버튼
		MemberService mc = new MemberServiceCon();
		if(mc.serverLogin(managerId.getText().trim(), managerPw.getText().trim()).equals(Protocol.SERVER_LOGIN_SUCCESS)) {
			Thread thread = new Thread() {
				public void run() {
					Platform.runLater(()->{
						try {
							ServerMainController.primaryStage = primaryStage;
							Parent Main = FXMLLoader.load(getClass().getResource("/application/ServerMain.fxml"));
							Scene scene = new Scene(Main);
							Button btnLogin = (Button)parent.lookup("#btnLogin");
							Stage primaryStage = (Stage)btnLogin.getScene().getWindow();
							primaryStage.setScene(scene);
							ServerMainController.parent = Main;
							ServerMainController.allUserSet(mc.serverAllUserList());
						}catch(IOException e) {
							e.printStackTrace();
						}
					});
				} 
			};
			thread.start();
		}else {
			SignUpDialogController.serverLoginFail();
		}
	}
	
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
}

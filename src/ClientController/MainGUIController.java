package ClientController;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

import application.objectVO.MemberVO;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Tab;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import protocol.Protocol;

public class MainGUIController implements Initializable{
	
	static Parent parent;
	static String friendListMsg;
	static String friends;
	static int check;
	static int check1;
	static String roomListMsg;
	static int friendListIndex;
	static int roomListIndex;
	static int roomListIndex1;
	static String friends1;
	
	// 메인 화면 친구목록 탭
	
	@FXML private Button addFriend;
	@FXML private Button delFriend;
	@FXML private Button btnCommunication;
	@FXML private Button btnGroup;
	@FXML private ListView<String> friendList;
	static ObservableList<String> friendListItem = FXCollections.observableArrayList();
	
	// 메인 화면 채팅룸 탭
	
	@FXML private Button btnInviteFriend;
	@FXML private TextArea communicationText;
	@FXML private TextField msgText;
	@FXML private Button msgButton;
	@FXML private ListView<String> roomList;
	static ObservableList<String> roomListItem = FXCollections.observableArrayList();
	@FXML private Button btnRoomExit;
	@FXML private Button btnFileSend;
	
	// 메인 화면 마이페이지 탭
	
	@FXML private Tab tapMyPage;
	@FXML private TextField textId0;
	@FXML private TextField textName0;
	@FXML private TextField textAge0;
	@FXML private TextField textGender0;
	@FXML private TextField textJumin0;
	@FXML private TextField textJumin01;
	@FXML private TextField textTel01;
	@FXML private TextField textTel02;
	@FXML private TextField textTel03;
	@FXML private Button btnCorrection;
	
	public static void setMyPage(MemberVO mv) { // 로그인 시 마이페이지에 정보 집어넣기.
		Platform.runLater(()->{
			System.out.println(mv.getName());
			TextField textName0 = (TextField)parent.lookup("#textName0");
			textName0.setText(mv.getName());
			TextField textJumin0 = (TextField)parent.lookup("#textJumin0");
			textJumin0.setText(Long.toString(mv.getJumin()).substring(0, 6));
			TextField textJumin01 = (TextField)parent.lookup("#textJumin01");
			textJumin01.setText(Long.toString(mv.getJumin()).substring(6, Long.toString(mv.getJumin()).length()));
			TextField textId0 = (TextField)parent.lookup("#textId0");
			textId0.setText(mv.getId());
			TextField textAge0 = (TextField)parent.lookup("#textAge0");
			textAge0.setText(Integer.toString(mv.getAge()));
			TextField textGender0 = (TextField)parent.lookup("#textGender0");
			textGender0.setText(mv.isGender() ? "남성" : "여성");
			TextField textTel01 = (TextField)parent.lookup("#textTel01");
			textTel01.setText(mv.getTel().substring(0, 3));
			TextField textTel02 = (TextField)parent.lookup("#textTel02");
			textTel02.setText(mv.getTel().substring(3, 7));
			TextField textTel03 = (TextField)parent.lookup("#textTel03");
			textTel03.setText(mv.getTel().substring(7, mv.getTel().length()));
		});
	}
	
	// JavaFX 부분

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Platform.runLater(()->{
			btnRoomExit.setOnAction(event->handleBtnRoomExit(event));			// 방 나가기 버튼
			btnCorrection.setOnAction(event->handleBtnCorrection(event));		// 수정 버튼
			btnInviteFriend.setOnAction(event->handleBtnInviteFriend(event));	// 친구초대 버튼
			addFriend.setOnAction(event->handleBtnAddFriend(event));			// 친구 추가 버튼
			delFriend.setOnAction(event->handleBtnDelFriend(event));			// 친구 삭제 버튼
			btnCommunication.setOnAction(event->handleBtnCommunication(event));	// 1:1 대화요청 버튼
			btnGroup.setOnAction(event->handleBtnGroup(event));					// 그룹 방 만들기 버튼
			msgButton.setOnAction(event->handleMsgButton(event));				// 메세지 전송 버튼
			friendList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);	// 친구 목록 여러개 선택 가능한 타입으로 변경
			// 친구목록 중 여러 목록 선택 시 마지막 인덱스 값과 선택한 아이디들 전부 빼내서 friends 필드에 저장
			friendList.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
				friendListIndex = friendList.getSelectionModel().getSelectedIndex();
				ObservableList<String> selectedItems = friendList.getSelectionModel().getSelectedItems();
			    StringBuilder builder = new StringBuilder();
			    for (String name : selectedItems) {
			    	builder.append(name + ":;:");
			    }
			    String[] str1 = builder.toString().split(":;:");
			    check = str1.length;
			    friends = builder.toString();
			    System.out.println(friends);
		    });
			friendList.setOnMouseClicked((MouseEvent)->{ // 친구목록 중 하나 선택 시 인덱스 값, 아이디 저장
				check1 = 2;
				friendListIndex = friendList.getSelectionModel().getSelectedIndex();
				if(friendListIndex != -1) {
					Object obj = friendList.getSelectionModel().getSelectedItem();
					friendListMsg = obj.toString();
				}
			});
			roomList.setOnMouseClicked((MouseEvent)->{ // 방 리스트 중 하나 선택 시 방 대화내용 갱신
				msgText.requestFocus();
				roomListIndex = roomList.getSelectionModel().getSelectedIndex();
				if(roomListIndex != -1) {
					Object roomObj = roomList.getSelectionModel().getSelectedItem();
					roomListMsg = roomObj.toString();
					ClientController.send(Protocol.SET_TEXTAREA + ":;:" + roomListIndex);
				}
			});
			
			msgText.setOnKeyPressed(new EventHandler<KeyEvent> () { // 메세지 입력 필드 엔터 키 이벤트
			    @Override
			      public void handle(KeyEvent event) {
			    	if (event.getCode().equals(KeyCode.ENTER)) msgTextEnterKey();
			      }
			});
		});
	}
	
	public void handleBtnRoomExit(ActionEvent event) { // 방 나가기 성공 시 (다이알로그 띄우기)
		if(roomListIndex == -1 || roomList.getItems().size() == 0 || roomListMsg == null) return;
		Platform.runLater(()->{
			StandardDialogController.roomExit();
		});
	}
	
	@SuppressWarnings("unchecked")
	public static void roomExit() { // 방 나가기 버튼
		Platform.runLater(()->{
			ListView<String> roomList = (ListView<String>)parent.lookup("#roomList");
			roomList.getItems().remove(roomListIndex);
			TextArea communicationText = (TextArea)parent.lookup("#communicationText");
			communicationText.setText("");
			ClientController.send(Protocol.ROOM_EXIT + ":;:" + roomListIndex + ":;:" + ClientController.mv.getId());
			roomListIndex = -1;
		});
	}
	
	@SuppressWarnings("unchecked")
	public void handleBtnInviteFriend(ActionEvent event) { // 방에서 친구 초대 버튼
		if(roomListIndex == -1 || roomList.getItems().size() == 0 || roomListMsg == null) return;
		Platform.runLater(()->{
			ListView<String> friendList = (ListView<String>)parent.lookup("#friendList");
			String joined = friendList.getItems().stream().map(Object::toString).collect(Collectors.joining(":;:"));
			String[] str = joined.split(":;:");
			String[] str1 = roomListMsg.split(", ");
			InviteFriendDialogController.inviteFriend(roomListIndex, str1, str);
		});
	}
	
	public void msgTextEnterKey() { // 메세지 보내기 엔터
		if(roomListIndex == -1 || roomList.getItems().size() == 0 || msgText.getText().trim().length() == 0) {
			msgText.clear();
			return;
		}
		ClientController.send(Protocol.CHAT_MESSAGE + ":;:" + roomListIndex + ":;:" + ClientController.mv.getId() + ":;:" + msgText.getText()); 
		msgText.clear();
	}
	
	public void handleMsgButton(ActionEvent event) {// 메세지 보내기 전송버튼
		if(roomListIndex == -1 || roomList.getItems().size() == 0 || msgText.getText().trim().length() == 0) {
			msgText.clear();
			return;
		}
		ClientController.send(Protocol.CHAT_MESSAGE + ":;:" + roomListIndex + ":;:" + ClientController.mv.getId() + ":;:" + msgText.getText()); 
		msgText.clear();
	}
	
	public void handleBtnGroup(ActionEvent event) { // 그룹 채팅 (만들기) 버튼
		if(friendListIndex == -1 || friendList.getItems().size() == 0 || check <= 1) return;
		String[] str = friends.split(":;:");
		String str1 = "";
		if(str[str.length-1] == null) {
			for(int i = 0; i< str.length-1; i++) {
				str1 += str[i] + ":;:";
			}
			friends = str1;
		}else {
			for(int i = 0; i< str.length; i++) {
				str1 += str[i] + ":;:";
			}
			friends = str1;
		}
		ClientController.send(Protocol.CHAT_ROOM + ":;:" + ClientController.mv.getId() + ":;:" + friends);
	}
	
	public void handleBtnCommunication(ActionEvent event) { // 목록에서 선택한 친구에게 1:1 대화 신청
		if(friendListIndex == -1 || friendList.getItems().size() == 0 || check > 1 || check1 != 2) return;
		ClientController.send(Protocol.CHAT_UNI + ":;:" + ClientController.mv.getId() + ":;:" + friendListMsg);
	}
	
	public void handleBtnAddFriend(ActionEvent event) { // 메인 화면 친구 추가 버튼
		AddFriendDialogController.addFriend();
	}
	
	public void handleBtnDelFriend(ActionEvent event) { // 메인 화면 친구 삭제 버튼
		if(friendListIndex == -1 || friendList.getItems().size() == 0 || check1 != 2) return;
		StandardDialogController.delFriend(friendListMsg);
	}
	
	public void handleBtnCorrection(ActionEvent event) { // 수정 버튼
		
		Platform.runLater(()->{
			if(textTel01.getText().trim().length() != 3 || (textTel02.getText().trim().length() != 4 && textTel02.getText().trim().length() != 3) || 
					textTel03.getText().trim().length() != 4) {
				SignUpDialogController.updateFail();
				return;
			}
			String tel = textTel01.getText().trim() + textTel02.getText().trim() + textTel03.getText().trim();
			String id = ClientController.mv.getId();
			ClientController.send(Protocol.UPDATE_TEL + ":;:" + tel + ":;:" + id);
		});
	}
	
	@SuppressWarnings("unchecked")
	public static void delFriend() { // 친구 삭제 다이알로그 확인 버튼 클릭 시 친구 삭제
		Platform.runLater(()->{
			ListView<String> friendList = (ListView<String>)parent.lookup("#friendList");
			friendList.getItems().remove(friendListIndex);
			ClientController.send(Protocol.DEL_FRIENDLIST + ":;:" + ClientController.mv.getId() + ":;:" + friendListMsg);
		});
	}
	
	@SuppressWarnings("unchecked")
	public static void friendListSet(String[] str) { // 메인 화면 친구 목록 친구 생성
		Platform.runLater(()->{
			ListView<String> friendList = (ListView<String>)parent.lookup("#friendList");
			friendListItem.add(str[2]);
			friendList.setItems(friendListItem);
			AddFriendDialogController.addFriendSuccess();
		});
	}
	
	@SuppressWarnings("unchecked")
	public static void setFriendList(String[] str) { // 로그인 시 친구 목록 생성
		Platform.runLater(()->{
			ListView<String> friendList = (ListView<String>)parent.lookup("#friendList");
			ObservableList<String> item = FXCollections.observableArrayList();
			for(int i = 1; i < str.length; i++) {
				item.add(str[i]);
			}
			friendList.setItems(item);
			MainGUIController.friendListItem = item;
			ClientController.send(Protocol.SET_ROOM + ":;:" + ClientController.mv.getId());
		});
	}
	
	@SuppressWarnings("unchecked")
	public static void setRoom(String[] str) { // 로그인 시 방 복구
		
		String userSu = "";
		String userId = "";
		String[] userSu1;
		String[] userId1;
		
		for(int i = 2; i < 2 + Integer.parseInt(str[1]); i++) userSu += str[i] + ":;:";
		for(int i = 2 + Integer.parseInt(str[1]); i < str.length; i++) userId += str[i] + ":;:";
		
		userSu1 = userSu.split(":;:");
		userId1 = userId.split(":;:");
		Platform.runLater(()->{
			ListView<String> roomList = (ListView<String>)parent.lookup("#roomList");
			ObservableList<String> item = FXCollections.observableArrayList();
			String userId2 = "";
			for(int i = 0; i < Integer.parseInt(userSu1[0]); i++) {
				userId2 += userId1[i] + ", ";
			}
			userId2 = userId2.substring(0, userId2.length()-2);
			item.add(userId2);
			
			for(int i = 1; i < Integer.parseInt(str[1]); i++) {
				userId2 = "";
				for(int j = Integer.parseInt(userSu1[i-1]); j < Integer.parseInt(userSu1[i-1]) + Integer.parseInt(userSu1[i]); j++) userId2 += userId1[j] + ", ";
				userId2 = userId2.substring(0, userId2.length()-2);
				item.add(userId2);
				userSu1[i] = String.valueOf(Integer.parseInt(userSu1[i - 1]) + Integer.parseInt(userSu1[i]));
			}
			roomList.setItems(item);
			MainGUIController.roomListItem = item;
		});
		
	}
	
	@SuppressWarnings("unchecked")
	public static boolean checkFriend(String friend) { // 친구추가 시 친구목록에 존재 유무 판단
		ListView<String> friendList = (ListView<String>)parent.lookup("#friendList");
		String joined = friendList.getItems().stream().map(Object::toString).collect(Collectors.joining(":;:"));
		String[] str = joined.split(":;:");
		for(int i = 0; i < str.length; i++) {
			if(friend.equals(str[i])) {
				return false;
			}
		}
		return true;
	}
	
	@SuppressWarnings("unchecked")
	public static void makeRoom(String[] str) { // 메인화면 채팅룸 채팅방 리스트 생성(1:1 채팅)
		Platform.runLater(()->{
			ListView<String> roomList = (ListView<String>)parent.lookup("#roomList");
			for(int i = 1; i < str.length; i++) {
				if(ClientController.mv.getId().equals(str[i])) continue;
				roomListItem.add(str[i]);
			}
			roomList.setItems(roomListItem);
			SignUpDialogController.makeRoomSuccess();
		});
	}
	
	@SuppressWarnings("unchecked")
	public static void groupRoom(String[] str) { // 메인화면 채팅룸 채팅방 리스트 생성(그룹 채팅)
		Platform.runLater(()->{
			String friend = "";
			ListView<String> roomList = (ListView<String>)parent.lookup("#roomList");
			for(int i = 1; i < str.length-1; i++) {
				if(ClientController.mv.getId().equals(str[i])) continue;
				friend += str[i] + ", ";
			}
			friend = friend.substring(0, friend.length()-2);
			if(!ClientController.mv.getId().equals(str[str.length-1])) friend += ", " + str[str.length-1];
			roomListItem.add(friend);
			roomList.setItems(roomListItem);
		});
	}
	
	public static void chatReceive(String roomIndex, String message) { // 메세지 받고 채팅화면 적용
		Platform.runLater(()->{
			if(!String.valueOf(roomListIndex).equals(roomIndex)) return;
			TextArea communicationText = (TextArea)parent.lookup("#communicationText");
			communicationText.clear();
			communicationText.setText(message);
			communicationText.positionCaret(communicationText.getText().length());
		});
	}
	
	public static void chatPresent(String message) { // 채팅 화면 적용
		Platform.runLater(()->{
			TextArea communicationText = (TextArea)parent.lookup("#communicationText");
			communicationText.clear();
			communicationText.setText(message);
			communicationText.positionCaret(communicationText.getText().length());
		});
	}
	
	@SuppressWarnings("unchecked")
	public static void inviteFriend(String[] str) { // 채팅방 초대 받기
		Platform.runLater(()->{
			String friend = "";
			ListView<String> roomList = (ListView<String>)parent.lookup("#roomList");
			for(int i = 1; i < str.length-1; i++) {
				if(ClientController.mv.getId().equals(str[i])) continue;
				friend += str[i] + ", ";
			}
			friend = friend.substring(0, friend.length()-2);
			if(!ClientController.mv.getId().equals(str[str.length-1])) friend += ", " + str[str.length-1];
			roomListItem.add(friend);
			roomList.setItems(roomListItem);
		});
	}
	
	@SuppressWarnings("unchecked")
	public static void inviteOwn() { // 채팅방으로 친구들 초대시 방 이름 새로고침
		Platform.runLater(()->{
			String roomFriend = "";
			String[] str = friends1.split(":;:");
			for(int i = 0; i < str.length; i++) roomFriend += ", " + str[i];
			
			ListView<String> roomList = (ListView<String>)parent.lookup("#roomList");
			String joined = roomList.getItems().stream().map(Object::toString).collect(Collectors.joining(":;:"));
			String[] str1 = joined.split(":;:");
			ObservableList<String> roomListItem = FXCollections.observableArrayList();
			for(int i = 0; i < str1.length; i++) {
				if(i == roomListIndex1) str1[i] += roomFriend;
				roomListItem.add(str1[i]);
			}
			MainGUIController.roomListItem = roomListItem;
			roomList.setItems(roomListItem);
		});
		
	}
	
	public static void inviteOwnSecond(int roomListIndex, String friends) { // 채팅방으로 친구들 초대시 방 이름 새로고침 보조메서드
		friends1 = friends;
		roomListIndex1 = roomListIndex;
	}
	
	@SuppressWarnings("unchecked")
	public static void inviteOld(int roomListIndex, String friends) { // 다른 친구를 채팅방에 초대시 초대 보낸 사람 제외한 원래 멤버들 처리
		Platform.runLater(()->{
			String roomFriend = "";
			String[] str = friends.split(":;:");
			for(int i = 0; i < str.length; i++) {
				if(ClientController.mv.getId().equals(str[i])) continue;
				roomFriend += str[i] + ", ";
			}
			roomFriend = roomFriend.substring(0, roomFriend.length()-2);
			
			ListView<String> roomList = (ListView<String>)parent.lookup("#roomList");
			String joined = roomList.getItems().stream().map(Object::toString).collect(Collectors.joining(":;:"));
			String[] str1 = joined.split(":;:");
			ObservableList<String> roomListItem = FXCollections.observableArrayList();
			for(int i = 0; i < str1.length; i++) {
				if(i == roomListIndex) str1[i] = roomFriend;
				roomListItem.add(str1[i]);
			}
			MainGUIController.roomListItem = roomListItem;
			roomList.setItems(roomListItem);
		});
	}
	
	@SuppressWarnings("unchecked")
	public static void roomExit(String index, String id, String exitMessage) { // 방에서 누군가 나갔을 때 방 제목에서 나간 사람 아이디 빼기.
		Platform.runLater(()->{
			ListView<String> roomList = (ListView<String>)parent.lookup("#roomList");
			String joined = roomList.getItems().stream().map(Object::toString).collect(Collectors.joining(":;:"));
			String[] str = joined.split(":;:");
			ObservableList<String> roomListItem = FXCollections.observableArrayList();
			for(int i = 0; i < str.length; i++) {
				if(i == Integer.parseInt(index)) {
					String[] ss = str[i].split(", ");
					str[i] = "";
					for(int j = 0; j < ss.length; j++) {
						if(ss[j].equals(id)) continue;
						str[i] += ss[j] + ", ";
					}
					str[i] = str[i].substring(0, str[i].length()-2);
				}
				roomListItem.add(str[i]);
			}
			if(roomListIndex == Integer.parseInt(index)) {
				TextArea communicationText = (TextArea)parent.lookup("#communicationText");
				communicationText.setText(communicationText.getText() + exitMessage);
			}
			MainGUIController.roomListItem = roomListItem;
			roomList.setItems(roomListItem);
		});
	}
	
	@SuppressWarnings("unchecked")
	public static void roomDelete(String index, String id) { // 방 인원수 한명남을 시 폭파
		Platform.runLater(()->{
			ListView<String> roomList = (ListView<String>)parent.lookup("#roomList");
			roomList.getItems().remove(Integer.parseInt(index));
			TextArea communicationText = (TextArea)parent.lookup("#communicationText");
			communicationText.setText("");
			roomListIndex = -1;
			SignUpDialogController.roomDelete(id);
		});
	}
}

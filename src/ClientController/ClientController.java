package ClientController;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;
import java.util.ResourceBundle;

import application.objectVO.MemberVO;
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
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import protocol.Protocol;

public class ClientController implements Initializable{
	
	
	// 통신 연결 필드
	
	private static SocketChannel socketChannel;
	public static MemberVO mv;
	
	// JavzFX 필드
	
		// 일반 필드

		public static Parent parent;
		public static Stage primaryStage;
	
		// 로그인 창 필드
		
		@FXML private Button btnSignUp;
		@FXML private Button btnExit;
		@FXML private Button btnLogin;
		@FXML private TextField loginId;
		@FXML private TextField loginPw;
		
		public ClientController() {}
	
	
	
	// 클라이언트의 통신 기능 부분
	
	public static void startClient() { // 서버와 통신 연결
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					socketChannel = SocketChannel.open();
					socketChannel.configureBlocking(true);
					socketChannel.connect(new InetSocketAddress("localhost", 5001));
					System.out.println("연결 완료 : " + socketChannel.getRemoteAddress());
					System.out.println();
				}catch(Exception e) {
					System.out.println("서버 통신 안됨");
					if(socketChannel.isOpen()) { stopClient(); }
					return;
				}
				receive();
			}
		};
		thread.start();
	}
	
	public static void stopClient() { // 서버와 통신 끊음
		try {
			socketChannel.close();
			Platform.runLater(()->{
				primaryStage.close();
			});
			System.out.println("연결 끊음");
		}catch(IOException ioe) {
			ioe.printStackTrace();
		}
	}
	
	public static void receive() { // 서버의 메세지를 받음
		while(true) {
			try {
				ByteBuffer byteBuffer = ByteBuffer.allocate(10000);
				
				int readByteCount = socketChannel.read(byteBuffer);
				
				if(readByteCount == -1) {
					System.out.println("서버가 정상적으로 소켓의 close() 를 호출함");
					throw new IOException();
				}
				
				byteBuffer.flip();
			
				Charset charset = Charset.forName("UTF-8");
				String data = charset.decode(byteBuffer).toString();
				String[] str = data.split(":;:");
				
				switch(str[0]) {
				case Protocol.EXIT: stopClient(); break;
				case Protocol.SIGNUP_SUCCESS: SignUpDialogController.signUpSuccess(); break;
				case Protocol.SIGNUP_FAIL: SignUpDialogController.signUpFail(); break;
				case Protocol.SIGNUP_CHECKID_SUCCESS: SignUpDialogController.checkIdSuccess(); break;
				case Protocol.SIGNUP_CHECKID_FAIL: SignUpDialogController.checkIdFail(); break;
				case Protocol.LOGIN_SUCCESS: loginSuccess(str); break;
				case Protocol.LOGIN_FAIL: SignUpDialogController.loginFail(); break;
				case Protocol.LOGIN_DIF: SignUpDialogController.loginDif(); break;
				case Protocol.UPDATE_TEL_SUCCESS: SignUpDialogController.updateTelSuccess(); break;
				case Protocol.UPDATE_TEL_FAIL: SignUpDialogController.updateTelFail(); break;
				case Protocol.SEARCH_FRIEND_SUCCESS: AddFriendDialogController.addFriendList(str); break;
				case Protocol.SEARCH_FRIEND_FAIL: SignUpDialogController.searchFriendFail(); break;
				case Protocol.ADD_FRIEND_SUCCESS: MainGUIController.friendListSet(str); break; 
				case Protocol.SET_FRIENDLIST_SUCCESS: MainGUIController.setFriendList(str); break;
				case Protocol.DEL_FRIENDLIST_SUCCESS: SignUpDialogController.delFriend(); break;
				case Protocol.NOTHING: SignUpDialogController.nothing(); break;
				case Protocol.GROUP_NOTHING: SignUpDialogController.groupNothing(); break;
				case Protocol.CHAT_QUESTION: StandardDialogController.chatQuestion(str); break;
				case Protocol.CHAT_REFUSE: SignUpDialogController.chatRefuse(str); break;
				case Protocol.CHAT_ALREADY: SignUpDialogController.chatAlready(); break;
				case Protocol.MAKEROOM_SUCCESS: MainGUIController.makeRoom(str); break;
				case Protocol.GROUPROOM_SUCCESS: MainGUIController.groupRoom(str); break;
				case Protocol.SET_ROOM_SUCCESS: MainGUIController.setRoom(str); break;
				case Protocol.ROOM_EXIT: MainGUIController.roomExit(str[1], str[2], str[3]); break;
				case Protocol.ROOM_DELETE: MainGUIController.roomDelete(str[1], str[2]);  break; 
				case Protocol.CHAT_RECEIVE: MainGUIController.chatReceive(str[1], str[2]); break;
				case Protocol.SET_TEXTAREA: MainGUIController.chatPresent(str[1]);  break;
				case Protocol.INVITE_FRIEND: MainGUIController.inviteFriend(str); break;
				case Protocol.INVITE_FAIL: SignUpDialogController.inviteFail(); break;
				case Protocol.INVITE_OWN: MainGUIController.inviteOwn(); break;
				case Protocol.INVITE_OLD: 
					String friends = "";
					for(int i = 2; i < str.length-1; i++) friends += str[i] + ":;:";
					friends += str[str.length-1];
					MainGUIController.inviteOld(Integer.parseInt(str[1]), friends); break;
				}
				
				System.out.println("받기 완료 : " + data);
			}catch(Exception e) {
				System.out.println("서버 통신 안됨");
				stopClient();
				break;
			}
		}
	}
	
	public static void send(String data) { // 서버에게 메세지 전송
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					Charset charset = Charset.forName("UTF-8");
					ByteBuffer byteBuffer = charset.encode(data);
					socketChannel.write(byteBuffer);
					System.out.println("보내기 완료!!");
					System.out.println(data);
				}catch(Exception e) {
					System.out.println("서버 통신 안됨");
					stopClient();
				}
			}
		};
		thread.start();
	}
	
	// ===============================================================================================================================
	
	// 통신 처리 부분
	
	public static void loginSuccess(String[] str) { // 로그인 성공 - 메인화면으로
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Parent Main = FXMLLoader.load(getClass().getResource("/application/MainGUI.fxml"));
						Scene scene = new Scene(Main);
						MainGUIController.parent = Main;
						SignUpDialogController.parent = Main;
						scene.getStylesheets().add(getClass().getResource("/application/application.css").toString());
						Button btnLogin = (Button)parent.lookup("#btnLogin");
						Stage primaryStage = (Stage)btnLogin.getScene().getWindow();
						primaryStage.setScene(scene);
						AddFriendDialogController.primaryStage = primaryStage;
					}catch(IOException e) {
						e.printStackTrace();
					}
				});
			} 
		};
		thread.start();
		
		mv = new MemberVO(str[1], Long.parseLong(str[2]), str[3], str[4], Integer.parseInt(str[5]), (str[6].equals("남성") ? true : false), str[7]);
		MainGUIController.setMyPage(mv);
		System.out.println(str[1] + "  " + str[2]);
		System.out.println(mv.getName() + " " + mv.getJumin() + " " + mv.isGender());
		System.out.println("클라 로그인 성공");
		send(Protocol.SET_FRIENDLIST + ":;:" + mv.getId());
	}
	
	
	// ===============================================================================================================================
	
	// JavaFX 처리 부분
	
	// 셋팅 메서드
	
	public void setPrimaryStage(Stage primaryStage) {
		this.primaryStage = primaryStage;
	}
	
	
	// JavaFX 생성자 부분
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Platform.runLater(()->{
			primaryStage.setOnCloseRequest(event->handleXButton(event));
			btnExit.setOnAction(event->handleBtnExit(event));
			btnSignUp.setOnAction(event->handleBtnSignUp(event));
			btnLogin.setOnAction(event->handleBtnLogin(event));
			loginPw.setOnKeyPressed(new EventHandler<KeyEvent> () {
			    @Override
			      public void handle(KeyEvent event) {
			    	if (event.getCode().equals(KeyCode.ENTER)) handleLoginPwEnter(event);
			      }
			});
		});
	}
	
	// JavaFX 이벤트 메서드
	
	public void handleBtnExit(ActionEvent event) {
		send(Protocol.EXIT);
	}
	
	public void handleXButton(WindowEvent event) { // 우측 상단 X 버튼
		System.out.println("X 버튼 클릭");
		System.out.println("클라 종료");
		send(Protocol.EXIT);
	}
	
	public void handleLoginPwEnter(KeyEvent event) { // 로그인 화면 로그인 버튼
		if(loginId.getText().trim().length() == 0 || loginPw.getText().trim().length() == 0) {
			SignUpDialogController.loginCheck();
			return;
		}
		String data = Protocol.LOGIN + ":;:" + loginId.getText().trim() + ":;:" + loginPw.getText().trim();
		ClientController.send(data);
	}
	
	public void handleBtnSignUp(ActionEvent event) { // 로그인 창 회원가입 버튼
		Thread thread = new Thread() {
			@Override
			public void run() {
				Platform.runLater(()->{
					try {
						Parent signUp = FXMLLoader.load(getClass().getResource("/application/SignUp.fxml"));
						StackPane root = (StackPane)btnSignUp.getScene().getRoot();
						root.getChildren().add(signUp);
						SignUpDialogController.parent = signUp;
					}catch(IOException e) {
						e.printStackTrace();
					}
				});
			}
		};
		thread.start();
	}
	
	public void handleBtnLogin(ActionEvent event) { // 로그인 화면 로그인 버튼
		if(loginId.getText().trim().length() == 0 || loginPw.getText().trim().length() == 0) {
			SignUpDialogController.loginCheck();
			return;
		}
		String data = Protocol.LOGIN + ":;:" + loginId.getText().trim() + ":;:" + loginPw.getText().trim();
		ClientController.send(data);
	}
	
}

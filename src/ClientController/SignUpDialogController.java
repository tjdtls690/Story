package ClientController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import protocol.Protocol;
import server.controller.ServerController;

public class SignUpDialogController implements Initializable{
	
	public static Parent parent;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
	}
	
	public static void checkIdSuccess() { // 아이디 중복확인 성공
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(ClientController.primaryStage);
						dialog.setTitle("아이디 중복 확인");
						
						Parent parent = FXMLLoader.load(getClass().getResource("/application/SignUpDialog.fxml"));
						Label signUpText = (Label)parent.lookup("#signUpText");
						signUpText.setText("사용할 수 있는 ID 입니다.");
						Button btnSignUpOK = (Button)parent.lookup("#btnSignUpOK");
						btnSignUpOK.setOnAction(event->{
							dialog.close();
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
						btnSignUpOK.requestFocus();
						SignUpController.checkID = true;
					}catch(IOException e) {}
				});
				
			} 
		};
		thread.start();
	}
	
	public static void checkIdFail() { // 아이디 중복확인 실패 메서드
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(ClientController.primaryStage);
						dialog.setTitle("아이디 중복 확인");
						
						Parent parent = FXMLLoader.load(getClass().getResource("/application/SignUpDialog.fxml"));
						Label signUpText = (Label)parent.lookup("#signUpText");
						signUpText.setText("사용할 수 없는 ID 입니다.");
						Button btnSignUpOK = (Button)parent.lookup("#btnSignUpOK");
						btnSignUpOK.setOnAction(event->{
							dialog.close();
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
						btnSignUpOK.requestFocus();
						SignUpController.checkID = false;
					}catch(IOException e) {}
				});
				
			} 
		};
		thread.start();
	}
	
	public static void checkId() { // 아이디 중복확인 안하고 회원가입 눌렀을 시.
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(ClientController.primaryStage);
						dialog.setTitle("아이디 중복 확인");
						
						Parent parent = FXMLLoader.load(getClass().getResource("/application/SignUpDialog.fxml"));
						Label signUpText = (Label)parent.lookup("#signUpText");
						signUpText.setText("ID 중복을 확인하세요.");
						Button btnSignUpOK = (Button)parent.lookup("#btnSignUpOK");
						btnSignUpOK.setOnAction(event->{
							dialog.close();
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
						btnSignUpOK.requestFocus();
					}catch(IOException e) {}
				});
			} 
		};
		thread.start();
	}
	
	public static void signUpSuccess() {  // 회원가입 성공 시 메서드
		System.out.println("회원가입 성공!");
		
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(ClientController.primaryStage);
						dialog.setTitle("회원가입 확인");
						Parent parent = FXMLLoader.load(getClass().getResource("/application/SignUpDialog.fxml"));
						Label signUpText = (Label)parent.lookup("#signUpText");
						signUpText.setText("회원가입을 성공하셨습니다!!");
						Button btnSignUpOK = (Button)parent.lookup("#btnSignUpOK");
						btnSignUpOK.setOnAction(event->{
							dialog.close();
							SignUpController.signUpOK(SignUpDialogController.parent);
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
						btnSignUpOK.requestFocus();
						SignUpController.checkID = false;
					}catch(IOException e) {}
				});
			} 
		};
		thread.start();
	}
	
	public static void signUpFail() {  // 회원가입 실패 시 메서드
		System.out.println("회원가입 성공!");
		
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(ClientController.primaryStage);
						dialog.setTitle("회원가입 확인");
						Parent parent = FXMLLoader.load(getClass().getResource("/application/SignUpDialog.fxml"));
						Label signUpText = (Label)parent.lookup("#signUpText");
						signUpText.setText("잘못 입력하셨습니다.\n  다시 입력해주세요.");
						Button btnSignUpOK = (Button)parent.lookup("#btnSignUpOK");
						btnSignUpOK.setOnAction(event->{
							dialog.close();
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
						btnSignUpOK.requestFocus();
					}catch(IOException e) {}
				});
			} 
		};
		thread.start();
	}
	
	public static void loginFail() { // 로그인 실패 시
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(ClientController.primaryStage);
						dialog.setTitle("로그인 실패");
						
						Parent parent = FXMLLoader.load(getClass().getResource("/application/SignUpDialog.fxml"));
						Label signUpText = (Label)parent.lookup("#signUpText");
						signUpText.setText("잘못 입력하셨습니다.\n  다시 입력해주세요.");
						Button btnSignUpOK = (Button)parent.lookup("#btnSignUpOK");
						btnSignUpOK.setOnAction(event->{
							dialog.close();
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
						btnSignUpOK.requestFocus();
					}catch(IOException e) {}
				});
				
			} 
		};
		thread.start();
	}
	
	public static void loginCheck() { // 로그인 시 하나라도 입력 안하면.
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(ClientController.primaryStage);
						dialog.setTitle("로그인 실패");
						Parent parent = FXMLLoader.load(getClass().getResource("/application/SignUpDialog.fxml"));
						Label signUpText = (Label)parent.lookup("#signUpText");
						signUpText.setText("전부 입력해주세요.");
						Button btnSignUpOK = (Button)parent.lookup("#btnSignUpOK");
						btnSignUpOK.setOnAction(event->{
							dialog.close();
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
						btnSignUpOK.requestFocus();
					}catch(IOException e) {}
				});
			} 
		};
		thread.start();
	}
	
	public static void searchFriendFail() { // 친구 검색 실패
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(AddFriendDialogController.primaryStage);
						dialog.setTitle("친구 검색 실패");
						
						Parent parent = FXMLLoader.load(getClass().getResource("/application/SignUpDialog.fxml"));
						Label signUpText = (Label)parent.lookup("#signUpText");
						signUpText.setText("검색한 아이디가 없습니다.");
						Button btnSignUpOK = (Button)parent.lookup("#btnSignUpOK");
						btnSignUpOK.setOnAction(event->{
							dialog.close();
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
						btnSignUpOK.requestFocus();
					}catch(IOException e) {}
				});
			} 
		};
		thread.start();
	}
	
	public static void writeName() { // 회원가입 누를 시 이름 안적었을 때
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(ClientController.primaryStage);
						dialog.setTitle("회원가입 실패");
						
						Parent parent = FXMLLoader.load(getClass().getResource("/application/SignUpDialog.fxml"));
						Label signUpText = (Label)parent.lookup("#signUpText");
						signUpText.setText("이름을 입력해주세요.");
						Button btnSignUpOK = (Button)parent.lookup("#btnSignUpOK");
						btnSignUpOK.setOnAction(event->{
							dialog.close();
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
						btnSignUpOK.requestFocus();
					}catch(IOException e) {}
				});
			} 
		};
		thread.start();
	}
	
	public static void writeJumin() { // 회원가입 누를 시 주민번호 안적었을 때
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(ClientController.primaryStage);
						dialog.setTitle("회원가입 실패");
						
						Parent parent = FXMLLoader.load(getClass().getResource("/application/SignUpDialog.fxml"));
						Label signUpText = (Label)parent.lookup("#signUpText");
						signUpText.setText("주민번호를 입력해주세요.");
						Button btnSignUpOK = (Button)parent.lookup("#btnSignUpOK");
						btnSignUpOK.setOnAction(event->{
							dialog.close();
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
						btnSignUpOK.requestFocus();
					}catch(IOException e) {}
				});
			} 
		};
		thread.start();
	}
	
	public static void writeId() { // 회원가입 누를 시 아이디 안적었을 때
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(ClientController.primaryStage);
						dialog.setTitle("회원가입 실패");
						
						Parent parent = FXMLLoader.load(getClass().getResource("/application/SignUpDialog.fxml"));
						Label signUpText = (Label)parent.lookup("#signUpText");
						signUpText.setText("아이디를 입력해주세요.");
						Button btnSignUpOK = (Button)parent.lookup("#btnSignUpOK");
						btnSignUpOK.setOnAction(event->{
							dialog.close();
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
						btnSignUpOK.requestFocus();
					}catch(IOException e) {}
				});
			} 
		};
		thread.start();
	}
	
	public static void writePw() { // 회원가입 누를 시 비밀번호 안적었을 때
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(ClientController.primaryStage);
						dialog.setTitle("회원가입 실패");
						
						Parent parent = FXMLLoader.load(getClass().getResource("/application/SignUpDialog.fxml"));
						Label signUpText = (Label)parent.lookup("#signUpText");
						signUpText.setText("비밀번호를 입력해주세요.");
						Button btnSignUpOK = (Button)parent.lookup("#btnSignUpOK");
						btnSignUpOK.setOnAction(event->{
							dialog.close();
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
						btnSignUpOK.requestFocus();
					}catch(IOException e) {}
				});
			} 
		};
		thread.start();
	}
	
	public static void writeAge() { // 회원가입 누를 시 나이 안적었을 때
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(ClientController.primaryStage);
						dialog.setTitle("회원가입 실패");
						
						Parent parent = FXMLLoader.load(getClass().getResource("/application/SignUpDialog.fxml"));
						Label signUpText = (Label)parent.lookup("#signUpText");
						signUpText.setText("나이를 입력해주세요.");
						Button btnSignUpOK = (Button)parent.lookup("#btnSignUpOK");
						btnSignUpOK.setOnAction(event->{
							dialog.close();
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
						btnSignUpOK.requestFocus();
					}catch(IOException e) {}
				});
			} 
		};
		thread.start();
	}
	
	public static void writeTel() { // 회원가입 누를 시 전화번호 안적었을 때
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(ClientController.primaryStage);
						dialog.setTitle("회원가입 실패");
						
						Parent parent = FXMLLoader.load(getClass().getResource("/application/SignUpDialog.fxml"));
						Label signUpText = (Label)parent.lookup("#signUpText");
						signUpText.setText("전화번호를 입력해주세요.");
						Button btnSignUpOK = (Button)parent.lookup("#btnSignUpOK");
						btnSignUpOK.setOnAction(event->{
							dialog.close();
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
						btnSignUpOK.requestFocus();
					}catch(IOException e) {}
				});
			} 
		};
		thread.start();
	}
	
	public static void password() { // 회원가입 누를 시 비밀번호와 비밀번호 확인이 다를 때
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(ClientController.primaryStage);
						dialog.setTitle("회원가입 실패");
						
						Parent parent = FXMLLoader.load(getClass().getResource("/application/SignUpDialog.fxml"));
						Label signUpText = (Label)parent.lookup("#signUpText");
						signUpText.setText("비밀번호 확인이 틀립니다.");
						Button btnSignUpOK = (Button)parent.lookup("#btnSignUpOK");
						btnSignUpOK.setOnAction(event->{
							dialog.close();
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
						btnSignUpOK.requestFocus();
					}catch(IOException e) {}
				});
			} 
		};
		thread.start();
	}
	
	public static void addFriendSuccess() { // 친구 추가 성공 시
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(AddFriendDialogController.primaryStage);
						dialog.setTitle("친구추가 성공");
						
						Parent parent = FXMLLoader.load(getClass().getResource("/application/SignUpDialog.fxml"));
						Label signUpText = (Label)parent.lookup("#signUpText");
						signUpText.setText("친구 추가 성공!!");
						Button btnSignUpOK = (Button)parent.lookup("#btnSignUpOK");
						btnSignUpOK.setOnAction(event->{
							dialog.close();
							ClientController.send(Protocol.SET_FRIENDLIST + ":;:" + ClientController.mv.getId());
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
						btnSignUpOK.requestFocus();
						System.out.println(ClientController.mv.getId());
					}catch(IOException e) {}
				});
			} 
		};
		thread.start();
	}
	
	public static void addFriendFail() { // 친구 추가 실패 시
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(AddFriendDialogController.primaryStage);
						dialog.setTitle("친구추가 실패");
						
						Parent parent = FXMLLoader.load(getClass().getResource("/application/SignUpDialog.fxml"));
						Label signUpText = (Label)parent.lookup("#signUpText");
						signUpText.setText("이미 친구가 존재합니다.");
						Button btnSignUpOK = (Button)parent.lookup("#btnSignUpOK");
						btnSignUpOK.setOnAction(event->{
							dialog.close();
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
						btnSignUpOK.requestFocus();
					}catch(IOException e) {}
				});
			} 
		};
		thread.start();
	}
	
	public static void delFriend() { // 친구 삭제 성공
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(ClientController.primaryStage);
						dialog.setTitle("친구삭제 성공");
						
						Parent parent = FXMLLoader.load(getClass().getResource("/application/SignUpDialog.fxml"));
						Label signUpText = (Label)parent.lookup("#signUpText");
						signUpText.setText("친구 삭제가 완료되었습니다.");
						Button btnSignUpOK = (Button)parent.lookup("#btnSignUpOK");
						btnSignUpOK.setOnAction(event->{
							dialog.close();
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
						btnSignUpOK.requestFocus();
					}catch(IOException e) {}
				});
			} 
		};
		thread.start();
	}
	
	public static void chatRefuse(String[] str) { // 상대가 대화 거절 시
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(ClientController.primaryStage);
						dialog.setTitle("대화 거절");
						
						Parent parent = FXMLLoader.load(getClass().getResource("/application/SignUpDialog.fxml"));
						Label signUpText = (Label)parent.lookup("#signUpText");
						signUpText.setText(str[2] + "님이 대화를\n   거절하셨습니다.");
						Button btnSignUpOK = (Button)parent.lookup("#btnSignUpOK");
						btnSignUpOK.setOnAction(event->{
							dialog.close();
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
						btnSignUpOK.requestFocus();
					}catch(IOException e) {}
				});
			} 
		};
		thread.start();
	}
	
	public static void makeRoomSuccess() { // 대화 신청 수락 시
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(ClientController.primaryStage);
						dialog.setTitle("채팅방 생성");
						
						Parent parent = FXMLLoader.load(getClass().getResource("/application/SignUpDialog.fxml"));
						Label signUpText = (Label)parent.lookup("#signUpText");
						signUpText.setText("채팅방이 생성되었습니다.");
						Button btnSignUpOK = (Button)parent.lookup("#btnSignUpOK");
						btnSignUpOK.setOnAction(event->{
							dialog.close();
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
						btnSignUpOK.requestFocus();
					}catch(IOException e) {}
				});
			} 
		};
		thread.start();
	}
	
	public static void nothing() { // 대상 미접속시
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(ClientController.primaryStage);
						dialog.setTitle("상대 연결 실패");
						
						Parent parent = FXMLLoader.load(getClass().getResource("/application/SignUpDialog.fxml"));
						Label signUpText = (Label)parent.lookup("#signUpText");
						signUpText.setText("상대방이 미접속중입니다.");
						Button btnSignUpOK = (Button)parent.lookup("#btnSignUpOK");
						btnSignUpOK.setOnAction(event->{
							dialog.close();
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
						btnSignUpOK.requestFocus();
					}catch(IOException e) {}
				});
			} 
		};
		thread.start();
	}
	
	public static void groupNothing() { // 그룹방 만들 때 대상 미접속시
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(ClientController.primaryStage);
						dialog.setTitle("상대 연결 실패");
						
						Parent parent = FXMLLoader.load(getClass().getResource("/application/SignUpDialog.fxml"));
						Label signUpText = (Label)parent.lookup("#signUpText");
						signUpText.setText("미접속중인 유저가\n 포함되어있습니다.");
						Button btnSignUpOK = (Button)parent.lookup("#btnSignUpOK");
						btnSignUpOK.setOnAction(event->{
							dialog.close();
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
						btnSignUpOK.requestFocus();
					}catch(IOException e) {}
				});
			} 
		};
		thread.start();
	}
	
	public static void roomDelete(String id) { // 방에 유저가 한명만 남게 됐을 때
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(ClientController.primaryStage);
						dialog.setTitle("방 폭파");
						
						Parent parent = FXMLLoader.load(getClass().getResource("/application/SignUpDialog.fxml"));
						Label signUpText = (Label)parent.lookup("#signUpText");
						signUpText.setText(id + "님이 나가셔서\n 방이 삭제됩니다.");
						Button btnSignUpOK = (Button)parent.lookup("#btnSignUpOK");
						btnSignUpOK.setOnAction(event->{
							dialog.close();
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
						btnSignUpOK.requestFocus();
					}catch(IOException e) {}
				});
			} 
		};
		thread.start();
	}
	
	public static void inviteFail() { // 초대 대상중 미접속중인 유저가 포함된 경우
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(ClientController.primaryStage);
						dialog.setTitle("초대 실패");
						
						Parent parent = FXMLLoader.load(getClass().getResource("/application/SignUpDialog.fxml"));
						Label signUpText = (Label)parent.lookup("#signUpText");
						signUpText.setText("미접속중인 유저가 있습니다.");
						Button btnSignUpOK = (Button)parent.lookup("#btnSignUpOK");
						btnSignUpOK.setOnAction(event->{
							dialog.close();
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
						btnSignUpOK.requestFocus();
					}catch(IOException e) {}
				});
			} 
		};
		thread.start();
	}
	
	public static void loginDif() { // 로그인 시 이미 접속중인 아이디일 때
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(ClientController.primaryStage);
						dialog.setTitle("로그인 실패");
						
						Parent parent = FXMLLoader.load(getClass().getResource("/application/SignUpDialog.fxml"));
						Label signUpText = (Label)parent.lookup("#signUpText");
						signUpText.setText("이미 접속중인 아이디입니다.");
						Button btnSignUpOK = (Button)parent.lookup("#btnSignUpOK");
						btnSignUpOK.setOnAction(event->{
							dialog.close();
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
						btnSignUpOK.requestFocus();
					}catch(IOException e) {}
				});
			} 
		};
		thread.start();
	}
	
	public static void chatAlready() { // 만드려는 1:1 채팅방이 이미 존재할 시
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(ClientController.primaryStage);
						dialog.setTitle("방 만들기 실패");
						
						Parent parent = FXMLLoader.load(getClass().getResource("/application/SignUpDialog.fxml"));
						Label signUpText = (Label)parent.lookup("#signUpText");
						signUpText.setText("이미 상대방과의 1:1\n 채팅방이 존재합니다.");
						Button btnSignUpOK = (Button)parent.lookup("#btnSignUpOK");
						btnSignUpOK.setOnAction(event->{
							dialog.close();
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
						btnSignUpOK.requestFocus();
					}catch(IOException e) {}
				});
			} 
		};
		thread.start();
	}
	
	public static void serverLoginFail() { // 서버 로그인 실패
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(ServerController.primaryStage);
						dialog.setTitle("서버 로그인 실패");
						
						Parent parent = FXMLLoader.load(getClass().getResource("/application/SignUpDialog.fxml"));
						Label signUpText = (Label)parent.lookup("#signUpText");
						signUpText.setText("잘못 입력하셨습니다.");
						Button btnSignUpOK = (Button)parent.lookup("#btnSignUpOK");
						btnSignUpOK.setOnAction(event->{
							dialog.close();
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
						btnSignUpOK.requestFocus();
					}catch(IOException e) {}
				});
			} 
		};
		thread.start();
	}
	
	public static void updateTelSuccess() { // 전화번호 수정 성공
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(ClientController.primaryStage);
						dialog.setTitle("전화번호 수정 성공");
						
						Parent parent = FXMLLoader.load(getClass().getResource("/application/SignUpDialog.fxml"));
						Label signUpText = (Label)parent.lookup("#signUpText");
						signUpText.setText("전화번호가 수정되었습니다.");
						Button btnSignUpOK = (Button)parent.lookup("#btnSignUpOK");
						btnSignUpOK.setOnAction(event->{
							dialog.close();
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
						btnSignUpOK.requestFocus();
					}catch(IOException e) {}
				});
			} 
		};
		thread.start();
	}
	
	public static void updateTelFail() { // 전화번호 수정 실패
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(ClientController.primaryStage);
						dialog.setTitle("전화번호 수정 실패");
						
						Parent parent = FXMLLoader.load(getClass().getResource("/application/SignUpDialog.fxml"));
						Label signUpText = (Label)parent.lookup("#signUpText");
						signUpText.setText("전화번호 수정에 실패하였습니다.");
						Button btnSignUpOK = (Button)parent.lookup("#btnSignUpOK");
						btnSignUpOK.setOnAction(event->{
							dialog.close();
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
						btnSignUpOK.requestFocus();
					}catch(IOException e) {}
				});
			} 
		};
		thread.start();
	}
	
	public static void updateFail() { // 전화번호 수정 잘못 입력
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(ClientController.primaryStage);
						dialog.setTitle("전화번호 수정 실패");
						
						Parent parent = FXMLLoader.load(getClass().getResource("/application/SignUpDialog.fxml"));
						Label signUpText = (Label)parent.lookup("#signUpText");
						signUpText.setText("잘못 입력하셨습니다.\n 다시 입력해 주세요.");
						Button btnSignUpOK = (Button)parent.lookup("#btnSignUpOK");
						btnSignUpOK.setOnAction(event->{
							dialog.close();
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
						btnSignUpOK.requestFocus();
					}catch(IOException e) {}
				});
			} 
		};
		thread.start();
	}
}

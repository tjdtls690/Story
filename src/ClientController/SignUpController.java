package ClientController;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.StackPane;
import protocol.Protocol;

public class SignUpController implements Initializable{
	
	static Boolean checkID = false;
	
	// 회원가입 창 버튼
	@FXML private Button btnSignUp1;
	@FXML private Button btnCancel;
	@FXML private Button CheckId;
	@FXML private Label signUpText;
	
	// 회원가입 창 입력 값
	@FXML private AnchorPane signUpPane;
	@FXML private TextField textName;
	@FXML private TextField textJumin1;
	@FXML private TextField textJumin2;
	@FXML private TextField textId;
	@FXML private TextField textPass1;
	@FXML private TextField textPass2;
	@FXML private TextField textAge;
	@FXML private ComboBox<String> comboTel;
	@FXML private TextField textTel1;
	@FXML private TextField textTel2;
	@FXML private RadioButton btnMan;
	@FXML private RadioButton btnWoman;
	
	
	@Override
	public void initialize(URL location, ResourceBundle resources) { // 이벤트 걸기
		Platform.runLater(()->{
			btnCancel.setOnAction(event->handleBtnCancel(event));	// 취소 버튼 이벤트
			btnSignUp1.setOnAction(event->handleBtnSignUp1(event));	// 회원가입 버튼 이벤트
			comboTel.setValue("010");								// 전화번호 1번째 선택 값 처음 값으로 셋팅
			CheckId.setOnAction(event->handleBtnCheckId(event));	// 아이디 중복 확인 버튼 이벤트
			textName.setOnKeyPressed(new EventHandler<KeyEvent> () {// 이 밑으론 사용자 편의를 위한 requestFocus 이벤트들
			    @Override
			     public void handle(KeyEvent event) {
			    	if (event.getCode().equals(KeyCode.ENTER)) textName();
			     }
			});
			textJumin1.setOnKeyReleased(new EventHandler<KeyEvent> () {
			    @Override
			     public void handle(KeyEvent event) {
			    	if (textJumin1.getText().trim().length() == 6) textJumin1();
			     }
			});
			textJumin2.setOnKeyReleased(new EventHandler<KeyEvent> () {
			    @Override
			     public void handle(KeyEvent event) {
			    	if (textJumin2.getText().trim().length() == 7) textJumin2();
			     }
			});
			textPass1.setOnKeyPressed(new EventHandler<KeyEvent> () {
			    @Override
			     public void handle(KeyEvent event) {
			    	if (event.getCode().equals(KeyCode.ENTER)) textPass1();
			     }
			});
			textPass2.setOnKeyPressed(new EventHandler<KeyEvent> () {
			    @Override
			     public void handle(KeyEvent event) {
			    	if (event.getCode().equals(KeyCode.ENTER)) textPass2();
			     }
			});
			comboTel.valueProperty().addListener(new ChangeListener<String>() {
		        @Override
		      	 public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
		        	textTel1.requestFocus();
		      	 }
		    });
			textTel1.setOnKeyPressed(new EventHandler<KeyEvent> () {
			    @Override
			     public void handle(KeyEvent event) {
			    	if (event.getCode().equals(KeyCode.ENTER)) textTel1();
			     }
			});
			
		});
	}
	
	public void textName() { // 이름 필드 엔터키 이벤트
		textJumin1.requestFocus();
	}
	
	public void textJumin1() { // 주민번호 이벤트 처리
		textJumin2.requestFocus();
	}
	
	public void textJumin2() { // 주민번호 이벤트 처리
		textId.requestFocus();
	}
	
	public void textPass1() { // 비밀번호 필드 엔터키 이벤트
		textPass2.requestFocus();
	}
	
	public void textPass2() { // 비밀번호 필드 엔터키 이벤트
		textAge.requestFocus();
	}
	
	public void textTel1() { // 전화번호 필드 엔터키 이벤트
		textTel2.requestFocus();
	}
	
	public void handleBtnCheckId(ActionEvent event) { // 회원가입 창 ID 중복확인 버튼
		String id = textId.getText().trim();
		String data = Protocol.SIGNUP_CHECKID + ":;:" + id;
		ClientController.send(data);
	}
	
	public void handleBtnCancel(ActionEvent event) { // 회원가입 창 취소버튼
		try {
			StackPane root = (StackPane) btnCancel.getScene().getRoot();
			root.getChildren().remove(signUpPane);
			SignUpController.checkID = false;
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void signUpOK(Parent parent) { // 회원가입 성공 후 로그인 창으로 전환
		try {
			Button btnCancel = (Button)parent.lookup("#btnCancel");
			StackPane root = (StackPane) btnCancel.getScene().getRoot();
			AnchorPane signUpPane = (AnchorPane)parent.lookup("#signUpPane");
			root.getChildren().remove(signUpPane);
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public void handleBtnSignUp1(ActionEvent event) { // 회원가입 창 회원가입 버튼
		
		if(textName.getText().trim().length() == 0) {	// 이름 안적을 시
			SignUpDialogController.writeName();
			return;
		}else if(textJumin1.getText().trim().length() == 0 || textJumin2.getText().trim().length() == 0) { // 주민번호 안적을 시
			SignUpDialogController.writeJumin();
			return;
		}else if(textId.getText().trim().length() == 0) { // 아이디 안적을 시
			SignUpDialogController.writeId();
			return;
		}else if(textPass1.getText().trim().length() == 0 || textPass2.getText().trim().length() == 0) { // 비밀번호 안적을 시
			SignUpDialogController.writePw();
			return;
		}else if(textAge.getText().trim().length() == 0) { // 나이 안적을 시
			SignUpDialogController.writeAge();
			return;
		}else if(textTel1.getText().trim().length() == 0 || textTel2.getText().trim().length() == 0) { // 전화번호 안적을 시
			SignUpDialogController.writeTel();
			return;
		}else if((textPass1.getText().trim().equals(textPass2.getText().trim())) == false) { // 비밀번호 확인 안맞을 시
			SignUpDialogController.password();
			return;
		}else if(checkID == false) { // 아이디 중복확인 안했을 시
			SignUpDialogController.checkId();
			return;
		}
		String name = textName.getText().trim();
		long jumin = Long.parseLong(textJumin1.getText().trim() + textJumin2.getText().trim());
		String id = textId.getText().trim();
		String password = textPass1.getText().trim();
		int age = Integer.parseInt(textAge.getText().trim());	// 텍스트 말고는 getValue() 쓴다.
		Boolean gender = btnMan.isSelected(); // 체크 여부 확인
		String tel = "0" + Long.parseLong(comboTel.getValue().trim() + textTel1.getText().trim() + textTel2.getText().trim());
		String data = Protocol.SIGNUP + ":;:" + name + ":;:" + jumin + ":;:" + id + ":;:" + password + ":;:" + age + ":;:" + gender + ":;:" + tel;
		ClientController.send(data);
	}

}

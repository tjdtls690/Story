package server.controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class ServerMainController implements Initializable{
	
	@FXML private ListView<String> allUserList;
	static ObservableList<String> allUserListItem = FXCollections.observableArrayList();
	@FXML private Button btnClose;
	@FXML private ListView<String> connectingUserList;
	static ObservableList<String> connectingUserListItem = FXCollections.observableArrayList();
	@FXML private TextArea loginLogoutText;
	@FXML private TextArea requestLog;
	
	static Parent parent; 
	static Stage primaryStage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		primaryStage.setOnCloseRequest(event->handleXButton(event)); 	// X 버튼 이벤트
		btnClose.setOnAction(event->handleBtnClose(event));				// 서버 셧다운 버튼 이벤트
	}
	
	public static void setRequestLog(String log) { // 자잘한 요청사항들 로그
		Platform.runLater(()->{
			TextArea requestLog = (TextArea)parent.lookup("#requestLog");
			requestLog.setText(requestLog.getText() + log + "\n");
			requestLog.positionCaret(requestLog.getText().length());
		});
	}
	
	public static void setLoginLogoutText(String log) { // 로그인, 로그아웃 로그 표시
		Platform.runLater(()->{
			TextArea loginLogoutText = (TextArea)parent.lookup("#loginLogoutText");
			loginLogoutText.setText(loginLogoutText.getText() + log + "\n");
			loginLogoutText.positionCaret(loginLogoutText.getText().length());
		});
	}
	
	@SuppressWarnings("unchecked")
	public static void allUserChangeSet(String allUser) { // 유저가 회원가입, 탈퇴 시 모든유저리스트 갱신
		Platform.runLater(()->{
			ListView<String> allUserList = (ListView<String>)parent.lookup("#allUserList");
			ObservableList<String> item = FXCollections.observableArrayList();
			String[] str = allUser.split(":;:");
			for(int i = 0; i < str.length; i++) item.add(str[i]);
			
			allUserList.setItems(item);
			allUserListItem = item;
		});
	}
	
	@SuppressWarnings("unchecked")
	public static void connectingUserSet(String connectingId) { // 접속중인 유저 셋팅
		Platform.runLater(()->{
			ListView<String> connectingUserList = (ListView<String>)parent.lookup("#connectingUserList");
			ObservableList<String> item = FXCollections.observableArrayList();
			String[] str = connectingId.split(":;:");
			for(int i = 0; i < str.length; i++) item.add(str[i]);
			
			connectingUserList.setItems(item);
			connectingUserListItem = item;
		});
	}
	
	@SuppressWarnings("unchecked")
	public static void allUserSet(String allUser) { // 로그인 시 모든 유저 리스트 셋팅
		Platform.runLater(()->{
			ListView<String> allUserList = (ListView<String>)parent.lookup("#allUserList");
			ObservableList<String> item = FXCollections.observableArrayList();
			String[] str = allUser.split(":;:");
			for(int i = 0; i < str.length; i++) item.add(str[i]);
			
			allUserList.setItems(item);
			allUserListItem = item;
			ServerController.startServer();
		});
	}
	
	public void handleXButton(WindowEvent event) { // 우측 상단 X 버튼
		System.out.println("X 버튼 클릭");
		System.out.println("서버 종료");
		ServerController.stopServer();
	}
	
	public void handleBtnClose(ActionEvent event) {
		ServerController.stopServer();
	}

}

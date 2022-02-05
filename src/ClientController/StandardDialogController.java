package ClientController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.fxml.FXML;
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

public class StandardDialogController implements Initializable{
	
	@FXML private Label setLabel;
	@FXML private Button btnOK;
	@FXML private Button btnCancel;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		
	}
	
	public static void delFriend(String friend) { // 친구 삭제 확인 다이알로그
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(ClientController.primaryStage);
						dialog.setTitle("친구 삭제");
						
						Parent parent = FXMLLoader.load(getClass().getResource("/application/StandardDialog.fxml"));
						Label setLabel = (Label)parent.lookup("#setLabel");
						setLabel.setText("정말로 삭제하시겠습니까??");
						Button btnOK = (Button)parent.lookup("#btnOK");
						btnOK.setOnAction(event->{
							dialog.close();
							MainGUIController.delFriend();
						});
						Button btnCancel = (Button)parent.lookup("#btnCancel");
						btnCancel.setOnAction(event->{
							dialog.close();
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
					}catch(IOException e) {
						e.printStackTrace();
					}
				});
			} 
		};
		thread.start();
	}
	
	public static void chatQuestion(String[] str) { // 대화 신청 확인 다이알로그
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(ClientController.primaryStage);
						dialog.setTitle("대화 신청");
						
						Parent parent = FXMLLoader.load(getClass().getResource("/application/StandardDialog.fxml"));
						Label setLabel = (Label)parent.lookup("#setLabel");
						setLabel.setText(str[1] + "님의 대화신청을\n   수락하시겠습니까??");
						Button btnOK = (Button)parent.lookup("#btnOK");
						btnOK.setOnAction(event->{ 
							dialog.close();
							ClientController.send(Protocol.CHAT_ACCEPT + ":;:" + str[1] + ":;:" + str[2]);
						});
						Button btnCancel = (Button)parent.lookup("#btnCancel");
						btnCancel.setOnAction(event->{
							dialog.close();
							ClientController.send(Protocol.CHAT_REFUSE + ":;:" + str[1] + ":;:" + str[2]);
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
					}catch(IOException e) {
						e.printStackTrace();
					}
				});
			} 
		};
		thread.start();
	}
	
	public static void roomExit() { // 방 나가기 확인
		Thread thread = new Thread() {
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						dialog.initOwner(ClientController.primaryStage);
						dialog.setTitle("방 나가기");
						
						Parent parent = FXMLLoader.load(getClass().getResource("/application/StandardDialog.fxml"));
						Label setLabel = (Label)parent.lookup("#setLabel");
						setLabel.setText("정말로 방을 나가시겠습니까??");
						Button btnOK = (Button)parent.lookup("#btnOK");
						btnOK.setOnAction(event->{ 
							dialog.close();
							MainGUIController.roomExit();
						});
						Button btnCancel = (Button)parent.lookup("#btnCancel");
						btnCancel.setOnAction(event->{
							dialog.close();
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
					}catch(IOException e) {
						e.printStackTrace();
					}
				});
			} 
		};
		thread.start();
	}
}

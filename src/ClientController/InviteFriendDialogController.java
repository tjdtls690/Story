package ClientController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import protocol.Protocol;

public class InviteFriendDialogController implements Initializable{
	
	@FXML private ListView<String> inviteList;
	@FXML private Button btnCancel;
	@FXML private Button btnInvite;
	static ObservableList<String> inviteListItem = FXCollections.observableArrayList();
	
	static int inviteListIndex;
	static String friends;
	static Parent parent;
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		inviteList.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		inviteList.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> ov, String old_val, String new_val) -> {
			inviteListIndex = inviteList.getSelectionModel().getSelectedIndex();
			ObservableList<String> selectedItems = inviteList.getSelectionModel().getSelectedItems();
		    StringBuilder builder = new StringBuilder();
		    for (String name : selectedItems) {
		    	builder.append(name + ":;:");
		    }
		    friends = builder.toString();
		    System.out.println(friends);
	    });
	}
	
	public static void inviteFriend(int roomListIndex, String[] old, String[] str) { // 친구초대 다이알로그
		Thread thread = new Thread() {
			@SuppressWarnings("unchecked")
			public void run() {
				Platform.runLater(()->{
					try {
						Parent parent = FXMLLoader.load(getClass().getResource("/application/InviteFriendDialog.fxml"));
						InviteFriendDialogController.parent = parent;
						ListView<String> inviteList = (ListView<String>)parent.lookup("#inviteList");
						ObservableList<String> inviteListItem = FXCollections.observableArrayList();
						for(int i = 0; i < str.length; i++) {
							int check = 0;
							for(int j = 0; j < old.length; j++) {
								if(str[i].equals(old[j])) {
									check++;
									break;
								}
							}
							if(check == 1) continue;
							inviteListItem.add(str[i]);
						}
						InviteFriendDialogController.inviteListItem = inviteListItem;
						inviteList.setItems(inviteListItem);
						
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						
						dialog.initOwner(ClientController.primaryStage);
						dialog.setTitle("친구 초대");
						
						Button btnInvite = (Button)parent.lookup("#btnInvite");
						btnInvite.setOnAction(event->{
							dialog.close();
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
							String oldf = "";
							for(int i = 0; i < old.length; i++) {
								oldf += old[i] + ":;:";
							}
							int oldsu = old.length;
							MainGUIController.inviteOwnSecond(roomListIndex, friends);
							ClientController.send(Protocol.INVITE_FRIEND + ":;:" + roomListIndex + ":;:" + oldsu + ":;:" + ClientController.mv.getId() + ":;:" + oldf + friends);
						});
						Button btnCancel = (Button)parent.lookup("#btnCancel");
						btnCancel.setOnAction(event->{
							dialog.close();
						});
						Scene scene = new Scene(parent);
						dialog.setScene(scene);
						dialog.setResizable(false);
						dialog.show();
						AddFriendDialogController.primaryStage = dialog;
					}catch(IOException e) {}
				});
			} 
		};
		thread.start();
	}

	
}

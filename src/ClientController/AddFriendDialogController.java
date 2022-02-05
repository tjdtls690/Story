package ClientController;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import application.objectVO.MemberVO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import protocol.Protocol;

public class AddFriendDialogController implements Initializable{
	
	static MemberVO mv;
	
	static Stage primaryStage;
	static Parent parent;
	String selectionStr;
	static int searchFriendListIndex;
	
	@FXML private Button btnSearch;
	@FXML private Button btnAddFriend;
	@FXML private Button btnCancel;
	
	@FXML private TextField searchFriend;
	@FXML private static ListView<String> searchFriendList;
	@FXML private static ObservableList<String> item;
	static String msg;
	
	
	@SuppressWarnings("unchecked")
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		Platform.runLater(()->{
			btnSearch.setOnAction(event->handleBtnSearch(event));	// 검색 버튼
			ListView<String> searchFriendList = (ListView<String>)parent.lookup("#searchFriendList");
			searchFriendList.setOnMouseClicked((MouseEvent)->{	// 친구 추가 목록 중 하나 선택 시 아이디를 필드에 저장
				searchFriendListIndex = searchFriendList.getSelectionModel().getSelectedIndex();
				if(searchFriendListIndex != -1) {
					Object obj = searchFriendList.getSelectionModel().getSelectedItem();
					msg = obj.toString();
				}
			});
			
			searchFriend.setOnKeyPressed(new EventHandler<KeyEvent> () {	// 친구 검색 텍스트필드 엔터키 이벤트
			    @Override
			      public void handle(KeyEvent event) {
			    	if (event.getCode().equals(KeyCode.ENTER)) searchFriendEnterKey();
			      }
			});
		});
	}
	
	public void searchFriendEnterKey() {	// 친구 검색 텍스트필드 엔터키 누를 시
		if(searchFriend.getText().trim().length() == 0) return;
		String str = Protocol.SEARCH_FRIEND + ":;:" + searchFriend.getText().trim();
		ClientController.send(str);
	}
	
	public static void addFriend() { // 친구 검색 창 띄우기
		Thread thread = new Thread() {
			@SuppressWarnings("unchecked")
			public void run() {
				Platform.runLater(()->{
					try {
						Stage dialog = new Stage(StageStyle.UTILITY);
						dialog.initModality(Modality.WINDOW_MODAL);
						
						dialog.initOwner(ClientController.primaryStage);
						dialog.setTitle("친구 아이디 검색");
						
						Parent parent = FXMLLoader.load(getClass().getResource("/application/AddFriendDialog.fxml"));
						AddFriendDialogController.parent = parent;
						Button btnAddFriend = (Button)parent.lookup("#btnAddFriend");
						btnAddFriend.setOnAction(event->{								// 친구 추가 버튼 누를 시
							ListView<String> searchFriendList = (ListView<String>)parent.lookup("#searchFriendList");
							if(searchFriendListIndex == -1 || searchFriendList.getItems().size() == 0) return;
							if(MainGUIController.checkFriend(msg) == false) {
								SignUpDialogController.addFriendFail();				// 이미 친구가 있을 시
								return;
							}else {
								ClientController.send(Protocol.ADD_FRIEND + ":;:" + msg);	// 친구 추가 정상 진행 시
							}
						});
						Button btnCancel = (Button)parent.lookup("#btnCancel");
						btnCancel.setOnAction(event->{	// 취소 버튼
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
	
	public void handleBtnSearch(ActionEvent event) { // 친구 검색 버튼
		if(searchFriend.getText().trim().length() == 0) return;
		String str = Protocol.SEARCH_FRIEND + ":;:" + searchFriend.getText().trim();
		ClientController.send(str);
	}
	
	public static void addFriendList(String[] str) { // 친구 검색 목록에 검색결과 출력
		Platform.runLater(()->{
			@SuppressWarnings("unchecked")
			ListView<String> searchFriendList = (ListView<String>)parent.lookup("#searchFriendList");
			ObservableList<String> item = FXCollections.observableArrayList();
			for(int i = 1; i < str.length; i++) {
				if(ClientController.mv.getId().equals(str[i])) continue;
				item.add(str[i]);
			}
			searchFriendList.setItems(item);
			setAddItem(item);
		});
	}
	
	public static void setAddItem(ObservableList<String> item) {
		AddFriendDialogController.item = item;
	}
	
	public static void setVO(MemberVO mv) {
		AddFriendDialogController.mv = mv;
	}
	
	public static void addFriendSuccess() {// 친구 추가 성공 다이알로그 
		SignUpDialogController.addFriendSuccess();
	}
}

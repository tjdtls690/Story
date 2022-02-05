import ClientController.ClientController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// 클라이언트 시작 클래스
public class Index extends Application{
	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/Login.fxml"));
			Parent login = loader.load();
			ClientController cl = loader.getController();
			cl.setPrimaryStage(primaryStage);
			ClientController.startClient(); // 클라이언트 통신 연결 요청
			Scene scene = new Scene(login);
			scene.getStylesheets().add(getClass().getResource("/application/application.css").toString());
			
			primaryStage.setResizable(false);
			primaryStage.setTitle("Client");
			primaryStage.setScene(scene);
			primaryStage.show();
			ClientController.parent = login;
			
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		launch(args);
		
	}
}

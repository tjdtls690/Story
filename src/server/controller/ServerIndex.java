package server.controller;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class ServerIndex extends Application{

	@Override
	public void start(Stage primaryStage) throws Exception {
		try {
			FXMLLoader loader = new FXMLLoader(getClass().getResource("/application/ServerLogin.fxml"));
			Parent login = loader.load();
			ServerController sc = loader.getController();
			sc.setPrimaryStage(primaryStage);
			Scene scene = new Scene(login);
			
			primaryStage.setResizable(false);
			primaryStage.setTitle("Server");
			primaryStage.setScene(scene);
			primaryStage.show();
			ServerController.parent = login;
			ServerController.primaryStage = primaryStage;
			
		}catch(Exception e) {
			e.printStackTrace();
		}
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}

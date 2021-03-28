package controller;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import util.PasswordSkin;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class Main extends Application {
	
	public static Stage stage = null;
	
	public static String login_id = "";
	public static String login_pass = "";
	public static String login_name = "";
	
	public static Boolean hiworks_login_stat = false;
	
	public static ScheduledExecutorService hiworkd_service = Executors.newSingleThreadScheduledExecutor();
	
	@Override
	public void start(Stage primaryStage) throws IOException {
		Parent root = FXMLLoader.load(getClass().getResource("/views/Login.fxml"));
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		scene.getStylesheets().add(getClass().getResource("/css/Login.css").toExternalForm());
		
		JFXPasswordField password = (JFXPasswordField) scene.lookup("#login_pass");
		password.setSkin(new PasswordSkin(password));
		password.setFocusTraversable(false);
		
		JFXTextField id = (JFXTextField) scene.lookup("#login_id");
		id.setFocusTraversable(false);
		
		this.stage = primaryStage;
		primaryStage.initStyle(StageStyle.UNDECORATED);
		primaryStage.setTitle("로그인");
		primaryStage.show();
	}
	
	@Override
	public void stop(){
		Main.hiworkd_service.shutdown();
		try {
			HiworksApi.HiworksLogout();
		} catch (Exception e) {
			e.printStackTrace();
		}
	    System.out.println("Stage is closing");
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	private static Main instance = null;
	public static synchronized Main getInstance(){
		if(null == instance){
			instance = new Main();
		}
		return instance;
	}
}

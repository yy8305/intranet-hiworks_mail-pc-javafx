package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import util.PasswordSkin;

public class MainController implements Initializable {
	
    @FXML
    private HBox main_parent;

    private double xOffset = 0;
    private double yOffset = 0;
	
	@FXML
	private Pane paneContent;
	
	public static Pane static_paneContent;
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
		static_paneContent = paneContent;
		makeStageDrageable();
    }
	
	@FXML
	private void open_homepage(ActionEvent event) throws IOException {
		Parent fxml = FXMLLoader.load(getClass().getResource("/views/Main_homepage.fxml"));
		paneContent.getChildren().removeAll();
		paneContent.getChildren().setAll(fxml);
	}
	
	@FXML
	private void open_hiworks(ActionEvent event) throws IOException {
		if(Main.hiworks_login_stat == false) {
			Parent fxml = FXMLLoader.load(getClass().getResource("/views/Main_hiworks.fxml"));
			paneContent.getChildren().removeAll();
			paneContent.getChildren().setAll(fxml);
			
			JFXPasswordField password = (JFXPasswordField) paneContent.lookup("#hiworks_pass");
			password.setSkin(new PasswordSkin(password));
			password.setFocusTraversable(false);
			
			JFXTextField id = (JFXTextField) paneContent.lookup("#hiworks_id");
			id.setFocusTraversable(false);
		}else {
			Parent fxml = FXMLLoader.load(getClass().getResource("/views/Main_hiworks_logout.fxml"));
			paneContent.getChildren().removeAll();
			paneContent.getChildren().setAll(fxml);
		}
	}
	
	@FXML
	private void open_chatting(ActionEvent event) throws IOException {
		Parent fxml = FXMLLoader.load(getClass().getResource("/views/Main_chatting.fxml"));
		paneContent.getChildren().removeAll();
		paneContent.getChildren().setAll(fxml);
	}
	
	@FXML
	private void open_setting(ActionEvent event) throws IOException {
		Parent fxml = FXMLLoader.load(getClass().getResource("/views/Main_setting.fxml"));
		paneContent.getChildren().removeAll();
		paneContent.getChildren().setAll(fxml);
	}
	
	private void makeStageDrageable() {
		main_parent.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
		main_parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	Controller.main_stage.setX(event.getScreenX() - xOffset);
            	Controller.main_stage.setY(event.getScreenY() - yOffset);
            	Controller.main_stage.setOpacity(0.7f);
            }
        });
		main_parent.setOnDragDone((e) -> {
			Controller.main_stage.setOpacity(1.0f);
        });
		main_parent.setOnMouseReleased((e) -> {
			Controller.main_stage.setOpacity(1.0f);
        });

    }
}

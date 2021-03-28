package controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Controller implements Initializable {
    
	public static Stage main_stage = null;
	
	@FXML
    private Pane pane;
    @FXML
    private AnchorPane parent;

    private double xOffset = 0;
    private double yOffset = 0;
    
    
	@FXML
	private JFXTextField login_id;
	
	@FXML
	private JFXPasswordField login_pass;
	
	@FXML
	private JFXButton login_btn;
	
	@FXML
	private Label login_status;
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
        makeStageDrageable();
        login_id.setOnKeyPressed(new EventHandler<KeyEvent> () {
	        @Override
	          public void handle(KeyEvent event) {
	        	if (event.getCode() == KeyCode.TAB)  {
	        		login_pass.setFocusTraversable(true);
	        	}else if (event.getCode() == KeyCode.ENTER)  {
	        		login_btn.fire();
	        	}
	          }
	    });
        
        login_pass.setOnKeyPressed(new EventHandler<KeyEvent> () {
	        @Override
	          public void handle(KeyEvent event) {
	        	if (event.getCode() == KeyCode.TAB)  {
	        		login_id.setFocusTraversable(true);
	        	}else if (event.getCode() == KeyCode.ENTER)  {
	        		login_btn.fire();
	        	}
	          }
	    });
    }
	
	@FXML
	private void close_app(MouseEvent event) {
		System.exit(0);
	}
	
	@FXML
	private void minimize_app(MouseEvent event) {
		Stage stage = (Stage) ((Node)event.getSource()).getScene().getWindow();
		stage.setIconified(true);
	}
	
	@FXML
	private void loginProc(ActionEvent event) {
		String string_id = login_id.getText(); /* 입력 데이터 String으로 변환 */
		String string_pass = login_pass.getText(); /* 입력 데이터 String으로 변환 */
		
		Connection connection = null;
        Statement st = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://<db server host>/<db name>?useSSL=false" , "<db username>", "<db password>");
            st = connection.createStatement();
 
            String sql;
            sql = "select * FROM user where USER_ID = '"+string_id+"' and USER_PASS = PASSWORD('"+string_pass+"') limit 1;";
 
            ResultSet rs = st.executeQuery(sql);
            
			if(rs.next()) {
				Main.login_id = string_id;
				Main.login_pass = string_pass;
				Main.login_name = rs.getString("NAME");
				
				((Node) (event.getSource())).getScene().getWindow().hide();
                Parent root = FXMLLoader.load(getClass().getResource("/views/Main.fxml"));
                Stage primaryStage = new Stage();
                Scene scene = new Scene(root);
                scene.getStylesheets().add(getClass().getResource("/css/Main.css").toExternalForm());
                
                JFXButton btn_homepage = (JFXButton) scene.lookup("#btn_homepage");
                btn_homepage.setFocusTraversable(false);
                JFXButton btn_hiworks = (JFXButton) scene.lookup("#btn_hiworks");
                btn_hiworks.setFocusTraversable(false);
                JFXButton btn_chatting = (JFXButton) scene.lookup("#btn_chatting");
                btn_chatting.setFocusTraversable(false);
                JFXButton btn_setting = (JFXButton) scene.lookup("#btn_setting");
                btn_setting.setFocusTraversable(false);
                
                this.main_stage = primaryStage;
                primaryStage.setResizable(false);
                /*primaryStage.initStyle(StageStyle.UNDECORATED);*/
                primaryStage.setScene(scene);
                primaryStage.setTitle("HONG ERP");
                primaryStage.show();
			}else{
            	login_status.setText("로그인 실패 했습니다. 아이디/패스워드를 확인해주세요.");
            	JOptionPane.showMessageDialog(null, "로그인 실패 했습니다.\n아이디/패스워드를 확인해주세요.");
			}
            /*while (rs.next()) {
                String sqlRecipeProcess = "";
                sqlRecipeProcess = rs.getString("USER_ID");
                System.out.println(sqlRecipeProcess);
            }*/
 
            rs.close();
            st.close();
            connection.close();
        } catch (SQLException se1) {
            se1.printStackTrace();
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            try {
                if (st != null)
                    st.close();
            } catch (SQLException se2) {
            }
            try {
                if (connection != null)
                    connection.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }
	}
	
	private void makeStageDrageable() {
        parent.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX();
                yOffset = event.getSceneY();
            }
        });
        parent.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	Main.stage.setX(event.getScreenX() - xOffset);
            	Main.stage.setY(event.getScreenY() - yOffset);
            	Main.stage.setOpacity(0.7f);
            }
        });
        parent.setOnDragDone((e) -> {
        	Main.stage.setOpacity(1.0f);
        });
        parent.setOnMouseReleased((e) -> {
        	Main.stage.setOpacity(1.0f);
        });

    }
	
}

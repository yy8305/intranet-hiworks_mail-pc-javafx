package controller;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import javax.swing.JOptionPane;

import org.controlsfx.control.Notifications;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.util.Duration;

public class HiworksController implements Initializable {
	@FXML
	private JFXTextField hiworks_id;
	
	@FXML
	private JFXPasswordField hiworks_pass;
	
	@FXML
	private JFXButton hiworks_btn;
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
		hiworks_id.setOnKeyPressed(new EventHandler<KeyEvent> () {
	        @Override
	          public void handle(KeyEvent event) {
	        	if (event.getCode() == KeyCode.TAB)  {
	        		hiworks_pass.setFocusTraversable(true);
	        	}else if (event.getCode() == KeyCode.ENTER)  {
	        		hiworks_btn.fire();
	        	}
	          }
	    });
        
		hiworks_pass.setOnKeyPressed(new EventHandler<KeyEvent> () {
	        @Override
	          public void handle(KeyEvent event) {
	        	if (event.getCode() == KeyCode.TAB)  {
	        		hiworks_id.setFocusTraversable(true);
	        	}else if (event.getCode() == KeyCode.ENTER)  {
	        		hiworks_btn.fire();
	        	}
	          }
	    });
    }
	
	@FXML
	private void startHiworks(ActionEvent event) throws IOException {
		String string_id = hiworks_id.getText();
		String string_pass = hiworks_pass.getText();
		
		HiworksApi hiworks = new HiworksApi(string_id,string_pass);
		if( hiworks.login_html.indexOf("alert(\"아이디 또는 비밀번호를 확인해주세요.\")") != -1 ) {
        	System.out.println("============Hiworks login false============");
        	JOptionPane.showMessageDialog(null, "hiworks 로그인 실패 했습니다.\n아이디/패스워드를 확인해주세요.");
        	return;
        }else if( hiworks.main_html.indexOf("alert(\"보안을 위하여 다시 로그인해 주시기 바랍니다.\")") != -1 ) {
        	System.out.println("============Hiworks re login============");
        	Main.hiworks_login_stat = false;
        	hiworks.HiworksLogin(string_id, string_pass);
        }
		
		Main.hiworks_login_stat = true;
		Parent fxml = FXMLLoader.load(getClass().getResource("/views/Main_hiworks_logout.fxml"));
		MainController.static_paneContent.getChildren().removeAll();
		MainController.static_paneContent.getChildren().setAll(fxml);
		
		int mailcnt_result = hiworks.WebmailCount();
		if(mailcnt_result != 1) {
			hiworks.mail_cnt = hiworks.new_mail_cnt;
			
			Runnable hiworks_runnable = new Runnable() {
				public void run() {
					System.out.println("====================");
					try {
						System.out.println("hiworks_login_stat = "+ Main.hiworks_login_stat);
						if(Main.hiworks_login_stat == true && SettingController.hiworks_setting_stat == true) {
							int mailcnt_result = hiworks.WebmailCount();
							System.out.println("mailcnt_result = "+mailcnt_result);
							if(mailcnt_result != 1) {
								if(hiworks.mail_cnt > hiworks.new_mail_cnt) {
									hiworks.mail_cnt = hiworks.new_mail_cnt;
								}
								if(hiworks.mail_cnt != hiworks.new_mail_cnt) {
									/*swing 팝업 (문제점: 최상위로 안뜸)
									 * JOptionPane.showMessageDialog(null, "메일이 도착했습니다.");*/


									/*alert 팝업(문제점: 최상위로 안뜸)
									 * Alert alert = new Alert(AlertType.INFORMATION);
									alert.setTitle("Information Dialog");
									alert.setHeaderText(null);
									alert.setContentText("메일이 도착했습니다.");
									alert.showAndWait();
									
									controller.Controller.main_stage.setAlwaysOnTop(true);
									controller.Controller.main_stage.toFront();*/


									Platform.runLater(() ->{
										Notifications notificationBuilder = Notifications.create()
											.title("메일")
											.text("메일이 도착했습니다.")
											.graphic(null)
											.hideAfter(Duration.seconds(5))
											.position(Pos.BOTTOM_RIGHT);
										notificationBuilder.showConfirm();
									});
									
									hiworks.mail_cnt = hiworks.new_mail_cnt;
								}
								System.out.println(hiworks.mail_cnt);
								System.out.println(hiworks.new_mail_cnt);
							}else{
								Main.hiworks_login_stat = false;
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			
			Main.hiworkd_service.scheduleAtFixedRate(hiworks_runnable, 0, 10, TimeUnit.SECONDS);
		}else {
			Main.hiworks_login_stat = false;
		}
	}
}

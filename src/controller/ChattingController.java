package controller;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ResourceBundle;

import javax.swing.JOptionPane;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.Stage;

public class ChattingController implements Initializable, Runnable {
	private Socket socket;
    private String msg;
    private BufferedReader br;
    private PrintWriter pw;
    private String name = Main.login_name;
    
    @FXML
	private TextArea chatlog;
    
    @FXML
    private JFXTextField chat_input;
    
	@Override
    public void initialize(URL url, ResourceBundle rb) {
		chatlog.setEditable(false);
		
		connect();
		
		initMessage();
		
		chat_input.setOnKeyPressed(new EventHandler<KeyEvent> () {
	        @Override
	          public void handle(KeyEvent event) {
	        	if (event.getCode() == KeyCode.ENTER)  {
	        		sendMessage(chat_input.getText());
	        	}
	          }
	    });
	}
	
	@FXML
	private void onChatBtn(ActionEvent event) throws IOException {
		sendMessage(chat_input.getText());
	}
	
	public void connect(){
        try {
	        socket = new Socket("127.0.0.1", 9999);
	        System.out.println("서버에 연결됨");
	        
	        br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
	        pw = new PrintWriter(socket.getOutputStream(), true);
	        
	        //접속하자마자 닉네임 전송하면, 서버가 닉네임으로 인식
	        pw.println(name);
	        
	        new Thread(this).start();
        } catch (IOException e) {
            e.printStackTrace();
            chatlog.appendText("====================[채팅서버 접속 불가]=======================");
        }
    }
    
    public void sendMessage(String msg){
        pw.println(msg);
        chat_input.setText("");
        saveMessage(msg);
    }
    
    public void saveMessage(String msg){
    	Connection connection = null;
        Statement st = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://<db server host>/<db name>?useSSL=false" , "<db username>", "<db password>");
            st = connection.createStatement();
 
            String sql;
            sql = "select count(*) as cnt FROM chat;";
 
            ResultSet rs = st.executeQuery(sql);
            
			if(rs.next()) {
				if(rs.getInt("cnt") > 100) {
					sql = "DELETE FROM chat WHERE CHAT_ID <= (SELECT CHAT_ID FROM (SELECT CHAT_ID FROM chat ORDER BY CHAT_ID DESC LIMIT 1 OFFSET 100) foo)";
					st.executeUpdate(sql);
				}
			}
			sql = "insert into chat (NAME,CONT) values ('"+name+"','"+msg+"')";
			st.executeUpdate(sql);
 
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
    
    public void initMessage(){
    	Connection connection = null;
        Statement st = null;
        try {
            Class.forName("com.mysql.jdbc.Driver");
            connection = DriverManager.getConnection("jdbc:mysql://<db server host>/<db name>?useSSL=false" , "<db username>", "<db password>");
            st = connection.createStatement();
 
            String sql;
            sql = "select * FROM chat order by CHAT_ID asc;";
 
            ResultSet rs = st.executeQuery(sql);
			
            while (rs.next()) {
                chatlog.appendText("[ " + rs.getString("NAME") + " ] : " + rs.getString("CONT") + "\n" );
            }
 
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
    
    @Override
    public void run() {
        try {
        	while((msg = br.readLine()) != null){
                chatlog.appendText(msg+"\n");
                //textArea박스의 스크롤바의 위치를 입력된 Text길이 만큼 내리기
            }
        } catch (Exception e) {
            System.out.println(e + "--> Client run fail");
        }
    }
}

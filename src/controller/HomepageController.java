package controller;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.Worker;
import javafx.concurrent.Worker.State;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

public class HomepageController implements Initializable {
	
	@FXML
	private WebView web_view;
	private WebEngine engine;
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
	    final WebEngine webengine = web_view.getEngine();
	    webengine.getLoadWorker().stateProperty().addListener(
	    new ChangeListener<State>() {
	        public void changed(ObservableValue ov, State oldState, State newState) {
	            if (newState == State.SUCCEEDED) {
	            	try {
	            		if(webengine.getLocation().toString().equals("http://<intranet url>/login")) {
	            			webengine.executeScript("document.getElementById('id').value = '"+ Main.login_id+"';");
			                webengine.executeScript("document.getElementById('pw').value = '"+ Main.login_pass+"';");
			                webengine.executeScript("document.forms['login'].submit()");
	            		}
	            	}catch(Exception e) {
	            		// TODO
	            	}
	            }
	        }
	    });
	    webengine.load("http://<intranet url>");
    }
}


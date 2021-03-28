package controller;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXToggleButton;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;

public class SettingController implements Initializable {
	
	@FXML
	JFXToggleButton hiworksToggle;
	
	public static Boolean hiworks_setting_stat = true;
	
	@Override
    public void initialize(URL url, ResourceBundle rb) {
		if(hiworks_setting_stat == true) {
			hiworksToggle.setSelected(true);
		}else {
			hiworksToggle.setSelected(false);
		}
		hiworksToggle.selectedProperty().addListener(new ChangeListener < Boolean > () {
			@Override
			public void changed(ObservableValue<? extends Boolean> arg0, Boolean arg1, Boolean arg2) {
				if(hiworksToggle.isSelected() == true) {
					hiworks_setting_stat = true;
				}else {
					hiworks_setting_stat = false;
				}
			}
		});
	}
}

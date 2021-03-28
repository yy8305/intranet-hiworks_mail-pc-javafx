package util;

import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.skins.JFXTextFieldSkin;
import com.sun.javafx.scene.control.skin.TextFieldSkin;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

public class PasswordSkin extends JFXTextFieldSkin {
    public PasswordSkin(JFXPasswordField textField) {
        super(textField);
    }

    protected String maskText(String txt) {
        if (getSkinnable() instanceof PasswordField) {
            int n = txt.length();
            StringBuilder passwordBuilder = new StringBuilder(n);
            for (int i = 0; i < n; i++)
                passwordBuilder.append("*");

            return passwordBuilder.toString();
        } else {
            return txt;
        }
    }
}
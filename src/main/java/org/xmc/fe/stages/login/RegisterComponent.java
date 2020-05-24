package org.xmc.fe.stages.login;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.xmc.fe.ui.FxmlComponentFactory;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.JMetroScene;

public class RegisterComponent {
    @FXML public TextField displayNameTextfield;
    @FXML public TextField usernameTextfield;
    @FXML public PasswordField passwordField1;
    @FXML public PasswordField passwordField2;

    @FXML
    public void onBack() {
        Parent registerComponent = FxmlComponentFactory.load(FxmlKey.LOGIN);

        Stage stage = (Stage)usernameTextfield.getScene().getWindow();
        stage.setScene(new JMetroScene(registerComponent));
    }
}

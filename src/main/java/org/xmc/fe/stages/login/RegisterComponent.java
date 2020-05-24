package org.xmc.fe.stages.login;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.xmc.fe.ui.DefaultScene;
import org.xmc.fe.ui.FxmlComponentFactory;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.validation.ValidationScene;

public class RegisterComponent {
    @FXML public TextField displayNameTextfield;
    @FXML public TextField usernameTextfield;
    @FXML public PasswordField passwordField1;

    @FXML
    public void onBack() {
        Parent registerComponent = FxmlComponentFactory.load(FxmlKey.LOGIN);

        Stage stage = (Stage)usernameTextfield.getScene().getWindow();
        stage.setScene(new ValidationScene(registerComponent));
    }

    @FXML
    public void onRegister() {
        Parent bootstrapComponent = FxmlComponentFactory.load(FxmlKey.BOOTSTRAP);

        Stage stage = (Stage)usernameTextfield.getScene().getWindow();
        stage.setScene(new DefaultScene(bootstrapComponent));
    }
}

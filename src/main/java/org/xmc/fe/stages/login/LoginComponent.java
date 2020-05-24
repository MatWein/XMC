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

public class LoginComponent {
    @FXML public TextField usernameTextfield;
    @FXML public PasswordField passwordField;

    @FXML
    public void init() {
        // TODO: load credentials...
    }

    @FXML
    public void onRegister() {
        Parent registerComponent = FxmlComponentFactory.load(FxmlKey.LOGIN_REGISTER);

        Stage stage = (Stage)usernameTextfield.getScene().getWindow();
        stage.setScene(new ValidationScene(registerComponent));
    }

    @FXML
    public void onLogin() {
        ValidationScene scene = (ValidationScene)usernameTextfield.getScene();
        if (!scene.validate()) {
            return;
        }

        BootstrapComponent.preprocessing = () -> {};
        BootstrapComponent.username = usernameTextfield.getText();
        BootstrapComponent.password = passwordField.getText();

        Parent bootstrapComponent = FxmlComponentFactory.load(FxmlKey.BOOTSTRAP);
        Stage stage = (Stage) scene.getWindow();
        stage.setScene(new DefaultScene(bootstrapComponent));
    }
}

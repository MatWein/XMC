package org.xmc.fe.stages.login;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.Pair;
import org.xmc.fe.ui.DefaultScene;
import org.xmc.fe.ui.FxmlComponentFactory;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.validation.ValidationScene;

public class LoginController {
    @FXML private TextField usernameTextfield;
    @FXML private PasswordField passwordField;

    @FXML
    public void initialize() {
        // TODO: load credentials...
    }

    @FXML
    public void onRegister() {
        Pair<Parent, LoginController> registerComponent = FxmlComponentFactory.load(FxmlKey.LOGIN_REGISTER);

        Stage stage = (Stage)usernameTextfield.getScene().getWindow();
        stage.setScene(new ValidationScene(registerComponent.getLeft()));
    }

    @FXML
    public void onLogin() {
        Pair<Parent, BootstrapController> bootstrapComponent = FxmlComponentFactory.load(FxmlKey.BOOTSTRAP);

        Stage stage = (Stage) usernameTextfield.getScene().getWindow();
        stage.setScene(new DefaultScene(bootstrapComponent.getLeft()));

        bootstrapComponent.getRight().start(
                usernameTextfield.getText(),
                passwordField.getText(),
                null);
    }
}

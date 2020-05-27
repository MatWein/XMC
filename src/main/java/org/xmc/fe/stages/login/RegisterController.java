package org.xmc.fe.stages.login;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.Pair;
import org.xmc.Main;
import org.xmc.be.services.login.UserRegistrationService;
import org.xmc.fe.ui.DefaultScene;
import org.xmc.fe.ui.FxmlComponentFactory;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.validation.ValidationScene;

public class RegisterController {
    @FXML private TextField displayNameTextfield;
    @FXML private TextField usernameTextfield;
    @FXML private PasswordField passwordField;

    @FXML
    public void onBack() {
        Pair<Parent, LoginController> loginComponent = FxmlComponentFactory.load(FxmlKey.LOGIN);

        Stage stage = (Stage)usernameTextfield.getScene().getWindow();
        stage.setScene(new ValidationScene(loginComponent.getLeft()));
    }

    @FXML
    public void onRegister() {
        Pair<Parent, BootstrapController> bootstrapComponent = FxmlComponentFactory.load(FxmlKey.BOOTSTRAP);

        Stage stage = (Stage)usernameTextfield.getScene().getWindow();
        stage.setScene(new DefaultScene(bootstrapComponent.getLeft()));

        bootstrapComponent.getRight().start(
                usernameTextfield.getText(),
                passwordField.getText(),
                this::registerUser,
                false);
    }

    private void registerUser() {
        UserRegistrationService userRegistrationService = Main.applicationContext.getBean(UserRegistrationService.class);
        userRegistrationService.registerNewUser(usernameTextfield.getText(), displayNameTextfield.getText());
    }
}

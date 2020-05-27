package org.xmc.fe.stages.login;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.Pair;
import org.controlsfx.control.ToggleSwitch;
import org.xmc.be.services.login.controller.CredentialFileController;
import org.xmc.be.services.login.dto.DtoCredentials;
import org.xmc.fe.ui.DefaultScene;
import org.xmc.fe.ui.FxmlComponentFactory;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.validation.ValidationScene;

import java.util.Optional;

public class LoginController {
    @FXML private TextField usernameTextfield;
    @FXML private PasswordField passwordField;
    @FXML private ToggleSwitch saveCredentialsToggle;
    @FXML private Label unsafeWarningLabel;

    @FXML
    public void initialize() {
        unsafeWarningLabel.visibleProperty().bind(saveCredentialsToggle.selectedProperty());

        Optional<DtoCredentials> dtoCredentials = CredentialFileController.readCredentialFile();
        if (dtoCredentials.isPresent()) {
            saveCredentialsToggle.setSelected(true);
            usernameTextfield.setText(dtoCredentials.get().getUsername());
            passwordField.setText(dtoCredentials.get().getPassword());
        }
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
                null,
                saveCredentialsToggle.isSelected());
    }
}

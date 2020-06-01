package org.xmc.fe.stages.login;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import org.xmc.be.services.login.dto.DtoBootstrapFile;
import org.xmc.fe.ui.DefaultScene;
import org.xmc.fe.ui.FxmlComponentFactory;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.validation.ValidationScene;

import java.util.Optional;

@Component
public class LoginController {
    @FXML private TextField usernameTextfield;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox saveCredentialsToggle;
    @FXML private CheckBox autoLoginToggle;
    @FXML private Label unsafeWarningLabel;

    @FXML
    public void initialize() {
        unsafeWarningLabel.visibleProperty().bind(saveCredentialsToggle.selectedProperty());

        autoLoginToggle.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (Boolean.TRUE.equals(newValue)) {
                saveCredentialsToggle.setSelected(true);
            }
        });
    }

    public void initialize(Optional<DtoBootstrapFile> dtoBootstrapFile) {
        if (dtoBootstrapFile.isPresent()) {
            saveCredentialsToggle.setSelected(dtoBootstrapFile.get().isSaveCredentials());
            autoLoginToggle.setSelected(dtoBootstrapFile.get().isAutoLogin());
            usernameTextfield.setText(dtoBootstrapFile.get().getUsername());
            passwordField.setText(dtoBootstrapFile.get().getPassword());
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
                new DtoBootstrapFile(
                        usernameTextfield.getText(),
                        passwordField.getText(),
                        saveCredentialsToggle.isSelected(),
                        autoLoginToggle.isSelected()),
                null);
    }
}

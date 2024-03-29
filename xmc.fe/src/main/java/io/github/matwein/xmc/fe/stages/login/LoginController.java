package io.github.matwein.xmc.fe.stages.login;

import io.github.matwein.xmc.common.stubs.login.DtoBootstrapFile;
import io.github.matwein.xmc.fe.stages.login.logic.BootstrapFileController;
import io.github.matwein.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.SceneUtil;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Optional;

@FxmlController
public class LoginController {
    @FXML private TextField usernameTextfield;
    @FXML private PasswordField passwordField;
    @FXML private CheckBox saveCredentialsToggle;
    @FXML private CheckBox autoLoginToggle;
    @FXML private Label unsafeWarningLabel;
    @FXML private Label versionLabel;

    @FXML
    public void initialize() {
        versionLabel.setText(BootstrapController.loadVersionWithoutSpringContext());
        unsafeWarningLabel.visibleProperty().bind(saveCredentialsToggle.selectedProperty());

        autoLoginToggle.selectedProperty().addListener((observableValue, oldValue, newValue) -> {
            if (Boolean.TRUE.equals(newValue)) {
                saveCredentialsToggle.setSelected(true);
            }
        });

        Optional<DtoBootstrapFile> dtoBootstrapFile = BootstrapFileController.readBootstrapFile();
        if (dtoBootstrapFile.isPresent()) {
            saveCredentialsToggle.setSelected(dtoBootstrapFile.get().isSaveCredentials());
            autoLoginToggle.setSelected(dtoBootstrapFile.get().isAutoLogin());
            usernameTextfield.setText(dtoBootstrapFile.get().getUsername());
            passwordField.setText(dtoBootstrapFile.get().getPassword());
        }
    }

    @FXML
    public void onRegister() {
        SceneUtil.switchSceneOfComponent(usernameTextfield, FxmlKey.LOGIN_REGISTER);
    }

    @FXML
    public void onLogin() {
        Pair<Parent, BootstrapController> bootstrapComponent = SceneUtil.switchSceneOfComponent(usernameTextfield, FxmlKey.BOOTSTRAP);

        bootstrapComponent.getRight().start(
                new DtoBootstrapFile(
                        usernameTextfield.getText(),
                        passwordField.getText(),
                        saveCredentialsToggle.isSelected(),
                        autoLoginToggle.isSelected()),
                null);
    }
}

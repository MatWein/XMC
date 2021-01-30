package org.xmc.fe.stages.login;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.tuple.Pair;
import org.xmc.Main;
import org.xmc.be.services.login.UserRegistrationService;
import org.xmc.common.stubs.login.DtoBootstrapFile;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.SceneUtil;

@FxmlController
public class RegisterController {
    @FXML private TextField displayNameTextfield;
    @FXML private TextField usernameTextfield;
    @FXML private PasswordField passwordField;

    @FXML
    public void onBack() {
        SceneUtil.switchSceneOfComponent(usernameTextfield, FxmlKey.LOGIN);
    }

    @FXML
    public void onRegister() {
        Pair<Parent, BootstrapController> bootstrapComponent = SceneUtil.switchSceneOfComponent(usernameTextfield, FxmlKey.BOOTSTRAP);

        bootstrapComponent.getRight().start(
                new DtoBootstrapFile(usernameTextfield.getText(), passwordField.getText(), false, false),
                this::registerUser);
    }

    private void registerUser() {
        UserRegistrationService userRegistrationService = Main.applicationContext.getBean(UserRegistrationService.class);
        userRegistrationService.registerNewUser(usernameTextfield.getText(), displayNameTextfield.getText());
    }
}

package io.github.matwein.xmc.fe.stages.login;

import io.github.matwein.xmc.common.services.login.IUserRegistrationService;
import io.github.matwein.xmc.common.stubs.login.DtoBootstrapFile;
import io.github.matwein.xmc.fe.common.XmcFrontendContext;
import io.github.matwein.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.SceneUtil;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.apache.commons.lang3.tuple.Pair;

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
        IUserRegistrationService userRegistrationService = XmcFrontendContext.applicationContext.get().getBean(IUserRegistrationService.class);
        userRegistrationService.registerNewUser(usernameTextfield.getText(), displayNameTextfield.getText());
    }
}

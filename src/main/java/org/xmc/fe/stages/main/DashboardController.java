package org.xmc.fe.stages.main;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ToolBar;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.stereotype.Component;
import org.xmc.Main;
import org.xmc.be.services.login.controller.BootstrapFileController;
import org.xmc.be.services.login.dto.DtoBootstrapFile;
import org.xmc.fe.stages.login.LoginController;
import org.xmc.fe.ui.FxmlComponentFactory;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;

import java.util.Optional;

@Component
public class DashboardController {
    @FXML private ToolBar toolbar;

    @FXML
    public void onLogout() {
        Optional<DtoBootstrapFile> dtoBootstrapFile = BootstrapFileController.readBootstrapFile();

        Pair<Parent, LoginController> component = FxmlComponentFactory.load(FxmlKey.LOGIN);
        component.getRight().initialize(dtoBootstrapFile);

        Stage loginStage = Main.createLoginStage(new Stage(), component);
        loginStage.show();

        ((Stage)toolbar.getScene().getWindow()).close();
    }

    @FXML
    public void onQuit() {
        ((Stage)toolbar.getScene().getWindow()).close();
    }
}

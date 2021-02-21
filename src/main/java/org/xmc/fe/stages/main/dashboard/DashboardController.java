package org.xmc.fe.stages.main.dashboard;

import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.ToolBar;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.Pair;
import org.xmc.Main;
import org.xmc.fe.stages.login.LoginController;
import org.xmc.fe.ui.FxmlComponentFactory;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.FxmlController;

@FxmlController
public class DashboardController {
    @FXML
    private ToolBar toolbar;

    @FXML
    public void onLogout() {
        Pair<Parent, LoginController> component = FxmlComponentFactory.load(FxmlKey.LOGIN);
        Stage loginStage = Main.createLoginStage(new Stage(), component);
        loginStage.show();

        ((Stage)toolbar.getScene().getWindow()).close();
    }

    @FXML
    public void onQuit() {
        ((Stage)toolbar.getScene().getWindow()).close();
    }
}

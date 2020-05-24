package org.xmc.fe;

import javafx.application.Application;
import javafx.stage.Stage;
import org.springframework.context.ApplicationContext;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.StageBuilder;

public class Main extends Application {
    public static ApplicationContext applicationContext = null;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        StageBuilder.getInstance()
                .withDefaultIcon()
                .withTitleKey(MessageKey.LOGIN_TITLE)
                .resizable(false)
                .withFxmlSceneComponent(FxmlKey.LOGIN)
                .build(primaryStage)
                .show();
    }
}

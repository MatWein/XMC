package org.xmc;

import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.xmc.common.utils.HomeDirectoryPathCalculator;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.StageBuilder;

@SpringBootApplication
public class Main extends Application {
    static {
        HomeDirectoryPathCalculator.initializeSystemProperties();
    }

    public static ConfigurableApplicationContext applicationContext = null;
    public static String[] args;

    public static void main(String[] args) {
        Main.args = args;
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        LoggerFactory.getLogger(Main.class).info("Opening login window.");

        StageBuilder.getInstance()
                .withDefaultIcon()
                .withTitleKey(MessageKey.LOGIN_TITLE)
                .resizable(false)
                .withFxmlSceneComponent(FxmlKey.LOGIN)
                .build(primaryStage)
                .show();
    }

    @Override
    public void stop() {
        destroy();
    }

    public static void destroy() {
        LoggerFactory.getLogger(Main.class).info("Closing application context.");

        if (applicationContext != null) {
            applicationContext.close();
            applicationContext = null;
        }
    }
}

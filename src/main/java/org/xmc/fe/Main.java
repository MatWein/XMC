package org.xmc.fe;

import javafx.application.Application;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.StageBuilder;

@SpringBootApplication
public class Main extends Application {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    public static ConfigurableApplicationContext applicationContext = null;
    public static String[] args;

    public static void main(String[] args) {
        LOGGER.info("Initializing application...");

        Main.args = args;
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        LOGGER.info("Opening login window.");

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
        if (applicationContext != null) {
            applicationContext.close();
            applicationContext = null;
        }
    }
}

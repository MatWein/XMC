package org.xmc;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.xmc.be.services.login.controller.BootstrapFileController;
import org.xmc.be.services.login.dto.DtoBootstrapFile;
import org.xmc.common.utils.HomeDirectoryPathCalculator;
import org.xmc.fe.stages.login.BootstrapController;
import org.xmc.fe.stages.login.LoginController;
import org.xmc.fe.ui.FxmlComponentFactory;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.StageBuilder;

import java.util.Optional;

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

        Optional<DtoBootstrapFile> dtoBootstrapFile = BootstrapFileController.readBootstrapFile();
        boolean autoLogin = dtoBootstrapFile.isPresent()
                && dtoBootstrapFile.get().isSaveCredentials()
                && dtoBootstrapFile.get().isAutoLogin()
                && StringUtils.isNotBlank(dtoBootstrapFile.get().getUsername())
                && StringUtils.isNotBlank(dtoBootstrapFile.get().getPassword());

        Pair<Parent, ?> component;
        if (autoLogin) {
            component = FxmlComponentFactory.load(FxmlKey.BOOTSTRAP);
        } else {
            component = FxmlComponentFactory.load(FxmlKey.LOGIN);
        }

        Stage stage = StageBuilder.getInstance()
                .withDefaultIcon()
                .withTitleKey(MessageKey.LOGIN_TITLE)
                .resizable(false)
                .withSceneComponent(component.getLeft())
                .build(primaryStage);

        if (autoLogin) {
            stage.setOnShown(windowEvent -> ((BootstrapController)component.getRight()).start(dtoBootstrapFile.get(), null));
        } else {
            ((LoginController)component.getRight()).initialize(dtoBootstrapFile);
        }

        stage.show();
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

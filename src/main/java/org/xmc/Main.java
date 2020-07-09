package org.xmc;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.xmc.be.services.login.controller.BootstrapFileController;
import org.xmc.common.stubs.login.DtoBootstrapFile;
import org.xmc.common.utils.HomeDirectoryPathCalculator;
import org.xmc.config.properties.XmcProperties;
import org.xmc.fe.stages.login.BootstrapController;
import org.xmc.fe.ui.FxmlComponentFactory;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.StageBuilder;

import java.util.Locale;
import java.util.Optional;

@SpringBootApplication
@EnableConfigurationProperties({ XmcProperties.class })
@EnableAsync
@EnableScheduling
public class Main extends Application {
    static {
        HomeDirectoryPathCalculator.initializeSystemProperties();

        String languageProperty = System.getProperty("xmc.language");
        if (StringUtils.isNotBlank(languageProperty)) {
            Locale.setDefault(Locale.forLanguageTag(languageProperty));
        }
    }

    public static ConfigurableApplicationContext applicationContext;
    public static HostServices hostServices;
    public static String[] args;

    public static void main(String[] args) {
        Main.args = args;
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        hostServices = getHostServices();

        Logger logger = LoggerFactory.getLogger(Main.class);
        logger.info("Opening login window.");

        Thread.setDefaultUncaughtExceptionHandler((t, e) -> logger.error("Unexpected error on thread '{}'.", t.getName(), e));

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

        Stage stage = createLoginStage(primaryStage, component);

        if (autoLogin) {
            stage.setOnShown(windowEvent -> ((BootstrapController)component.getRight()).start(dtoBootstrapFile.get(), null));
        }

        stage.show();
    }

    public static Stage createLoginStage(Stage primaryStage, Pair<Parent, ?> component) {
        return StageBuilder.getInstance()
                    .withDefaultIcon()
                    .withTitleKey(MessageKey.LOGIN_TITLE)
                    .resizable(false)
                    .withSceneComponent(component.getLeft())
                    .build(primaryStage);
    }

    @Override
    public void stop() {
        destroy();
    }

    public static void destroy() {
        LoggerFactory.getLogger(Main.class).info("Closing application context.");

        if (applicationContext != null) {
            SpringApplication.exit(applicationContext);
            applicationContext = null;
        }
    }
}

package io.github.matwein.xmc;

import io.github.matwein.xmc.common.stubs.login.DtoBootstrapFile;
import io.github.matwein.xmc.config.SCalcConfig;
import io.github.matwein.xmc.fe.SystemProperties;
import io.github.matwein.xmc.fe.common.HomeDirectoryPathCalculator;
import io.github.matwein.xmc.fe.common.XmcFrontendContext;
import io.github.matwein.xmc.fe.stages.login.BootstrapController;
import io.github.matwein.xmc.fe.stages.login.logic.BootstrapFileController;
import io.github.matwein.xmc.fe.stages.login.logic.LoginStageFactory;
import io.github.matwein.xmc.fe.ui.FxmlComponentFactory;
import io.github.matwein.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.stage.Stage;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Locale;
import java.util.Optional;

public class Main extends Application {
	static {
        HomeDirectoryPathCalculator.initializeSystemProperties();

        String languageProperty = System.getProperty(SystemProperties.XMC_LANGUAGE);
        if (StringUtils.isNotBlank(languageProperty)) {
            Locale.setDefault(Locale.forLanguageTag(languageProperty));
        }
    }

    public static void main(String[] args) {
	    XmcApplication.args = args;
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
	    XmcFrontendContext.hostServices = getHostServices();
	    XmcFrontendContext.applicationContext = new ApplicationContextWrapper();

        Logger logger = LoggerFactory.getLogger(Main.class);
        logger.info("Opening login window.");
	
	    SCalcConfig.init();
        
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> logger.error("Unexpected error on thread '{}'.", t.getName(), e));

        Optional<DtoBootstrapFile> dtoBootstrapFile = BootstrapFileController.readBootstrapFile();
        boolean autoLogin = dtoBootstrapFile.isPresent()
                && dtoBootstrapFile.get().isSaveCredentials()
                && dtoBootstrapFile.get().isAutoLogin()
                && StringUtils.isNotBlank(dtoBootstrapFile.get().getUsername())
                && StringUtils.isNotBlank(dtoBootstrapFile.get().getPassword())
                && new File(HomeDirectoryPathCalculator.calculateDatabaseDirForUser(dtoBootstrapFile.get().getUsername())).isDirectory();

        Pair<Parent, ?> component;
        if (autoLogin) {
            component = FxmlComponentFactory.load(FxmlKey.BOOTSTRAP);
        } else {
            component = FxmlComponentFactory.load(FxmlKey.LOGIN);
        }

        Stage stage = LoginStageFactory.createLoginStage(primaryStage, component);

        if (autoLogin) {
            stage.setOnShown(windowEvent -> startBootstrapping(dtoBootstrapFile, component));
        }
	
	    stage.show();
    }
	
	private void startBootstrapping(Optional<DtoBootstrapFile> dtoBootstrapFile, Pair<Parent, ?> component) {
		((BootstrapController) component.getRight()).start(dtoBootstrapFile.get(), null);
	}
	
    @Override
    public void stop() {
	    XmcApplication.destroy();
    }
}

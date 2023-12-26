package io.github.matwein.xmc.fe.stages.login;

import io.github.matwein.xmc.common.services.login.IUserLoginService;
import io.github.matwein.xmc.common.stubs.login.DtoBootstrapFile;
import io.github.matwein.xmc.fe.common.HomeDirectoryPathCalculator;
import io.github.matwein.xmc.fe.common.MessageAdapter;
import io.github.matwein.xmc.fe.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.fe.common.SleepUtil;
import io.github.matwein.xmc.fe.common.XmcFrontendContext;
import io.github.matwein.xmc.fe.config.BeanConfig;
import io.github.matwein.xmc.fe.stages.login.logic.BackupController;
import io.github.matwein.xmc.fe.stages.login.logic.BootstrapFileController;
import io.github.matwein.xmc.fe.stages.main.MainController;
import io.github.matwein.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.SceneUtil;
import io.github.matwein.xmc.fe.ui.StageBuilder;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;

import static io.github.matwein.xmc.fe.SystemProperties.*;

@FxmlController
public class BootstrapController {
	private static final Logger LOGGER = LoggerFactory.getLogger(BootstrapController.class);
	
	@FXML private VBox vbox;
    @FXML private Label statusLabel;
    @FXML private ProgressBar progressbar;
    @FXML private Label errorLabel;
    @FXML private Label versionLabel;
    @FXML private Button backButton;

    private DtoBootstrapFile dtoBootstrapFile;
    private Runnable preprocessing;
	
	@FXML
    public void initialize() {
        versionLabel.setText(loadVersionWithoutSpringContext());
    }

    public static String loadVersionWithoutSpringContext() {
        try {
            return new ProjectInfoAutoConfiguration(new ProjectInfoProperties()).buildProperties().getVersion();
        } catch (Exception e) {
            return new BeanConfig().buildProperties().getVersion();
        }
    }

    public void start(DtoBootstrapFile dtoBootstrapFile, Runnable preprocessing) {
        this.dtoBootstrapFile = dtoBootstrapFile;
        this.preprocessing = preprocessing;

        Thread thread = new Thread(this::run);
        thread.setDaemon(true);
        thread.start();
    }

    public void run() {
        getStage().setOnCloseRequest(Event::consume);

        try {
            runWithoutErrorHandling();
        } catch (Throwable e) {
            handleErrors(e);
        } finally {
            getStage().setOnCloseRequest(null);
        }
    }

    private void runWithoutErrorHandling() {
	    XmcFrontendContext.applicationContext.destroy();

        createApplicationContext();
        
	    backupFiles();
        
        runPreprocessing();
        
        doLogin();
    }
	
	private void backupFiles() {
		Platform.runLater(() -> statusLabel.setText(MessageAdapter.getByKey(MessageKey.BOOTSTRAP_STATUS_BACKUP_FILES)));
		BackupController.backupFiles(dtoBootstrapFile);
	}
	
	private void createApplicationContext() {
        Platform.runLater(() -> statusLabel.setText(MessageAdapter.getByKey(MessageKey.BOOTSTRAP_STATUS_CREATING_CONTEXT)));

        System.setProperty(USER_NAME, dtoBootstrapFile.getUsername());
        System.setProperty(USER_PASSWORD, dtoBootstrapFile.getPassword());
        System.setProperty(USER_DATABASE_DIR, HomeDirectoryPathCalculator.calculateDatabaseDirForUser(dtoBootstrapFile.getUsername()));
	
	    XmcFrontendContext.applicationContext.start();
    }
	
	private void runPreprocessing() {
        Platform.runLater(() -> statusLabel.setText(MessageAdapter.getByKey(MessageKey.BOOTSTRAP_STATUS_PREPROCESSING)));

        if (preprocessing != null) {
            preprocessing.run();
        }
    }

    private void doLogin() {
        Platform.runLater(() -> statusLabel.setText(MessageAdapter.getByKey(MessageKey.BOOTSTRAP_STATUS_LOGIN)));

        IUserLoginService userLoginService = XmcFrontendContext.applicationContext.get().getBean(IUserLoginService.class);
        String displayName = userLoginService.login(dtoBootstrapFile);
	
	    System.clearProperty(USER_NAME);
	    System.clearProperty(USER_PASSWORD);
	    System.clearProperty(USER_DATABASE_DIR);
	    System.setProperty(USER_DISPLAYNAME, displayName);
	
	    BootstrapFileController.writeBootstrapFile(dtoBootstrapFile);

        Platform.runLater(() -> {
            progressbar.setVisible(false);
            statusLabel.setText(MessageAdapter.getByKey(MessageKey.BOOTSTRAP_STATUS_LOGIN_FINISHED));
        });

        SleepUtil.sleep(500);

        Platform.runLater(() -> {
            MainController.mainWindow = StageBuilder.getInstance()
                    .resizable(true)
                    .maximized(true)
                    .minSize(1024, 768)
                    .withFxmlSceneComponent(FxmlKey.MAIN)
                    .withDefaultTitleKey()
                    .withDefaultIcon()
                    .build();
	        
	        MainController.mainWindow.show();

            getStage().setOnCloseRequest(null);
            getStage().close();
        });
    }

    private Stage getStage() {
        return (Stage)statusLabel.getScene().getWindow();
    }

    private void handleErrors(Throwable e) {
        LOGGER.error("Error on bootstrapping application.", e);

        Platform.runLater(() -> {
            vbox.getChildren().remove(progressbar);
            vbox.getChildren().remove(statusLabel);
            errorLabel.setVisible(true);
            backButton.setVisible(true);
            backButton.requestFocus();
        });
    }

    @FXML
    public void onBack() {
        SceneUtil.switchSceneOfComponent(backButton, FxmlKey.LOGIN);
    }
}

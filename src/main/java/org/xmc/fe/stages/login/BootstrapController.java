package org.xmc.fe.stages.login;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.xmc.Main;
import org.xmc.be.services.login.UserLoginService;
import org.xmc.common.stubs.login.DtoBootstrapFile;
import org.xmc.common.utils.HomeDirectoryPathCalculator;
import org.xmc.common.utils.SleepUtil;
import org.xmc.config.BeanConfig;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.SceneUtil;
import org.xmc.fe.ui.StageBuilder;

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
        versionLabel.setText(BeanConfig.loadVersionWithoutSprintContext());
    }

    public void start(DtoBootstrapFile dtoBootstrapFile, Runnable preprocessing) {
        this.dtoBootstrapFile = dtoBootstrapFile;
        this.preprocessing = preprocessing;

        Thread thread = new Thread(this::run);
        thread.setDaemon(true);
        thread.start();
    }

    public void run() {
        try {
            runWithoutErrorHandling();
        } catch (Throwable e) {
            handleErrors(e);
        }
    }

    private void runWithoutErrorHandling() {
        Main.destroy();

        createApplicationContext();
        runPreprocessing();
        doLogin();
    }

    private void createApplicationContext() {
        Platform.runLater(() -> statusLabel.setText(MessageAdapter.getByKey(MessageKey.BOOTSTRAP_STATUS_CREATING_CONTEXT)));

        System.setProperty("user.name", dtoBootstrapFile.getUsername());
        System.setProperty("user.password", dtoBootstrapFile.getPassword());
        System.setProperty("user.database.dir", HomeDirectoryPathCalculator.calculateDatabaseDirForUser(dtoBootstrapFile.getUsername()));

        Main.applicationContext = SpringApplication.run(Main.class, Main.args);
    }

    private void runPreprocessing() {
        Platform.runLater(() -> statusLabel.setText(MessageAdapter.getByKey(MessageKey.BOOTSTRAP_STATUS_PREPROCESSING)));

        if (preprocessing != null) {
            preprocessing.run();
        }
    }

    private void doLogin() {
        Platform.runLater(() -> statusLabel.setText(MessageAdapter.getByKey(MessageKey.BOOTSTRAP_STATUS_LOGIN)));

        UserLoginService userLoginService = Main.applicationContext.getBean(UserLoginService.class);
        userLoginService.login(dtoBootstrapFile);

        Platform.runLater(() -> {
            progressbar.setVisible(false);
            statusLabel.setText(MessageAdapter.getByKey(MessageKey.BOOTSTRAP_STATUS_LOGIN_FINISHED));
        });

        SleepUtil.sleep(500);

        Platform.runLater(() -> {
            StageBuilder.getInstance()
                    .resizable(true)
                    .maximized(true)
                    .minSize(1024, 768)
                    .withFxmlSceneComponent(FxmlKey.MAIN)
                    .withDefaultTitleKey()
                    .withDefaultIcon()
                    .build()
                    .show();

            ((Stage)statusLabel.getScene().getWindow()).close();
        });
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

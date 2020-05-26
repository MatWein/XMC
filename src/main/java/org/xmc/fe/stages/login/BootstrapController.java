package org.xmc.fe.stages.login;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.xmc.Main;
import org.xmc.be.services.login.UserLoginService;
import org.xmc.common.utils.HomeDirectoryPathCalculator;
import org.xmc.fe.ui.FxmlComponentFactory;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.validation.ValidationScene;

public class BootstrapController {
    private static final Logger LOGGER = LoggerFactory.getLogger(BootstrapController.class);

    @FXML private VBox vbox;
    @FXML private Label statusLabel;
    @FXML private ProgressBar progressbar;
    @FXML private Label errorLabel;
    @FXML private Button backButton;

    private String username;
    private String password;
    private Runnable preprocessing;

    public void start(String username, String password, Runnable preprocessing) {
        this.username = username;
        this.password = password;
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

        System.setProperty("user.name", username);
        System.setProperty("user.password", password);
        System.setProperty("derby.stream.error.file", HomeDirectoryPathCalculator.calculateDerbyLogFilePath());
        System.setProperty("user.database.dir", HomeDirectoryPathCalculator.calculateDatabaseDirForUser(username));

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
        userLoginService.login(username);

        // TODO: new stage
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
        Pair<Parent, LoginController> loginComponent = FxmlComponentFactory.load(FxmlKey.LOGIN);

        Stage stage = (Stage)backButton.getScene().getWindow();
        stage.setScene(new ValidationScene(loginComponent.getLeft()));
    }
}

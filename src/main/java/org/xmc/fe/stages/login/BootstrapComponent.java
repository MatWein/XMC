package org.xmc.fe.stages.login;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import org.springframework.boot.SpringApplication;
import org.springframework.util.DigestUtils;
import org.xmc.fe.Main;
import org.xmc.fe.config.DynamicDataSourceConfig;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.io.File;

public class BootstrapComponent implements Runnable {
    @FXML
    public Label statusLabel;

    static Runnable preprocessing;
    static String username;
    static String password;

    @FXML
    public void initialize() {
        new Thread(this).start();
    }

    @Override
    public void run() {
        if (Main.applicationContext != null) {
            Main.applicationContext.close();
        }

        Platform.runLater(() -> statusLabel.setText(MessageAdapter.getByKey(MessageKey.BOOTSTRAP_STATUS_CREATING_CONTEXT)));
        {
            File databaseFolder = new File(System.getProperty("user.dir"), "database");
            databaseFolder.mkdirs();

            DynamicDataSourceConfig.username = username;
            DynamicDataSourceConfig.password = password;
            DynamicDataSourceConfig.url = String.format("jdbc:derby:%s/%s;create=true;bootPassword=%s",
                    databaseFolder.getAbsoluteFile(),
                    DigestUtils.md5DigestAsHex(username.getBytes()),
                    password);

            Main.applicationContext = SpringApplication.run(Main.class, Main.args);
        }

        Platform.runLater(() -> statusLabel.setText(MessageAdapter.getByKey(MessageKey.BOOTSTRAP_STATUS_PREPROCESSING)));
        {
            preprocessing.run();
        }

        Platform.runLater(() -> statusLabel.setText(MessageAdapter.getByKey(MessageKey.BOOTSTRAP_STATUS_LOGIN)));
        {
            // TODO: login
        }
    }
}

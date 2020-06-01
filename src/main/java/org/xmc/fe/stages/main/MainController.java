package org.xmc.fe.stages.main;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.services.login.UserLoginService;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

@Component
public class MainController {
    private final MemoryBarController memoryBarController;

    @FXML private Label statusLabel;
    @FXML private ProgressBar memoryProgressbar;
    @FXML private Label displayNameLabel;

    @Autowired
    public MainController(MemoryBarController memoryBarController) {
        this.memoryBarController = memoryBarController;
    }

    @FXML
    private void initialize() {
        memoryBarController.startMemoryBarThread(memoryProgressbar);
        displayNameLabel.setText(MessageAdapter.getByKey(MessageKey.MAIN_DISPLAYNAME, System.getProperty(UserLoginService.SYSTEM_PROPERTY_DISPLAYNAME)));
    }
}

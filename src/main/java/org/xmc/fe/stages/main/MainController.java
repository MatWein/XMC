package org.xmc.fe.stages.main;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MainController {
    private final MemoryBarController memoryBarController;

    @FXML private Label statusLabel;
    @FXML private ProgressBar memoryProgressbar;

    @Autowired
    public MainController(MemoryBarController memoryBarController) {
        this.memoryBarController = memoryBarController;
    }

    @FXML
    private void initialize() {
        memoryBarController.startMemoryBarThread(memoryProgressbar);
    }
}

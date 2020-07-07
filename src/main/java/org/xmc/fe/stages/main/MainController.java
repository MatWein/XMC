package org.xmc.fe.stages.main;

import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.Region;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.be.services.login.UserLoginService;
import org.xmc.fe.stages.main.logic.MemoryBarController;
import org.xmc.fe.ui.CustomDialogBuilder;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.components.async.ProcessView;

@Component
public class MainController {
    private final MemoryBarController memoryBarController;

    public static Region backdropRef;
    public static ProcessView processViewRef;

    @FXML private ProcessView processView;
    @FXML private ProgressIndicator progressIndicator;
    @FXML private Label processCountLabel;
    @FXML private ProgressBar processProgressbar;
    @FXML private Region backdrop;
    @FXML private Label statusLabel;
    @FXML private ProgressBar memoryProgressbar;
    @FXML private Label displayNameLabel;

    @Autowired
    public MainController(MemoryBarController memoryBarController) {
        this.memoryBarController = memoryBarController;
    }

    @FXML
    public void initialize() {
        backdropRef = backdrop;
        processViewRef = processView;

        memoryBarController.startMemoryBarThread(memoryProgressbar);
        displayNameLabel.setText(MessageAdapter.getByKey(MessageKey.MAIN_DISPLAYNAME, System.getProperty(UserLoginService.SYSTEM_PROPERTY_DISPLAYNAME)));

        BooleanBinding itemCountObservable = processView.itemCountProperty().greaterThan(0);
        progressIndicator.visibleProperty().bind(itemCountObservable);
        processCountLabel.visibleProperty().bind(itemCountObservable);
        processProgressbar.visibleProperty().bind(itemCountObservable);

        processView.itemCountProperty().addListener((observable, oldValue, newValue) -> processCountLabel.setText(MessageAdapter.getByKey(MessageKey.MAIN_PROCESS_COUNTER, newValue.intValue())));
        processView.setProcessProgressbar(processProgressbar);
    }

    @FXML
    public void onAbout() {
        CustomDialogBuilder.getInstance()
                .titleKey(MessageKey.ABOUT_TITLE)
                .addButton(MessageKey.DIALOG_OK, ButtonData.OK_DONE)
                .withFxmlContent(FxmlKey.ABOUT)
                .withDefaultIcon()
                .build()
                .showAndWait();
    }

    @FXML
    public void onToggleProcessView() {
        processView.setVisible(!processView.isVisible());
    }
}

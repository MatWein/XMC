package org.xmc.fe.stages.main;

import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.xmc.be.services.login.UserLoginService;
import org.xmc.common.utils.SleepUtil;
import org.xmc.fe.ui.CustomDialogBuilder;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.FxmlController;
import org.xmc.fe.ui.IAfterStageShown;
import org.xmc.fe.ui.MessageAdapter;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.components.MemoryProgressBar;
import org.xmc.fe.ui.components.async.ProcessView;
import org.xmc.fe.ui.extras.ExtrasUsageCalculator;
import org.xmc.fe.ui.extras.SnowAnimationController;

import java.util.concurrent.TimeUnit;

@FxmlController
public class MainController implements IAfterStageShown<Void> {
    public volatile static Region backdropRef;
    public volatile static ProcessView processViewRef;
    public volatile static Stage mainWindow;
	
    private final ExtrasUsageCalculator extrasUsageCalculator;
	
	private SnowAnimationController snowAnimationController;

    @FXML private ProcessView processView;
    @FXML private ProgressIndicator progressIndicator;
    @FXML private Label processCountLabel;
    @FXML private ProgressBar processProgressbar;
    @FXML private Region backdrop;
    @FXML private AnchorPane snowRegion;
    @FXML private Label statusLabel;
    @FXML private MemoryProgressBar memoryProgressbar;
    @FXML private Label displayNameLabel;
	
    @Autowired
	public MainController(ExtrasUsageCalculator extrasUsageCalculator) {
	    this.extrasUsageCalculator = extrasUsageCalculator;
    }
	
	@FXML
    public void initialize() {
        backdropRef = backdrop;
        processViewRef = processView;

        memoryProgressbar.initialize();
        displayNameLabel.setText(MessageAdapter.getByKey(MessageKey.MAIN_DISPLAYNAME, System.getProperty(UserLoginService.SYSTEM_PROPERTY_DISPLAYNAME)));

        BooleanBinding itemCountObservable = processView.itemCountProperty().greaterThan(0);
        progressIndicator.visibleProperty().bind(itemCountObservable);
        processCountLabel.visibleProperty().bind(itemCountObservable);
        processProgressbar.visibleProperty().bind(itemCountObservable);

        processView.itemCountProperty().addListener((observable, oldValue, newValue) -> processCountLabel.setText(MessageAdapter.getByKey(MessageKey.MAIN_PROCESS_COUNTER, newValue.intValue())));
        processView.setProcessProgressbar(processProgressbar);
    }
	
	@Override
	public void afterStageShown(Void param) {
		if (extrasUsageCalculator.calculateUseWinterExtras()) {
			snowAnimationController = new SnowAnimationController(snowRegion);
			
			Thread thread = new Thread(() -> {
				SleepUtil.sleep(TimeUnit.SECONDS.toMillis(10));
				
				Platform.runLater(() -> {
					snowAnimationController.start();
					
					mainWindow.iconifiedProperty().addListener((observable, oldValue, newValue) -> {
						if (newValue) {
							snowAnimationController.stop();
						} else {
							snowAnimationController.start();
						}
					});
				});
			});
			thread.setDaemon(true);
			thread.start();
		}
	}
	
	@FXML
    public void onChangelog() {
	    CustomDialogBuilder.getInstance()
			    .titleKey(MessageKey.CHANGELOG_TITLE)
			    .addButton(MessageKey.DIALOG_OK, ButtonData.OK_DONE)
			    .withFxmlContent(FxmlKey.CHANGELOG)
			    .build()
			    .showAndWait();
    }
    
    @FXML
    public void onAbout() {
        CustomDialogBuilder.getInstance()
                .titleKey(MessageKey.ABOUT_TITLE)
                .addButton(MessageKey.DIALOG_OK, ButtonData.OK_DONE)
                .withFxmlContent(FxmlKey.ABOUT)
                .build()
                .showAndWait();
    }

    @FXML
    public void onToggleProcessView() {
        processView.setVisible(!processView.isVisible());
    }
    
    @FXML
    public void onSettings() {
	    CustomDialogBuilder.getInstance()
			    .titleKey(MessageKey.SETTINGS_TITLE)
			    .addButton(MessageKey.DIALOG_CLOSE, ButtonData.FINISH)
			    .withFxmlContent(FxmlKey.SETTINGS)
			    .build()
			    .showAndWait();
    }
}

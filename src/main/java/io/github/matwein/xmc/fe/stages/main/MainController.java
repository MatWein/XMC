package io.github.matwein.xmc.fe.stages.main;

import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.application.Platform;
import javafx.beans.binding.BooleanBinding;
import javafx.fxml.FXML;
import javafx.scene.Cursor;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import javafx.util.Duration;
import org.springframework.beans.factory.annotation.Autowired;
import io.github.matwein.xmc.be.entities.settings.SettingType;
import io.github.matwein.xmc.be.services.login.UserLoginService;
import io.github.matwein.xmc.be.services.settings.SettingsService;
import io.github.matwein.xmc.common.utils.DesktopUtils;
import io.github.matwein.xmc.common.utils.HomeDirectoryPathCalculator;
import io.github.matwein.xmc.common.utils.SleepUtil;
import io.github.matwein.xmc.fe.async.AsyncProcessor;
import io.github.matwein.xmc.fe.stages.main.logging.LogsController;
import io.github.matwein.xmc.fe.ui.CustomDialogBuilder;
import io.github.matwein.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import io.github.matwein.xmc.fe.ui.FxmlController;
import io.github.matwein.xmc.fe.ui.IAfterStageShown;
import io.github.matwein.xmc.fe.ui.MessageAdapter;
import io.github.matwein.xmc.fe.ui.MessageAdapter.MessageKey;
import io.github.matwein.xmc.fe.ui.components.MemoryProgressBar;
import io.github.matwein.xmc.fe.ui.components.async.ProcessView;
import io.github.matwein.xmc.fe.ui.extras.ExtrasUsageCalculator;
import io.github.matwein.xmc.fe.ui.extras.SnowAnimationController;

import java.util.concurrent.TimeUnit;
import java.util.function.BiConsumer;

@FxmlController
public class MainController implements IAfterStageShown<Void> {
    public volatile static Region backdropRef;
    public volatile static ProcessView processViewRef;
    public volatile static Label errorAlertImageViewRef;
    public volatile static Label statusLabelRef;
    public volatile static Stage mainWindow;
	
    private final ExtrasUsageCalculator extrasUsageCalculator;
	private final AsyncProcessor asyncProcessor;
	private final SettingsService settingsService;
	
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
	@FXML private Label errorAlertImageView;
	
    @Autowired
	public MainController(
		    ExtrasUsageCalculator extrasUsageCalculator,
		    AsyncProcessor asyncProcessor,
		    SettingsService settingsService) {
    	
	    this.extrasUsageCalculator = extrasUsageCalculator;
	    this.asyncProcessor = asyncProcessor;
	    this.settingsService = settingsService;
    }
	
	@FXML
    public void initialize() {
        backdropRef = backdrop;
        processViewRef = processView;
		errorAlertImageViewRef = errorAlertImageView;
		statusLabelRef = statusLabel;

        memoryProgressbar.initialize();
        displayNameLabel.setText(MessageAdapter.getByKey(MessageKey.MAIN_DISPLAYNAME, System.getProperty(UserLoginService.SYSTEM_PROPERTY_DISPLAYNAME)));

        BooleanBinding itemCountObservable = processView.itemCountProperty().greaterThan(0);
        progressIndicator.visibleProperty().bind(itemCountObservable);
        processCountLabel.visibleProperty().bind(itemCountObservable);
        processProgressbar.visibleProperty().bind(itemCountObservable);

        processView.itemCountProperty().addListener((observable, oldValue, newValue) -> processCountLabel.setText(MessageAdapter.getByKey(MessageKey.MAIN_PROCESS_COUNTER, newValue.intValue())));
        processView.setProcessProgressbar(processProgressbar);
		
		errorAlertImageView.managedProperty().bind(errorAlertImageView.visibleProperty());
		errorAlertImageView.setCursor(Cursor.HAND);
		errorAlertImageView.setOnMouseClicked(event -> {
			errorAlertImageView.setVisible(false);
			statusLabel.setText(MessageAdapter.getByKey(MessageKey.STATUS_READY));
			
			onLogs();
		});
		
		FadeTransition fadeTransition = new FadeTransition(Duration.seconds(2.5), errorAlertImageView);
		fadeTransition.setFromValue(1.0);
		fadeTransition.setToValue(0.0);
		fadeTransition.setCycleCount(Animation.INDEFINITE);
		
		errorAlertImageView.visibleProperty().addListener((observable, oldValue, newValue) -> {
			if (Boolean.TRUE.equals(newValue)) {
				fadeTransition.play();
			} else {
				fadeTransition.stop();
			}
		});
    }
	
	@Override
	public void afterStageShown(Void param) {
		asyncProcessor.runAsync(
				() -> {},
				monitor -> settingsService.loadSetting(monitor, SettingType.EXTRAS_SHOW_SNOW),
				this::initializeSnow,
				() -> {}
		);
	}
	
	private void initializeSnow(boolean showSnow) {
		if (extrasUsageCalculator.calculateUseWinterExtras() && showSnow) {
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
    public void onProtocol() {
	    CustomDialogBuilder.getInstance()
			    .titleKey(MessageKey.PROTOCOL_TITLE)
			    .addButton(MessageKey.DIALOG_OK, ButtonData.OK_DONE)
			    .withFxmlContent(FxmlKey.PROTOCOL)
			    .build()
			    .showAndWait();
    }
	
	@FXML
	public void onLogs() {
		CustomDialogBuilder.getInstance()
				.titleKey(MessageKey.LOGS_TITLE)
				.addButton(MessageKey.LOGS_OPEN_FOLDER, ButtonData.CANCEL_CLOSE, (BiConsumer<Dialog<?>, LogsController>) (dialog, controller) -> {
					DesktopUtils.openInFileExplorer(HomeDirectoryPathCalculator.calculateLogDir());
					dialog.close();
				})
				.addButton(MessageKey.DIALOG_OK, ButtonData.OK_DONE)
				.withFxmlContent(FxmlKey.LOGS)
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

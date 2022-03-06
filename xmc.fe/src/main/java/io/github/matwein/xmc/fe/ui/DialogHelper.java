package io.github.matwein.xmc.fe.ui;

import com.google.common.collect.Lists;
import io.github.matwein.xmc.fe.FeConstants;
import io.github.matwein.xmc.fe.common.MessageAdapter;
import io.github.matwein.xmc.fe.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.fe.stages.main.MainController;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.io.File;
import java.util.Optional;
import java.util.function.Supplier;

public class DialogHelper {
	private static File lastSelectedFile = null;
	
    public static boolean showConfirmDialog(MessageKey messageKey, Object... args) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setContentText(MessageAdapter.getByKey(messageKey, args));
	    
        DialogPane dialogPane = alert.getDialogPane();
	    dialogPane.getStylesheets().add(FeConstants.BASE_CSS_PATH);
	    dialogPane.setMinHeight(Region.USE_PREF_SIZE);
        dialogPane.getButtonTypes().setAll(Lists.newArrayList(ButtonType.NO, ButtonType.OK));

        prepareDefaultAlert(alert);

        return alert.showAndWait()
                .map(buttonType -> buttonType == ButtonType.OK)
                .orElse(false);
    }
	
	public static Optional<String> showTextInputDialog(MessageKey messageKey, Object... args) {
		TextInputDialog dialog = new TextInputDialog();
		dialog.setHeaderText(null);
		dialog.setContentText(MessageAdapter.getByKey(messageKey, args));
		
		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getStylesheets().add(FeConstants.BASE_CSS_PATH);
		dialogPane.setMinWidth(350.0);
		
		prepareDefaultAlert(dialog);
		
		return dialog.showAndWait();
	}

    public static Optional<File> showOpenFileDialog(Window ownerWindow, ExtensionFilterType extensionFilter) {
        FileChooser fileChooser = createFileChooser(extensionFilter);
        return showBackdrop(() -> {
	        File selectedFile = fileChooser.showOpenDialog(ownerWindow);
	        if (selectedFile != null && selectedFile.isFile()) {
		        lastSelectedFile = selectedFile;
	        }
	        
	        return Optional.ofNullable(selectedFile);
        });
    }
	
	public static Optional<File> showSaveFileDialog(Window ownerWindow, ExtensionFilterType extensionFilter) {
		FileChooser fileChooser = createFileChooser(extensionFilter);
		
		if (!extensionFilter.getExtensionFilter().getExtensions().isEmpty()) {
			fileChooser.setInitialFileName(extensionFilter.getExtensionFilter().getExtensions().get(0));
		}
		
		return showBackdrop(() -> {
			File selectedFile = fileChooser.showSaveDialog(ownerWindow);
			if (selectedFile != null && selectedFile.isFile()) {
				lastSelectedFile = selectedFile;
			}
			
			return Optional.ofNullable(selectedFile);
		});
	}

    static FileChooser createFileChooser(ExtensionFilterType extensionFilter) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle(MessageAdapter.getByKey(MessageKey.FILECHOOSER_TITLE));
        fileChooser.getExtensionFilters().add(extensionFilter.getExtensionFilter());
        fileChooser.setSelectedExtensionFilter(extensionFilter.getExtensionFilter());
        
        if (lastSelectedFile != null && lastSelectedFile.getParentFile().isDirectory()) {
        	fileChooser.setInitialDirectory(lastSelectedFile.getParentFile());
        }
        
        return fileChooser;
    }

    private static <T> T showBackdrop(Supplier<T> callable) {
        boolean showBackdrop = MainController.backdropRef != null && !MainController.backdropRef.isVisible();
        if (showBackdrop) {
            MainController.backdropRef.setVisible(true);
        }
        try {
            return callable.get();
        } finally {
            if (showBackdrop) {
                MainController.backdropRef.setVisible(false);
            }
        }
    }

    private static void prepareDefaultAlert(Dialog<?> alert) {
        alert.setTitle(MessageAdapter.getByKey(MessageKey.APP_NAME));

        Scene scene = SceneBuilder.getInstance().build(alert.getDialogPane().getScene());
        ((Stage)scene.getWindow()).getIcons().add(FeConstants.APP_ICON);

        CustomDialogBuilder.showBackdrop(alert);
    }
}

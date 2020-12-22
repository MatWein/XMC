package org.xmc.fe.ui;

import com.google.common.collect.Lists;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.layout.Region;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.xmc.fe.FeConstants;
import org.xmc.fe.stages.main.MainController;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.function.Supplier;

public class DialogHelper {
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

    public static Optional<File> showOpenFileDialog(Window ownerWindow, ExtensionFilterType extensionFilter) {
        FileChooser fileChooser = createFileChooser(extensionFilter);
        return showBackdrop(() -> Optional.ofNullable(fileChooser.showOpenDialog(ownerWindow)));
    }

    public static List<File> showOpenMultipleFileDialog(Window ownerWindow, ExtensionFilterType extensionFilter) {
        FileChooser fileChooser = createFileChooser(extensionFilter);
        return showBackdrop(() -> fileChooser.showOpenMultipleDialog(ownerWindow));
    }

    static FileChooser createFileChooser(ExtensionFilterType extensionFilter) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle(MessageAdapter.getByKey(MessageKey.FILECHOOSER_TITLE));
        fileChooser.getExtensionFilters().add(extensionFilter.getExtensionFilter());
        fileChooser.setSelectedExtensionFilter(extensionFilter.getExtensionFilter());
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

    private static void prepareDefaultAlert(Alert alert) {
        alert.setTitle(MessageAdapter.getByKey(MessageKey.APP_NAME));

        Scene scene = SceneBuilder.getInstance().build(alert.getDialogPane().getScene());
        ((Stage)scene.getWindow()).getIcons().add(FeConstants.APP_ICON);

        CustomDialogBuilder.showBackdrop(alert);
    }
}

package org.xmc.fe.ui;

import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Window;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.io.File;
import java.util.List;
import java.util.Optional;

public class DialogHelper {
    public static final ExtensionFilter IMAGE_EXTENSION_FILTER = new ExtensionFilter(
            MessageAdapter.getByKey(MessageKey.FILECHOOSER_IMAGES),
            "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.gif");

    public static Optional<File> showOpenFileDialog(Window ownerWindow, ExtensionFilter extensionFilter) {
        FileChooser fileChooser = createFileChooser(extensionFilter);
        return Optional.ofNullable(fileChooser.showOpenDialog(ownerWindow));
    }

    public static List<File> showOpenMultipleFileDialog(Window ownerWindow, ExtensionFilter extensionFilter) {
        FileChooser fileChooser = createFileChooser(extensionFilter);
        return fileChooser.showOpenMultipleDialog(ownerWindow);
    }

    static FileChooser createFileChooser(ExtensionFilter extensionFilter) {
        FileChooser fileChooser = new FileChooser();

        fileChooser.setTitle(MessageAdapter.getByKey(MessageKey.FILECHOOSER_TITLE));
        fileChooser.getExtensionFilters().add(extensionFilter);
        fileChooser.setSelectedExtensionFilter(extensionFilter);
        return fileChooser;
    }
}

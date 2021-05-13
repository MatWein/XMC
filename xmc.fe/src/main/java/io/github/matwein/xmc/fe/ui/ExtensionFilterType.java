package io.github.matwein.xmc.fe.ui;

import io.github.matwein.xmc.utils.MessageAdapter;
import javafx.stage.FileChooser.ExtensionFilter;

public enum ExtensionFilterType {
    CSV_OR_EXCEL(new ExtensionFilter(
            MessageAdapter.getByKey(MessageAdapter.MessageKey.FILECHOOSER_CSV_EXCEL),
            "*.csv", "*.xlsx", "*.xls")),

    IMAGES(new ExtensionFilter(
            MessageAdapter.getByKey(MessageAdapter.MessageKey.FILECHOOSER_IMAGES),
            "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.gif")),

    ALL(new ExtensionFilter(
            MessageAdapter.getByKey(MessageAdapter.MessageKey.FILECHOOSER_ALL),
            "*.*"))
    ;

    private final ExtensionFilter extensionFilter;

    ExtensionFilterType(ExtensionFilter extensionFilter) {
        this.extensionFilter = extensionFilter;
    }

    public ExtensionFilter getExtensionFilter() {
        return extensionFilter;
    }
}

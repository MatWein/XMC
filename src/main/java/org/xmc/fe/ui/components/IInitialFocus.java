package org.xmc.fe.ui.components;

import javafx.application.Platform;
import javafx.scene.Node;

public interface IInitialFocus {
    int MAX_FOCUS_REPEAT = 10;

    boolean isInitialFocus();
    void setInitialFocus(boolean initialFocus);

    default void requestInitialFocus() {
        requestInitialFocus(MAX_FOCUS_REPEAT);
    }

    default void requestInitialFocus(int max) {
        if (max > 0) {
            Platform.runLater(() -> {
                Node node = (Node)this;

                if (!node.isFocused()) {
                    node.requestFocus();
                    requestInitialFocus(max - 1);
                }
            });
        }
    }
}

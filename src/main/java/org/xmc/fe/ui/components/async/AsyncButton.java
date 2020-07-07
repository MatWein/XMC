package org.xmc.fe.ui.components.async;

import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Button;

public class AsyncButton extends Button implements IDisableAsync {
    private ObservableValue<? extends Boolean> observable;

    public AsyncButton() {
    }

    public AsyncButton(String text) {
        super(text);
    }

    public AsyncButton(String text, Node graphic) {
        super(text, graphic);
    }

    @Override
    public void bindDisable(ObservableValue<? extends Boolean> observable) {
        this.observable = observable;
        disableProperty().bind(observable);
    }

    @Override
    public void setDisableAsync(boolean disable) {
        if (disable) {
            disableProperty().unbind();
            setDisable(true);
        } else {
            setDisable(false);
            if (observable != null) {
                disableProperty().bind(observable);
            }
        }
    }
}

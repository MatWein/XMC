package org.xmc.fe.ui.components.async;

import javafx.beans.value.ObservableValue;

public interface IDisableAsync {
    void bindDisable(ObservableValue<? extends Boolean> observable);

    void setDisableAsync(boolean disable);
}

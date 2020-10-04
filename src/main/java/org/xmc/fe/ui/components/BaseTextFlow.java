package org.xmc.fe.ui.components;

import javafx.beans.binding.Bindings;
import javafx.scene.text.TextFlow;

public class BaseTextFlow extends TextFlow {
    public BaseTextFlow() {
        this.opacityProperty().bind(
                Bindings.when(this.disabledProperty())
                        .then(0.4)
                        .otherwise(1)
        );
    }
}

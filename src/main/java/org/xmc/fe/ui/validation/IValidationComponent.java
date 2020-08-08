package org.xmc.fe.ui.validation;

import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.Tooltip;

import java.util.LinkedHashSet;

public interface IValidationComponent {
    LinkedHashSet<String> validate();

    void initialize(Scene scene);

    String getCssClassInvalid();
    void setTooltip(Tooltip tooltip);
    ObservableList<String> getStyleClass();
}

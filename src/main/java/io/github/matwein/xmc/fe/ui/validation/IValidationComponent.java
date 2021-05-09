package io.github.matwein.xmc.fe.ui.validation;

import javafx.scene.Scene;
import javafx.scene.control.Tooltip;

import java.util.LinkedHashSet;

public interface IValidationComponent {
    LinkedHashSet<String> validate();
    default boolean isValid() { return validate().isEmpty(); }

    void initialize(Scene scene);

    String getCssClassInvalid();
    void setTooltip(Tooltip tooltip);
    void addStyleClass(String styleClass);
    void removeStyleClass(String styleClass);
}

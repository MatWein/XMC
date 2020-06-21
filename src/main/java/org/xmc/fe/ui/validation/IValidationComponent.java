package org.xmc.fe.ui.validation;

import javafx.scene.Scene;

public interface IValidationComponent {
    boolean validate();

    void initValidationEvent(Scene scene);
}

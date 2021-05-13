package io.github.matwein.xmc.fe.ui.validation.components;

import io.github.matwein.xmc.fe.ui.validation.IValidatedComponent;
import javafx.scene.control.Button;

public class ValidatedButton extends Button implements IValidatedComponent {
    @Override
    public void onFormValid() {
        this.setDisable(false);
    }

    @Override
    public void onFormInvalid() {
        this.setDisable(true);
    }
}

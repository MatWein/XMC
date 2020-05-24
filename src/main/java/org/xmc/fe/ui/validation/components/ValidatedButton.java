package org.xmc.fe.ui.validation.components;

import javafx.scene.control.Button;
import org.xmc.fe.ui.validation.IValidatedComponent;

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

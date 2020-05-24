package org.xmc.fe.ui.validation.components;

import javafx.scene.control.PasswordField;
import org.apache.commons.lang3.StringUtils;
import org.xmc.fe.ui.validation.IValidationComponent;
import org.xmc.fe.ui.validation.ValidationScene;

public class ValidationPasswordField extends PasswordField implements IValidationComponent {
    public static final String CSS_CLASS_INVALID = "passwordfield-invalid";

    private boolean required;

    public boolean isRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public boolean isValid() {
        return !required || StringUtils.isNotBlank(this.getText());
    }

    @Override
    public boolean validate() {
        boolean valid = isValid();

        if (valid) {
            this.getStyleClass().removeAll(CSS_CLASS_INVALID);
        } else {
            this.getStyleClass().add(CSS_CLASS_INVALID);
        }

        return valid;
    }

    @Override
    public void initValidationEvent(ValidationScene scene) {
        textProperty().addListener((a, b, c) -> scene.validate());
    }
}

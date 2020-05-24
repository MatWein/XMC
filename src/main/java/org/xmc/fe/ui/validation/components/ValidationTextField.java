package org.xmc.fe.ui.validation.components;

import javafx.animation.PauseTransition;
import javafx.scene.control.TextField;
import javafx.util.Duration;
import org.apache.commons.lang3.StringUtils;
import org.xmc.fe.ui.validation.IValidationComponent;
import org.xmc.fe.ui.validation.ValidationScene;

public class ValidationTextField extends TextField implements IValidationComponent {
    private static final String CSS_CLASS_INVALID = "textfield-invalid";
    private static final Duration DELAY = Duration.millis(500);

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
        PauseTransition pause = new PauseTransition(DELAY);
        this.textProperty().addListener((observable, oldValue, newValue) -> {
            pause.setOnFinished(event -> scene.validate());
            pause.playFromStart();
        });
    }
}

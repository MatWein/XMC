package org.xmc.fe.ui.validation.components;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.xmc.fe.ui.validation.*;

import java.util.LinkedHashSet;

public class ValidationTextArea extends TextArea implements IValidationComponent, IRequired, ILength, IEqualTo, ICustomValidator {
    private static final String CSS_CLASS_INVALID = "textfield-invalid";

    private boolean required;
    private boolean disableTabChar;
    private Integer minLength;
    private Integer maxLength = 255;
    private String equalTo;
    private String customValidator;

    @Override
    public LinkedHashSet<String> validate() {
        return CommonTextfieldValidator.validate(this);
    }

    @Override
    public void initialize(Scene scene) {
        CommonTextfieldValidator.initValidationEvent(this, scene);

        if (disableTabChar) {
            this.addEventFilter(KeyEvent.KEY_PRESSED, event -> {
                if (event.getCode() == KeyCode.TAB && !event.isShiftDown() && !event.isControlDown()) {
                    event.consume();

                    Node source = (Node) event.getSource();
                    KeyEvent newEvent = new KeyEvent(source,
                            event.getTarget(), event.getEventType(),
                            event.getCharacter(), event.getText(),
                            event.getCode(), event.isShiftDown(),
                            true, event.isAltDown(),
                            event.isMetaDown());

                    source.fireEvent(newEvent);
                }
            });
        }
    }

    @Override
    public String getCssClassInvalid() {
        return CSS_CLASS_INVALID;
    }

    @Override
    public boolean isRequired() {
        return required;
    }

    @Override
    public void setRequired(boolean required) {
        this.required = required;
    }

    @Override
    public Integer getMinLength() {
        return minLength;
    }

    @Override
    public void setMinLength(Integer minLength) {
        this.minLength = minLength;
    }

    @Override
    public Integer getMaxLength() {
        return maxLength;
    }

    @Override
    public void setMaxLength(Integer maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public String getEqualTo() {
        return equalTo;
    }

    @Override
    public void setEqualTo(String equalTo) {
        this.equalTo = equalTo;
    }

    @Override
    public String getCustomValidator() {
        return customValidator;
    }

    @Override
    public void setCustomValidator(String customValidator) {
        this.customValidator = customValidator;
    }

    public boolean isDisableTabChar() {
        return disableTabChar;
    }

    public void setDisableTabChar(boolean disableTabChar) {
        this.disableTabChar = disableTabChar;
    }
}

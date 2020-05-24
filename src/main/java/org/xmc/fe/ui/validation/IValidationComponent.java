package org.xmc.fe.ui.validation;

public interface IValidationComponent {
    boolean isValid();
    boolean validate();

    void initValidationEvent(ValidationScene scene);
}

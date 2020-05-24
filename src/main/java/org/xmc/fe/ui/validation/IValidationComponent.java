package org.xmc.fe.ui.validation;

public interface IValidationComponent {
    boolean validate();

    void initValidationEvent(ValidationScene scene);
}

package org.xmc.fe.ui.validation;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Parent;
import javafx.scene.control.TextField;
import org.xmc.fe.ui.DefaultScene;

import java.util.HashSet;
import java.util.Set;

public class ValidationScene extends DefaultScene {
    private final Set<IValidationComponent> validationComponents = new HashSet<>();
    private final Set<IValidatedComponent> nodesToUpdateAfterValidation = new HashSet<>();

    public ValidationScene(Parent component) {
        super(component);

        getAllChildren(component).stream()
                .filter(c -> c instanceof IValidationComponent)
                .map(c -> (IValidationComponent)c)
                .forEach(this::registerValidationComponent);

        getAllChildren(component).stream()
                .filter(c -> c instanceof IValidatedComponent)
                .map(c -> (IValidatedComponent)c)
                .forEach(this::registerNodeToUpdateAfterValidation);

        getAllChildren(component).stream()
                .filter(c -> c instanceof TextField)
                .map(c -> (TextField)c)
                .forEach(this::overrideOnAction);

        validate();
    }

    private void overrideOnAction(TextField textField) {
        EventHandler<ActionEvent> onAction = textField.getOnAction();
        if (onAction != null) {
            textField.setOnAction(actionEvent -> {
                if (validate()) {
                    onAction.handle(actionEvent);
                }
            });
        }
    }

    private void registerValidationComponent(IValidationComponent component) {
        validationComponents.add(component);
        component.initValidationEvent(this);
    }

    private void registerNodeToUpdateAfterValidation(IValidatedComponent component) {
        nodesToUpdateAfterValidation.add(component);
    }

    public boolean validate() {
        boolean allValid = true;

        for (IValidationComponent validationComponent : validationComponents) {
            allValid &= validationComponent.validate();
        }

        for (IValidatedComponent node : nodesToUpdateAfterValidation) {
            if (allValid) {
                node.onFormValid();
            } else {
                node.onFormInvalid();
            }
        }

        return allValid;
    }
}

package org.xmc.fe.ui.validation;

import javafx.scene.Parent;
import org.xmc.fe.ui.DefaultScene;

import java.util.HashSet;
import java.util.Set;

public class ValidationScene extends DefaultScene {
    private Set<IValidationComponent> validationComponents = new HashSet<>();
    private Set<IValidatedComponent> nodesToUpdateAfterValidation = new HashSet<>();

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

        validate();
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

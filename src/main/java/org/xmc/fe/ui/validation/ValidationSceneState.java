package org.xmc.fe.ui.validation;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import org.xmc.fe.ui.SceneUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ValidationSceneState {
    private final Set<IValidationComponent> validationComponents = new HashSet<>();
    private final Set<IValidatedComponent> nodesToUpdateAfterValidation = new HashSet<>();
    private final Scene scene;

    public ValidationSceneState(Scene scene) {
        this.scene = scene;
        List<Node> allChildren = SceneUtil.getAllChildren(scene.getRoot());

        allChildren.stream()
                .filter(c -> c instanceof IValidationComponent)
                .map(c -> (IValidationComponent)c)
                .forEach(this::registerValidationComponent);

        allChildren.stream()
                .filter(c -> c instanceof IValidatedComponent)
                .map(c -> (IValidatedComponent)c)
                .forEach(this::registerNodeToUpdateAfterValidation);

        allChildren.stream()
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
        component.initValidationEvent(scene);
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

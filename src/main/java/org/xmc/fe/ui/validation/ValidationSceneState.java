package org.xmc.fe.ui.validation;

import com.google.common.collect.Sets;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import org.xmc.fe.ui.SceneUtil;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class ValidationSceneState {
    private static final Set<ButtonData> BUTTON_TYPES_TO_DISABLE = Sets.newHashSet(ButtonData.OK_DONE, ButtonData.APPLY, ButtonData.FINISH, ButtonData.YES);

    private final Set<IValidationComponent> validationComponents = new HashSet<>();
    private final Set<IValidatedComponent> nodesToUpdateAfterValidation = new HashSet<>();
    private final Set<Node> dialogButtons = new HashSet<>();
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

        if (scene.getRoot() instanceof DialogPane) {
            dialogButtons.addAll(lookupDialogButtons());
        }

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

        for (Node dialogButton : dialogButtons) {
            dialogButton.setDisable(!allValid);
        }

        return allValid;
    }

    private Set<Node> lookupDialogButtons() {
        DialogPane dialogPane = (DialogPane) scene.getRoot();
        List<ButtonType> buttonTypesToDisable = dialogPane.getButtonTypes().stream()
                .filter(buttonType -> BUTTON_TYPES_TO_DISABLE.contains(buttonType.getButtonData()))
                .collect(Collectors.toList());

        return buttonTypesToDisable.stream()
                .map(dialogPane::lookupButton)
                .filter(Objects::nonNull)
                .collect(Collectors.toSet());
    }
}

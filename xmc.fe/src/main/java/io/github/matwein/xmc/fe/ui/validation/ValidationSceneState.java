package io.github.matwein.xmc.fe.ui.validation;

import io.github.matwein.xmc.fe.ui.SceneUtil;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;

import java.util.*;
import java.util.stream.Collectors;

public class ValidationSceneState {
    private static final Set<ButtonData> BUTTON_TYPES_TO_DISABLE = Set.of(ButtonData.OK_DONE, ButtonData.APPLY, ButtonData.FINISH, ButtonData.YES);
    private static final String PREFIX = "- ";

    private final Set<IValidationComponent> validationComponents = new HashSet<>();
    private final Set<IValidatedComponent> nodesToUpdateAfterValidation = new HashSet<>();
    private final Set<IValidationController> validationControllers = new HashSet<>();
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

        allChildren.stream()
                .map(Node::getUserData)
                .filter(Objects::nonNull)
                .filter(c -> c instanceof IValidationController)
                .map(c -> (IValidationController)c)
                .forEach(this::registerValidationController);

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
        component.initialize(scene);
    }

    private void registerNodeToUpdateAfterValidation(IValidatedComponent component) {
        nodesToUpdateAfterValidation.add(component);
    }

    private void registerValidationController(IValidationController controller) {
        validationControllers.add(controller);
    }

    public boolean validate() {
        Map<IValidationComponent, List<String>> validationErrors = new HashMap<>();

        for (IValidationComponent validationComponent : validationComponents) {
            LinkedHashSet<String> fieldErrors = validationComponent.validate();
	
            List<String> errorsByKey = validationErrors.getOrDefault(validationComponent, new ArrayList<>());
            errorsByKey.addAll(fieldErrors);
            validationErrors.put(validationComponent, errorsByKey);
        }
        
        for (IValidationController validationController : validationControllers) {
            Map<IValidationComponent, List<String>> controllerErrors = validationController.validate();
            validationErrors.putAll(controllerErrors);
        }

        boolean allValid = validationErrors.values().stream()
		        .flatMap(Collection::stream)
		        .collect(Collectors.toSet())
		        .isEmpty();
        
        for (IValidationComponent validationComponent : validationComponents) {
            List<String> validationErrorsForField = validationErrors.get(validationComponent).stream()
                    .map(errorMessage -> PREFIX + errorMessage)
                    .collect(Collectors.toList());

            if (validationErrorsForField.isEmpty()) {
                validationComponent.removeStyleClass(validationComponent.getCssClassInvalid());
                validationComponent.setTooltip(null);
            } else {
                validationComponent.addStyleClass(validationComponent.getCssClassInvalid());
                validationComponent.setTooltip(new Tooltip(String.join(System.lineSeparator(), validationErrorsForField)));
            }
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

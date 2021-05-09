package io.github.matwein.xmc.fe.ui;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TitledPane;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.Pair;
import io.github.matwein.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import io.github.matwein.xmc.fe.ui.validation.ValidationSceneState;

import java.util.ArrayList;
import java.util.List;

public class SceneUtil {
    public static <T> Pair<Parent, T> switchSceneOfComponent(Node componentToGetStage, FxmlKey fxmlKeyOfNewSceneComponent) {
        Pair<Parent, T> component = FxmlComponentFactory.load(fxmlKeyOfNewSceneComponent);

        Stage stage = (Stage)componentToGetStage.getScene().getWindow();
        stage.setScene(SceneBuilder.getInstance().withRoot(component.getLeft()).build());

        return component;
    }

    public static ValidationSceneState getOrCreateValidationSceneState(Scene scene) {
        if (scene.getUserData() instanceof ValidationSceneState) {
            return (ValidationSceneState)scene.getUserData();
        } else {
            return createValidationSceneState(scene);
        }
    }

    public static ValidationSceneState createValidationSceneState(Scene scene) {
        var validationSceneState = new ValidationSceneState(scene);
        scene.setUserData(validationSceneState);
        return validationSceneState;
    }

    public static List<Node> getAllChildren(Parent root) {
        List<Node> nodes = new ArrayList<>();
        addAllChildrenRecursive(root, nodes);
        return nodes;
    }

    private static void addAllChildrenRecursive(Parent parent, List<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent) {
                addAllChildrenRecursive((Parent) node, nodes);
            }
            if (node instanceof TitledPane) {
                addAllChildrenRecursive((Parent) ((TitledPane)node).getContent(), nodes);
            }
        }
    }
}

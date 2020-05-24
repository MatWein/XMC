package org.xmc.fe.ui;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

import java.util.ArrayList;
import java.util.List;

public class DefaultScene extends Scene {
    public DefaultScene(Parent component) {
        super(component);

        JMetro jMetro = new JMetro(this, Style.LIGHT);
        jMetro.reApplyTheme();
    }

    public List<Node> getAllChildren(Parent root) {
        List<Node> nodes = new ArrayList<>();
        addAllChildrenRecursive(root, nodes);
        return nodes;
    }

    private void addAllChildrenRecursive(Parent parent, List<Node> nodes) {
        for (Node node : parent.getChildrenUnmodifiable()) {
            nodes.add(node);
            if (node instanceof Parent) {
                addAllChildrenRecursive((Parent) node, nodes);
            }
        }
    }
}

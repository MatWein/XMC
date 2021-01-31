package org.xmc.fe.ui.components;

import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class BreadcrumbBar<T> extends HBox {
    private static final Image CHEVRON_IMAGE = new Image(BreadcrumbBar.class.getResourceAsStream("/images/feather/chevron-right.png"));

    private final ObservableList<BreadcrumbPathElement<T>> elements = FXCollections.observableArrayList();

    public BreadcrumbBar() {
        this.setPrefHeight(25.0);
        this.elements.addListener((ListChangeListener<BreadcrumbPathElement<T>>) change -> onElementsChanged());
    }

    private void onElementsChanged() {
        List<Node> nodes = new ArrayList<>();

        for (int i = 0; i < elements.size(); i++) {
            BreadcrumbPathElement<T> element = elements.get(i);
            if (i > 0) {
                nodes.add(createChevronLabel());
            }

            nodes.add(createElementButton(i, element));
        }

        getChildren().clear();
        getChildren().addAll(nodes);
    }

    private Button createElementButton(int i, BreadcrumbPathElement<T> element) {
        Button button = new Button(element.getTextOrNull());

        boolean hasActionAssigned = element.getOnAction() != null;
        if (hasActionAssigned) {
            button.setOnAction(element.getOnAction());
        }

        boolean isLastButton = i == elements.size() - 1;
        if (isLastButton) {
            button.setDisable(true);
        }

        return button;
    }

    private Label createChevronLabel() {
        Label chevronLabel = new Label("", new ImageView(CHEVRON_IMAGE));

        chevronLabel.setDisable(true);
        chevronLabel.setPadding(new Insets(5, 0, 5, 0));

        return chevronLabel;
    }

    public static class BreadcrumbPathElement<T> {
        private String id;
        private String text;
        private T data;
        private EventHandler<ActionEvent> onAction;

        public BreadcrumbPathElement() {
            this.id = UUID.randomUUID().toString();
        }

        public BreadcrumbPathElement(String text) {
            this();
            this.text = text;
        }
	
	    public BreadcrumbPathElement(String text, EventHandler<ActionEvent> onAction) {
		    this();
		    this.text = text;
		    this.onAction = onAction;
	    }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getTextOrNull() {
            return text;
        }

        public void setText(String text) {
            this.text = text;
        }

        public T getData() {
            return data;
        }

        public void setData(T data) {
            this.data = data;
        }

        public EventHandler<ActionEvent> getOnAction() {
            return onAction;
        }

        public void setOnAction(EventHandler<ActionEvent> onAction) {
            this.onAction = onAction;
        }
    }

    public ObservableList<BreadcrumbPathElement<T>> getElements() {
        return elements;
    }
}

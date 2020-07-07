package org.xmc.fe.ui.components.async;

import javafx.application.Platform;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class ProcessView extends ScrollPane {
    private final VBox vBox;

    private final ObservableList<ProcessViewElement> elements = FXCollections.observableArrayList();
    private final SimpleIntegerProperty itemCount = new SimpleIntegerProperty(0);

    private ProgressBar processProgressbar;

    public ProcessView() {
        vBox = new VBox();
        setContent(vBox);

        getStyleClass().add("progress-view");
        setHbarPolicy(ScrollBarPolicy.NEVER);
        setVbarPolicy(ScrollBarPolicy.ALWAYS);
        setFitToHeight(true);
        setFitToWidth(true);

        elements.addListener((ListChangeListener<ProcessViewElement>) c -> itemCount.set(elements.size()));
        itemCount.addListener((observable, oldValue, newValue) -> {
            if (newValue.intValue() == 0) {
                setVisible(false);
            }
        });
    }

    public void updateProcessProgressbar() {
        if (processProgressbar == null) {
            return;
        }

        processProgressbar.setProgress(elements.stream()
                .mapToDouble(ProcessViewElement::getProgress)
                .filter(value -> value >= 0.0)
                .average()
                .orElse(0.0));
    }

    public ProcessViewElement addNewElement() {
        ProcessViewElement element = new ProcessViewElement(this);
        Platform.runLater(() -> {
            if (!elements.contains(element)) {
                elements.add(element);
            }
            if (!vBox.getChildren().contains(element)) {
                vBox.getChildren().add(element);
            }
        });
        return element;
    }

    public void removeElement(ProcessViewElement element) {
        Platform.runLater(() -> {
            elements.remove(element);
            vBox.getChildren().remove(element);
        });
    }

    public int getItemCount() {
        return itemCount.get();
    }

    public SimpleIntegerProperty itemCountProperty() {
        return itemCount;
    }

    public ProgressBar getProcessProgressbar() {
        return processProgressbar;
    }

    public void setProcessProgressbar(ProgressBar processProgressbar) {
        this.processProgressbar = processProgressbar;
    }
}

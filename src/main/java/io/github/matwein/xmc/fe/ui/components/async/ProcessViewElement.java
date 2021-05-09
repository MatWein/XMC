package io.github.matwein.xmc.fe.ui.components.async;

import javafx.application.Platform;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import io.github.matwein.xmc.fe.ui.MessageAdapter;
import io.github.matwein.xmc.fe.ui.MessageAdapter.MessageKey;

public class ProcessViewElement extends VBox {
    private final Label statusLabel;
    private final ProgressBar progressBar;
    private final ProcessView processView;

    private String text;
    private double progress;

    public ProcessViewElement(ProcessView processView) {
        this.processView = processView;

        getStyleClass().add("progress-view-element");

        statusLabel = new Label();
        statusLabel.setText(MessageAdapter.getByKey(MessageKey.MAIN_NEW_PROCESS));
        VBox.setVgrow(statusLabel, Priority.ALWAYS);

        progressBar = new ProgressBar();
        progressBar.setProgress(-1.0);
        VBox.setVgrow(progressBar, Priority.ALWAYS);

        getChildren().add(statusLabel);
        getChildren().add(progressBar);
    }

    public String getTextOrNull() {
        return text;
    }

    public void setText(String text) {
        this.text = text;

        Platform.runLater(() -> statusLabel.setText(text));
    }

    public double getProgress() {
        return progress;
    }

    public void setProgress(double progress) {
        this.progress = progress;

        Platform.runLater(() -> {
            progressBar.setProgress(progress);
            processView.updateProcessProgressbar();
        });
    }
}

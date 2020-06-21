package org.xmc.fe.ui;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.Pair;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

public class DialogBuilder<CONTROLLER_TYPE, RETURN_TYPE> {
    public static DialogBuilder getInstance() { return new DialogBuilder(); }

    private MessageKey titleKey;
    private MessageKey headerTextKey;
    private Node content;
    private Node headerGraphic;
    private BiFunction<ButtonType, CONTROLLER_TYPE, RETURN_TYPE> resultConverter;
    private BiConsumer<CONTROLLER_TYPE, RETURN_TYPE> inputConverter;
    private CONTROLLER_TYPE controller;
    private RETURN_TYPE input;
    private List<ButtonType> buttons = new ArrayList<>();
    private boolean useDefaultIcon;

    public DialogBuilder titleKey(MessageKey titleKey) {
        this.titleKey = titleKey;
        return this;
    }

    public DialogBuilder headerTextKey(MessageKey headerTextKey) {
        this.headerTextKey = headerTextKey;
        return this;
    }

    public DialogBuilder headerGraphic(Node headerGraphic) {
        this.headerGraphic = headerGraphic;
        return this;
    }

    public DialogBuilder withContent(Node content) {
        this.content = content;
        return this;
    }

    public DialogBuilder withFxmlContent(FxmlKey key) {
        Pair<Parent, CONTROLLER_TYPE> component = FxmlComponentFactory.load(key);
        this.controller = component.getRight();
        return withContent(component.getLeft());
    }

    public DialogBuilder addButton(MessageKey buttonTextKey, ButtonData buttonData) {
        buttons.add(new ButtonType(MessageAdapter.getByKey(buttonTextKey), buttonData));
        return this;
    }

    public DialogBuilder resultConverter(BiFunction<ButtonType, CONTROLLER_TYPE, RETURN_TYPE> resultConverter) {
        this.resultConverter = resultConverter;
        return this;
    }

    public DialogBuilder inputConverter(BiConsumer<CONTROLLER_TYPE, RETURN_TYPE> inputConverter) {
        this.inputConverter = inputConverter;
        return this;
    }

    public DialogBuilder withInput(RETURN_TYPE input) {
        this.input = input;
        return this;
    }

    public DialogBuilder withDefaultIcon() {
        this.useDefaultIcon = true;
        return this;
    }

    public Dialog<RETURN_TYPE> build() {
        Dialog<RETURN_TYPE> dialog = new Dialog<>();

        dialog.setTitle(MessageAdapter.getByKey(titleKey));
        dialog.setHeaderText(MessageAdapter.getByKey(headerTextKey));
        dialog.setGraphic(headerGraphic);

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setContent(content);
        dialogPane.getButtonTypes().addAll(buttons);

        if (resultConverter != null) {
            dialog.setResultConverter(param -> resultConverter.apply(param, controller));
        }
        if (inputConverter != null && input != null) {
            inputConverter.accept(controller, input);
        }

        Scene scene = SceneBuilder.getInstance().build(dialog.getDialogPane().getScene());

        if (useDefaultIcon) {
            ((Stage)scene.getWindow()).getIcons().add(new Image(getClass().getResourceAsStream("/images/XMC_512.png")));
        }

        return dialog;
    }
}

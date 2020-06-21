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
import javafx.util.Callback;
import org.apache.commons.lang3.tuple.Pair;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.util.ArrayList;
import java.util.List;

public class DialogBuilder<T> {
    public static DialogBuilder getInstance() { return new DialogBuilder(); }

    private MessageKey titleKey;
    private MessageKey headerTextKey;
    private Node content;
    private Node headerGraphic;
    private Callback<ButtonType, T> resultConverter;
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
        Pair<Parent, Object> component = FxmlComponentFactory.load(key);
        return withContent(component.getLeft());
    }

    public DialogBuilder addButton(MessageKey buttonTextKey, ButtonData buttonData) {
        buttons.add(new ButtonType(MessageAdapter.getByKey(buttonTextKey), buttonData));
        return this;
    }

    public DialogBuilder resultConverter(Callback<ButtonType, T> resultConverter) {
        this.resultConverter = resultConverter;
        return this;
    }

    public DialogBuilder withDefaultIcon() {
        this.useDefaultIcon = true;
        return this;
    }

    public Dialog<T> build() {
        Dialog<T> dialog = new Dialog<>();

        dialog.setTitle(MessageAdapter.getByKey(titleKey));
        dialog.setHeaderText(MessageAdapter.getByKey(headerTextKey));
        dialog.setGraphic(headerGraphic);

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.setContent(content);
        dialogPane.getButtonTypes().addAll(buttons);

        if (resultConverter != null) {
            dialog.setResultConverter(resultConverter);
        }

        Scene scene = SceneBuilder.getInstance().build(dialog.getDialogPane().getScene());
        SceneUtil.getOrCreateValidationSceneState(scene);

        if (useDefaultIcon) {
            ((Stage)scene.getWindow()).getIcons().add(new Image(getClass().getResourceAsStream("/images/XMC_512.png")));
        }

        return dialog;
    }
}

package org.xmc.fe.ui;

import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.Pair;
import org.xmc.Main;
import org.xmc.fe.FeConstants;
import org.xmc.fe.async.AsyncProcessor;
import org.xmc.fe.async.IAsyncCallable;
import org.xmc.fe.stages.main.MainController;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.util.ArrayList;
import java.util.List;

public class CustomDialogBuilder<CONTROLLER_TYPE, RETURN_TYPE, ASYNC_DATA_TYPE> {
    public static CustomDialogBuilder getInstance() { return new CustomDialogBuilder(); }

    private MessageKey titleKey;
    private MessageKey headerTextKey;
    private Node content;
    private Node headerGraphic;
    private IDialogMapper<CONTROLLER_TYPE, RETURN_TYPE> mapper;
    private CONTROLLER_TYPE controller;
    private RETURN_TYPE input;
    private List<ButtonType> buttons = new ArrayList<>();
    private boolean useDefaultIcon;
    private boolean showBackdrop = true;
    private IAsyncCallable<ASYNC_DATA_TYPE> asyncCallable;

    public CustomDialogBuilder titleKey(MessageKey titleKey) {
        this.titleKey = titleKey;
        return this;
    }

    public CustomDialogBuilder headerTextKey(MessageKey headerTextKey) {
        this.headerTextKey = headerTextKey;
        return this;
    }

    public CustomDialogBuilder headerGraphic(Node headerGraphic) {
        this.headerGraphic = headerGraphic;
        return this;
    }

    public CustomDialogBuilder showBackdrop(boolean showBackdrop) {
        this.showBackdrop = showBackdrop;
        return this;
    }

    public CustomDialogBuilder withContent(Node content) {
        this.content = content;
        return this;
    }

    public CustomDialogBuilder withFxmlContent(FxmlKey key) {
        Pair<Parent, CONTROLLER_TYPE> component = FxmlComponentFactory.load(key);
        this.controller = component.getRight();
        return withContent(component.getLeft());
    }

    public CustomDialogBuilder addButton(MessageKey buttonTextKey, ButtonData buttonData) {
        buttons.add(new ButtonType(MessageAdapter.getByKey(buttonTextKey), buttonData));
        return this;
    }

    public CustomDialogBuilder withMapper(IDialogMapper<CONTROLLER_TYPE, RETURN_TYPE> mapper) {
        this.mapper = mapper;
        return this;
    }

    public CustomDialogBuilder withInput(RETURN_TYPE input) {
        this.input = input;
        return this;
    }

    public CustomDialogBuilder withAsyncDataLoading(IAsyncCallable<ASYNC_DATA_TYPE> asyncCallable) {
        this.asyncCallable = asyncCallable;
        return this;
    }

    public CustomDialogBuilder withDefaultIcon() {
        this.useDefaultIcon = true;
        return this;
    }

    public Dialog<RETURN_TYPE> build() {
        Dialog<RETURN_TYPE> dialog = new Dialog<>();

        dialog.setTitle(MessageAdapter.getByKey(titleKey));
        dialog.setHeaderText(MessageAdapter.getByKey(headerTextKey));
        dialog.setGraphic(headerGraphic);

        DialogPane dialogPane = dialog.getDialogPane();
        dialogPane.getStylesheets().add(FeConstants.BASE_CSS_PATH);
        dialogPane.setContent(content);
        dialogPane.getButtonTypes().addAll(buttons);

        if (mapper != null) {
            dialog.setResultConverter(param -> mapper.apply(param.getButtonData(), controller));
        }
        if (mapper != null && input != null) {
            mapper.accept(controller, input);
        }

        Scene scene = SceneBuilder.getInstance().build(dialog.getDialogPane().getScene());

        if (useDefaultIcon) {
            ((Stage)scene.getWindow()).getIcons().add(FeConstants.APP_ICON);
        }

        if (showBackdrop) {
            dialog.setOnShown(event -> MainController.backdropRef.setVisible(true));
            dialog.setOnCloseRequest(event -> MainController.backdropRef.setVisible(false));
        }

        if (asyncCallable != null && controller != null && controller instanceof IDialogWithAsyncData) {
            Main.applicationContext.getBean(AsyncProcessor.class).runAsync(
                    () -> dialogPane.setDisable(true),
                    asyncCallable,
                    asyncData -> ((IDialogWithAsyncData<ASYNC_DATA_TYPE>)controller).acceptAsyncData(asyncData),
                    () -> dialogPane.setDisable(false)
            );
        }

        return dialog;
    }
}

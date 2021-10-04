package io.github.matwein.xmc.fe.ui;

import com.google.common.collect.Maps;
import io.github.matwein.xmc.fe.FeConstants;
import io.github.matwein.xmc.fe.async.AsyncProcessor;
import io.github.matwein.xmc.fe.async.IAsyncCallable;
import io.github.matwein.xmc.fe.common.MessageAdapter;
import io.github.matwein.xmc.fe.common.MessageAdapter.MessageKey;
import io.github.matwein.xmc.fe.common.XmcFrontendContext;
import io.github.matwein.xmc.fe.stages.main.MainController;
import io.github.matwein.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import javafx.event.ActionEvent;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class CustomDialogBuilder<CONTROLLER_TYPE, RETURN_TYPE, ASYNC_DATA_TYPE> {
    public static CustomDialogBuilder getInstance() { return new CustomDialogBuilder(); }

    private final List<ButtonType> buttons = new ArrayList<>();
    private final Map<ButtonType, BiConsumer<Dialog<RETURN_TYPE>, CONTROLLER_TYPE>> customButtonActions = Maps.newHashMap();
    
    private MessageKey titleKey;
    private MessageKey headerTextKey;
    private Node content;
    private IDialogMapper<CONTROLLER_TYPE, RETURN_TYPE> mapper;
    private CONTROLLER_TYPE controller;
    private RETURN_TYPE input;
    private IAsyncCallable<ASYNC_DATA_TYPE> asyncCallable;

    private CustomDialogBuilder() {
    }

    public CustomDialogBuilder titleKey(MessageKey titleKey) {
        this.titleKey = titleKey;
        return this;
    }

    public CustomDialogBuilder headerTextKey(MessageKey headerTextKey) {
        this.headerTextKey = headerTextKey;
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
    
    public CustomDialogBuilder addButton(
    		MessageKey buttonTextKey,
		    ButtonData buttonData,
		    BiConsumer<Dialog<RETURN_TYPE>, CONTROLLER_TYPE> onAction) {
    	
	    ButtonType buttonType = new ButtonType(MessageAdapter.getByKey(buttonTextKey), buttonData);
	    
	    buttons.add(buttonType);
	    customButtonActions.put(buttonType, onAction);
	    
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

    public Dialog<RETURN_TYPE> build() {
        Dialog<RETURN_TYPE> dialog = new Dialog<>();

        dialog.setTitle(MessageAdapter.getByKey(titleKey));
        dialog.setHeaderText(MessageAdapter.getByKey(headerTextKey));
	    dialog.initOwner(MainController.mainWindow);
	    dialog.initModality(Modality.WINDOW_MODAL);
	    
        DialogPane dialogPane = createDialogPane(dialog);
        dialogPane.getStylesheets().add(FeConstants.BASE_CSS_PATH);
        dialogPane.setContent(content);
        dialogPane.getButtonTypes().addAll(buttons);
        dialog.setDialogPane(dialogPane);

        if (controller instanceof IAfterInit castedController) {
            castedController.afterInitialize(dialog);
        }
        if (mapper != null) {
            dialog.setResultConverter(param -> mapper.apply(param == null ? null : param.getButtonData(), controller));
        }
        if (mapper != null && input != null) {
            mapper.accept(controller, input);
        }

        Scene scene = SceneBuilder.getInstance().build(dialog.getDialogPane().getScene());
        ((Stage)scene.getWindow()).getIcons().add(FeConstants.APP_ICON);

        showBackdrop(dialog);

        if (asyncCallable != null && controller != null && controller instanceof IDialogWithAsyncData castedController) {
            XmcFrontendContext.applicationContext.get().getBean(AsyncProcessor.class).runAsync(
                    () -> dialogPane.setDisable(true),
                    asyncCallable,
		            (Consumer<ASYNC_DATA_TYPE>) castedController::acceptAsyncData,
                    () -> dialogPane.setDisable(false)
            );
        }

        return dialog;
    }

    private DialogPane createDialogPane(Dialog<RETURN_TYPE> dialog) {
        return new DialogPane() {
            @Override
            protected Node createButton(ButtonType buttonType) {
                if (customButtonActions.containsKey(buttonType)) {
                    final Button button = new Button(buttonType.getText());
                    final ButtonData buttonData = buttonType.getButtonData();
                    ButtonBar.setButtonData(button, buttonData);
                    button.setDefaultButton(buttonData.isDefaultButton());
                    button.setCancelButton(buttonData.isCancelButton());
                    button.addEventHandler(ActionEvent.ACTION, ae -> {
                        if (ae.isConsumed()) return;
                        customButtonActions.get(buttonType).accept(dialog, controller);
                    });
                    return button;
                } else {
                    return super.createButton(buttonType);
                }
            }
        };
    }

    public static void showBackdrop(Dialog<?> dialog) {
        if (MainController.backdropRef != null && !MainController.backdropRef.isVisible()) {
            dialog.setOnShown(event -> MainController.backdropRef.setVisible(true));
            dialog.setOnCloseRequest(event -> MainController.backdropRef.setVisible(false));
        }
    }
}

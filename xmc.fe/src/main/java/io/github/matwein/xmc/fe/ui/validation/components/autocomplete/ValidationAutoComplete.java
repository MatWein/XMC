package io.github.matwein.xmc.fe.ui.validation.components.autocomplete;

import io.github.matwein.xmc.fe.FeConstants;
import io.github.matwein.xmc.fe.common.ReflectionUtil;
import io.github.matwein.xmc.fe.common.XmcFrontendContext;
import io.github.matwein.xmc.fe.ui.SceneUtil;
import io.github.matwein.xmc.fe.ui.components.FocusLostListener;
import io.github.matwein.xmc.fe.ui.validation.components.ValidationTextField;
import javafx.animation.PauseTransition;
import javafx.application.Platform;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.stage.Popup;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;

public class ValidationAutoComplete<T> extends ValidationTextField {
    private static final Set<KeyCode> KEYS_TO_IGNORE = Set.of(
            KeyCode.ESCAPE, KeyCode.LEFT, KeyCode.RIGHT, KeyCode.UP,
            KeyCode.DOWN, KeyCode.TAB);

    public static final double BUTTON_HEIGHT = 30.0;

    private final ScrollPane scrollPane;
    private final VBox vbox;
    private final Popup autoCompleteMenu;

    private String autoCompleteController;
    private int autoCompleteLimit = 50;
    private int visibleRowCount = 5;
    private Function<T, String> converter;
    private Function<T, String> contextMenuConverter;
    private Consumer<T> itemSelectedConsumer;

    public ValidationAutoComplete() {
        autoCompleteMenu = new Popup();

        vbox = new VBox();
        vbox.setFillWidth(true);
        vbox.setMaxWidth(Double.MAX_VALUE);

        scrollPane = new ScrollPane(vbox);
        scrollPane.setFitToHeight(true);
        scrollPane.setFitToWidth(true);

        autoCompleteMenu.getContent().add(scrollPane);
    }

    @Override
    public void initialize(Scene scene) {
        super.initialize(scene);

        PauseTransition pause = new PauseTransition(FeConstants.DEFAULT_DELAY);

        this.focusedProperty().addListener((FocusLostListener) autoCompleteMenu::hide);

        this.addEventHandler(KeyEvent.KEY_RELEASED, event -> {
            if (KEYS_TO_IGNORE.contains(event.getCode())) {
                return;
            }

            pause.setOnFinished(e -> search());
            pause.playFromStart();
        });

        scene.addEventFilter(MouseEvent.MOUSE_PRESSED, event -> autoCompleteMenu.hide());
        scene.getWindow().widthProperty().addListener(event -> autoCompleteMenu.hide());
        scene.getWindow().heightProperty().addListener(event -> autoCompleteMenu.hide());
        scene.getWindow().xProperty().addListener(event -> autoCompleteMenu.hide());
        scene.getWindow().yProperty().addListener(event -> autoCompleteMenu.hide());
    }

    private void search() {
        Platform.runLater(() -> {
	        Scene scene = getScene();
	        if (scene == null) {
		        return;
	        }
	        
            if (StringUtils.isBlank(getTextOrNull())) {
                autoCompleteMenu.hide();
                return;
            }

            List<T> results = getTypedAutoCompleteController().search(getTextOrNull(), autoCompleteLimit);
            if (results.isEmpty()) {
                autoCompleteMenu.hide();
                return;
            }

            List<Button> menuItems = results.stream()
                    .map(this::createMenuButton)
                    .collect(Collectors.toList());

            vbox.getChildren().setAll(menuItems);

            Point2D txtCoords = localToScene(0.0, 0.0);
	        
	        double x = txtCoords.getX() + scene.getX() + scene.getWindow().getX();
            double y = txtCoords.getY() + scene.getY() + scene.getWindow().getY() + getHeight();

            double maxWidth = getWidth();
            scrollPane.setPrefWidth(maxWidth);
            scrollPane.setMaxWidth(maxWidth);

            vbox.applyCss();
            vbox.layout();

            double maxHeight = (BUTTON_HEIGHT * visibleRowCount) + 3;
            scrollPane.setPrefHeight(maxHeight);
            scrollPane.setMinHeight(maxHeight);
            scrollPane.setMaxHeight(maxHeight);
            scrollPane.setVvalue(0.0);

            autoCompleteMenu.show(this.getScene().getWindow(), x, y);
            vbox.requestFocus();
        });
    }

    private Button createMenuButton(T item) {
        String text = contextMenuConverter == null ? Objects.toString(item) : contextMenuConverter.apply(item);

        Button button = new Button(text);

        button.setMaxWidth(Double.MAX_VALUE);
        button.setTextAlignment(TextAlignment.LEFT);
        button.setAlignment(Pos.CENTER_LEFT);
        button.setUserData(item);
        button.setMinHeight(BUTTON_HEIGHT);
        button.setPrefHeight(BUTTON_HEIGHT);
        button.setMaxHeight(BUTTON_HEIGHT);

        button.setOnAction(event -> {
            selectItem(item);

            if (itemSelectedConsumer != null) {
                itemSelectedConsumer.accept(item);
            }
        });

        return button;
    }

    public void selectItem(T item) {
        String selectedText = converter == null ? Objects.toString(item) : converter.apply(item);
        setText(selectedText);
        SceneUtil.getOrCreateValidationSceneState(getScene()).validate();
        autoCompleteMenu.hide();
    }

    private IAutoCompleteController<T> getTypedAutoCompleteController() {
        Class<?> controllerType = ReflectionUtil.forName(autoCompleteController);
        return (IAutoCompleteController<T>) XmcFrontendContext.createNewInstanceFactory().call(controllerType);
    }

    public String getAutoCompleteController() {
        return autoCompleteController;
    }

    public void setAutoCompleteController(String autoCompleteController) {
        this.autoCompleteController = autoCompleteController;
    }

    public int getAutoCompleteLimit() {
        return autoCompleteLimit;
    }

    public void setAutoCompleteLimit(int autoCompleteLimit) {
        this.autoCompleteLimit = autoCompleteLimit;
    }

    public Function<T, String> getConverter() {
        return converter;
    }

    public void setConverter(Function<T, String> converter) {
        this.converter = converter;
    }

    public int getVisibleRowCount() {
        return visibleRowCount;
    }

    public void setVisibleRowCount(int visibleRowCount) {
        this.visibleRowCount = visibleRowCount;
    }

    public Function<T, String> getContextMenuConverter() {
        return contextMenuConverter;
    }

    public void setContextMenuConverter(Function<T, String> contextMenuConverter) {
        this.contextMenuConverter = contextMenuConverter;
    }

    public Consumer<T> getItemSelectedConsumer() {
        return itemSelectedConsumer;
    }

    public void setItemSelectedConsumer(Consumer<T> itemSelectedConsumer) {
        this.itemSelectedConsumer = itemSelectedConsumer;
    }
}

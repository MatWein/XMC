package io.github.matwein.xmc.fe.ui;

import io.github.matwein.xmc.fe.FeConstants;
import io.github.matwein.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import io.github.matwein.xmc.utils.MessageAdapter;
import io.github.matwein.xmc.utils.MessageAdapter.MessageKey;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.Pair;

import java.awt.*;

public class StageBuilder {
    public static StageBuilder getInstance() { return new StageBuilder(); }

    private Scene scene;
    private boolean useDefaultIcon;
    private MessageKey titleKey;
    private boolean resizable;
    private boolean maximized;
    private Dimension minSize;
    private Object controller;
    private Object input;

    private StageBuilder() {
    }

    public StageBuilder withDefaultIcon() {
        this.useDefaultIcon = true;
        return this;
    }

    public StageBuilder withDefaultTitleKey() {
        this.titleKey = MessageKey.APP_NAME;
        return this;
    }

    public StageBuilder withTitleKey(MessageKey titleKey) {
        this.titleKey = titleKey;
        return this;
    }

    public StageBuilder withInput(Object input) {
        this.input = input;
        return this;
    }

    public StageBuilder withSceneComponent(Parent component) {
        this.scene = SceneBuilder.getInstance()
                .withRoot(component)
                .build();
        return this;
    }

    public StageBuilder withFxmlSceneComponent(FxmlKey key) {
        Pair<Parent, Object> component = FxmlComponentFactory.load(key);
        this.controller = component.getRight();
        return withSceneComponent(component.getLeft());
    }

    public StageBuilder withScene(Scene scene) {
        this.scene = scene;
        return this;
    }

    public StageBuilder resizable(boolean resizable) {
        this.resizable = resizable;
        return this;
    }

    public StageBuilder maximized(boolean maximized) {
        this.maximized = maximized;
        return this;
    }

    public StageBuilder minSize(int width, int height) {
        this.minSize = new Dimension(width, height);
        return this;
    }

    public Stage build() {
        Stage stage = new Stage();
        return build(stage);
    }

    public Stage build(Stage existingStage) {
        existingStage.setScene(SceneBuilder.getInstance().build(scene));
        existingStage.setResizable(resizable);
        existingStage.setMaximized(maximized);
        existingStage.setTitle(MessageAdapter.getByKey(titleKey));

        if (useDefaultIcon) {
            existingStage.getIcons().add(FeConstants.APP_ICON);
        }

        if (minSize != null) {
            existingStage.setMinWidth(minSize.getWidth());
            existingStage.setMinHeight(minSize.getHeight());
        }

        if (input != null && controller instanceof IAfterInit) {
            ((IAfterInit)controller).afterInitialize(input);
        }
        
        if (controller instanceof IAfterStageShown) {
        	existingStage.setOnShown(windowEvent -> ((IAfterStageShown)controller).afterStageShown(input));
        }

        return existingStage;
    }
}

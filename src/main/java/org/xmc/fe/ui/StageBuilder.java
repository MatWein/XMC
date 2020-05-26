package org.xmc.fe.ui;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.apache.commons.lang3.tuple.Pair;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.MessageAdapter.MessageKey;
import org.xmc.fe.ui.validation.ValidationScene;

import java.awt.*;

public class StageBuilder {
    public static StageBuilder getInstance() { return new StageBuilder(); }

    private ValidationScene scene;
    private boolean useDefaultIcon;
    private MessageKey titleKey;
    private boolean resizable;
    private boolean maximized;
    private Dimension minSize;

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

    public StageBuilder withSceneComponent(Parent component) {
        this.scene = new ValidationScene(component);
        return this;
    }

    public StageBuilder withFxmlSceneComponent(FxmlKey key) {
        Pair<Parent, Object> component = FxmlComponentFactory.load(key);
        return withSceneComponent(component.getLeft());
    }

    public StageBuilder withScene(ValidationScene scene) {
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
        existingStage.setScene(scene);
        existingStage.setResizable(resizable);
        existingStage.setMaximized(maximized);

        if (useDefaultIcon) {
            existingStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/XMC_512.png")));
        }

        if (titleKey != null) {
            existingStage.setTitle(MessageAdapter.getByKey(titleKey));
        }

        if (minSize != null) {
            existingStage.setMinWidth(minSize.getWidth());
            existingStage.setMinHeight(minSize.getHeight());
        }

        return existingStage;
    }
}

package org.xmc.fe.ui;

import javafx.scene.Parent;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import org.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

public class StageBuilder {
    public static StageBuilder getInstance() { return new StageBuilder(); }

    private JMetroScene scene;
    private boolean useDefaultIcon;
    private MessageKey titleKey;
    private boolean resizable;

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
        this.scene = new JMetroScene(component);
        return this;
    }

    public StageBuilder withFxmlSceneComponent(FxmlKey key) {
        Parent component = FxmlComponentFactory.load(key);
        return withSceneComponent(component);
    }

    public StageBuilder withScene(JMetroScene scene) {
        this.scene = scene;
        return this;
    }

    public StageBuilder resizable(boolean resizable) {
        this.resizable = resizable;
        return this;
    }

    public Stage build() {
        Stage stage = new Stage();
        return build(stage);
    }

    public Stage build(Stage existingStage) {
        existingStage.setScene(scene);
        existingStage.setResizable(resizable);

        if (useDefaultIcon) {
            existingStage.getIcons().add(new Image(getClass().getResourceAsStream("/images/XMC_512.png")));
        }

        if (titleKey != null) {
            existingStage.setTitle(MessageAdapter.getByKey(titleKey));
        }

        return existingStage;
    }
}

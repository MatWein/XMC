package org.xmc.fe.ui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class SceneBuilder {
    private static final Style WINDOW_STYLE = Style.LIGHT;

    public static SceneBuilder getInstance() { return new SceneBuilder(); }

    private Parent root;

    public SceneBuilder withRoot(Parent root) {
        this.root = root;
        return this;
    }

    public Scene build() {
        Scene scene = new Scene(root);
        return build(scene);
    }

    public Scene build(Scene existingScene) {
        SceneUtil.getOrCreateValidationSceneState(existingScene);

        applyTheme(existingScene);

        return existingScene;
    }

    private static void applyTheme(Scene scene) {
        JMetro jMetro = new JMetro(scene, WINDOW_STYLE);
        jMetro.reApplyTheme();
    }
}

package org.xmc.fe.ui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;
import org.xmc.common.SystemProperties;

public class SceneBuilder {
	private static final String CLASS_ROOT_LIGHT = "root-light";
	private static final String CLASS_ROOT_DARK = "root-dark";
	
	public static SceneBuilder getInstance() { return new SceneBuilder(); }

    private Parent root;

    private SceneBuilder() {
    }

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
	    Style style = getStyle();
	
	    JMetro jMetro = new JMetro(scene, style);
        jMetro.reApplyTheme();
	
	    if (style == Style.LIGHT) {
		    scene.getRoot().getStyleClass().remove(CLASS_ROOT_DARK);
		    scene.getRoot().getStyleClass().add(CLASS_ROOT_LIGHT);
	    } else if (style == Style.DARK) {
	        scene.getRoot().getStyleClass().remove(CLASS_ROOT_LIGHT);
	        scene.getRoot().getStyleClass().add(CLASS_ROOT_DARK);
        }
    }
	
	public static Style getStyle() {
		String styleProperty = System.getProperty(SystemProperties.XMC_STYLE);
		return "dark".equalsIgnoreCase(styleProperty) ? Style.DARK : Style.LIGHT;
	}
}

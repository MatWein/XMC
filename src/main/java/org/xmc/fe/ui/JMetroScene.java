package org.xmc.fe.ui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import jfxtras.styles.jmetro.JMetro;
import jfxtras.styles.jmetro.Style;

public class JMetroScene extends Scene {
    public JMetroScene(Parent component) {
        super(component);

        JMetro jMetro = new JMetro(this, Style.LIGHT);
        jMetro.reApplyTheme();
    }
}

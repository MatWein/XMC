package io.github.matwein.xmc.fe.ui;

import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import io.github.matwein.xmc.JUnitTestBase;

class StageBuilderTest extends JUnitTestBase {
    @Test
    void testBuild() throws Throwable {
        runJavaFxCode(() -> {
            Stage result = StageBuilder.getInstance()
                    .resizable(true)
                    .withDefaultIcon()
                    .withSceneComponent(new AnchorPane())
                    .maximized(false)
                    .minSize(100, 100)
                    .withDefaultTitleKey()
                    .build();

            Assertions.assertNotNull(result);
        });
    }
}
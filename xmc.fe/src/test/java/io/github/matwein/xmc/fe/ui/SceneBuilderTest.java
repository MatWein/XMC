package io.github.matwein.xmc.fe.ui;

import io.github.matwein.xmc.fe.JavaFxTestBase;
import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class SceneBuilderTest extends JavaFxTestBase {
    @Test
    void testBuild() throws Throwable {
        runJavaFxCode(() -> {
            Scene result = SceneBuilder.getInstance()
                    .withRoot(new AnchorPane())
                    .build();

            Assertions.assertNotNull(result);
        });
    }
}
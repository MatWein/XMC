package org.xmc.fe.ui;

import javafx.scene.Scene;
import javafx.scene.layout.AnchorPane;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xmc.JUnitTestBase;

class SceneBuilderTest extends JUnitTestBase {
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
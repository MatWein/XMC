package io.github.matwein.xmc.fe.ui;

import io.github.matwein.xmc.fe.JavaFxTestBase;
import io.github.matwein.xmc.fe.ui.FxmlComponentFactory.FxmlKey;
import javafx.scene.Parent;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class FxmlComponentFactoryTest extends JavaFxTestBase {
    private static final Logger LOGGER = LoggerFactory.getLogger(FxmlComponentFactoryTest.class);

    @Test
    void testCreateAllFxmlComponents() throws Throwable {
        runJavaFxCode(() -> {
            for (FxmlKey fxmlKey : FxmlKey.values()) {
                LOGGER.debug("Creating component for '{}'.", fxmlKey);

                Pair<Parent, Object> component = FxmlComponentFactory.load(fxmlKey);
                Assertions.assertNotNull(component.getLeft());
            }
        });
    }
}
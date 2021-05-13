package io.github.matwein.xmc.fe.ui;

import io.github.matwein.xmc.fe.JavaFxTestBase;
import javafx.stage.FileChooser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DialogHelperTest extends JavaFxTestBase {
    @Test
    void testCreateFileChooser() throws Throwable {
        runJavaFxCode(() -> {
            FileChooser result = DialogHelper.createFileChooser(ExtensionFilterType.ALL);
            Assertions.assertNotNull(result);
        });
    }
}
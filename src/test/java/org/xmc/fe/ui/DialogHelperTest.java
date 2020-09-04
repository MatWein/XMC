package org.xmc.fe.ui;

import javafx.stage.FileChooser;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.xmc.JUnitTestBase;

class DialogHelperTest extends JUnitTestBase {
    @Test
    void testCreateFileChooser() throws Throwable {
        runJavaFxCode(() -> {
            FileChooser result = DialogHelper.createFileChooser(ExtensionFilterType.ALL);
            Assert.assertNotNull(result);
        });
    }
}
package org.xmc.fe.ui;

import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Dialog;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.xmc.be.IntegrationTest;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

class CustomDialogBuilderTest extends IntegrationTest {
    @Test
    void testBuild() throws Throwable {
        runJavaFxCode(() -> {
            Dialog result = CustomDialogBuilder.getInstance()
                    .addButton(MessageKey.DIALOG_OK, ButtonData.OK_DONE)
                    .titleKey(MessageKey.APP_NAME)
                    .withFxmlContent(FxmlComponentFactory.FxmlKey.CASH_ACCOUNT_EDIT)
                    .headerTextKey(MessageKey.APP_NAME)
                    .build();

            Assert.assertNotNull(result);
        });
    }
}
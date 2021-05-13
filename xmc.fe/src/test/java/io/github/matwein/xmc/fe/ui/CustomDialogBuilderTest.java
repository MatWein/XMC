package io.github.matwein.xmc.fe.ui;

import io.github.matwein.xmc.fe.JavaFxTestBase;
import io.github.matwein.xmc.utils.MessageAdapter.MessageKey;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.Dialog;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class CustomDialogBuilderTest extends JavaFxTestBase {
    @Test
    void testBuild() throws Throwable {
        runJavaFxCode(() -> {
            Dialog result = CustomDialogBuilder.getInstance()
                    .addButton(MessageKey.DIALOG_OK, ButtonData.OK_DONE)
                    .titleKey(MessageKey.APP_NAME)
                    .withFxmlContent(FxmlComponentFactory.FxmlKey.CASH_ACCOUNT_EDIT)
                    .headerTextKey(MessageKey.APP_NAME)
                    .build();

            Assertions.assertNotNull(result);
        });
    }
}
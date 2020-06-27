package org.xmc.fe.ui;

import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xmc.JUnitTestBase;
import org.xmc.fe.ui.MessageAdapter.MessageKey;

import java.util.Locale;

class MessageAdapterTest extends JUnitTestBase {
    @BeforeEach
    void setUp() {
        Locale.setDefault(Locale.GERMANY);
        MessageAdapter.initBundle();
    }

    @Test
    void testGetByKey() {
        String result = MessageAdapter.getByKey(MessageKey.APP_NAME);
        Assert.assertEquals("XMC", result);
    }

    @Test
    void testGetByKey_Kanguage() {
        Locale.setDefault(Locale.GERMANY);
        MessageAdapter.initBundle();

        String result = MessageAdapter.getByKey(MessageKey.CASHACCOUNT_EDIT_CANCEL);
        Assert.assertEquals("Abbrechen", result);

        Locale.setDefault(Locale.ENGLISH);
        MessageAdapter.initBundle();

        result = MessageAdapter.getByKey(MessageKey.CASHACCOUNT_EDIT_CANCEL);
        Assert.assertEquals("Cancel", result);
    }

    @Test
    void testGetByKey_Params() {
        String result = MessageAdapter.getByKey(MessageKey.VALIDATION_MIN_LENGTH, 10);
        Assert.assertEquals("Mindestens 10 Zeichen erforderlich.", result);
    }
}
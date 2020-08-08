package org.xmc.common.utils;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.xmc.JUnitTestBase;

import java.text.ParseException;
import java.util.Locale;

class NumberUtilsTest extends JUnitTestBase {
    @Test
    void testParseDoubleValue() throws ParseException {
        Locale.setDefault(Locale.GERMANY);

        Assert.assertEquals(0.0, NumberUtils.parseDoubleValue("0"), 0.001);
        Assert.assertEquals(0.01, NumberUtils.parseDoubleValue("0,01"), 0.001);
        Assert.assertEquals(25.95, NumberUtils.parseDoubleValue("25,95"), 0.001);
        Assert.assertEquals(2125.99, NumberUtils.parseDoubleValue("2125,99"), 0.001);
        Assert.assertEquals(2125.99, NumberUtils.parseDoubleValue("2.125,99"), 0.001);
        Assert.assertEquals(12125.99, NumberUtils.parseDoubleValue(" 12.125,99 "), 0.001);
    }

    @Test
    void testParseDoubleValue_LocaleEnglish() throws ParseException {
        Locale.setDefault(Locale.ENGLISH);

        Assert.assertEquals(0.0, NumberUtils.parseDoubleValue("0"), 0.001);
        Assert.assertEquals(0.01, NumberUtils.parseDoubleValue("0.01"), 0.001);
        Assert.assertEquals(25.95, NumberUtils.parseDoubleValue("25.95"), 0.001);
        Assert.assertEquals(2125.99, NumberUtils.parseDoubleValue("2125.99"), 0.001);
        Assert.assertEquals(2125.99, NumberUtils.parseDoubleValue("2,125.99"), 0.001);
        Assert.assertEquals(12125.99, NumberUtils.parseDoubleValue(" 12,125.99 "), 0.001);
    }

    @Test
    void testParseDoubleValue_InvalidChars() {
        Locale.setDefault(Locale.GERMANY);

        Assert.assertThrows(ParseException.class, () -> NumberUtils.parseDoubleValue("0,99 €"));
    }
}
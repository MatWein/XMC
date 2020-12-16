package org.xmc.common.utils;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.xmc.JUnitTestBase;

import java.text.ParseException;
import java.util.Locale;

class NumberUtilsTest extends JUnitTestBase {
    @Test
    void testParseDoubleValue() throws ParseException {
        Locale.setDefault(Locale.GERMANY);

        Assertions.assertEquals(0.0, NumberUtils.parseDoubleValue("0"), 0.001);
        Assertions.assertEquals(0.01, NumberUtils.parseDoubleValue("0,01"), 0.001);
        Assertions.assertEquals(25.95, NumberUtils.parseDoubleValue("25,95"), 0.001);
        Assertions.assertEquals(2125.99, NumberUtils.parseDoubleValue("2125,99"), 0.001);
        Assertions.assertEquals(2125.99, NumberUtils.parseDoubleValue("2.125,99"), 0.001);
        Assertions.assertEquals(-0.99, NumberUtils.parseDoubleValue("-0,99"), 0.001);
        Assertions.assertEquals(12125.99, NumberUtils.parseDoubleValue(" 12.125,99 "), 0.001);
    }

    @Test
    void testParseDoubleValue_LocaleEnglish() throws ParseException {
        Locale.setDefault(Locale.ENGLISH);

        Assertions.assertEquals(0.0, NumberUtils.parseDoubleValue("0"), 0.001);
        Assertions.assertEquals(0.01, NumberUtils.parseDoubleValue("0.01"), 0.001);
        Assertions.assertEquals(25.95, NumberUtils.parseDoubleValue("25.95"), 0.001);
        Assertions.assertEquals(2125.99, NumberUtils.parseDoubleValue("2125.99"), 0.001);
        Assertions.assertEquals(2125.99, NumberUtils.parseDoubleValue("2,125.99"), 0.001);
        Assertions.assertEquals(12125.99, NumberUtils.parseDoubleValue(" 12,125.99 "), 0.001);
    }

    @Test
    void testParseDoubleValue_InvalidChars() {
        Locale.setDefault(Locale.GERMANY);

        Assertions.assertThrows(ParseException.class, () -> NumberUtils.parseDoubleValue("0,99 €"));
    }
}
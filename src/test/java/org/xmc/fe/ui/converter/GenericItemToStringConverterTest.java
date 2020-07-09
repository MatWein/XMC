package org.xmc.fe.ui.converter;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.xmc.JUnitTestBase;
import org.xmc.common.stubs.bank.DtoBank;

class GenericItemToStringConverterTest extends JUnitTestBase {
    @Test
    void testToStringAndFromString() {
        var converter = GenericItemToStringConverter.getInstance(DtoBank::getName);

        DtoBank dtoBank = new DtoBank();
        dtoBank.setName("testbank");

        String result = converter.toString(dtoBank);
        Assert.assertSame(dtoBank.getName(), result);

        DtoBank result2 = converter.fromString(dtoBank.getName());
        Assert.assertSame(dtoBank, result2);
    }

    @Test
    void testToStringAndFromString_NotInCache() {
        var converter = GenericItemToStringConverter.getInstance(DtoBank::getName);

        DtoBank dtoBank = new DtoBank();
        dtoBank.setName("testbank");

        DtoBank result = converter.fromString(dtoBank.getName());
        Assert.assertNull(result);
    }

    @Test
    void testToString_Null() {
        var converter = GenericItemToStringConverter.getInstance(DtoBank::getName);

        String result = converter.toString(null);
        Assert.assertNull(result);
    }
}
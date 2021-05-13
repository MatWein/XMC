package io.github.matwein.xmc.fe.ui.converter;

import io.github.matwein.xmc.JUnitTestBase;
import io.github.matwein.xmc.common.stubs.bank.DtoBank;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class GenericItemToStringConverterTest extends JUnitTestBase {
    @Test
    void testToStringAndFromString() {
        var converter = GenericItemToStringConverter.getInstance(DtoBank::getName);

        DtoBank dtoBank = new DtoBank();
        dtoBank.setName("testbank");

        String result = converter.toString(dtoBank);
        Assertions.assertSame(dtoBank.getName(), result);

        DtoBank result2 = converter.fromString(dtoBank.getName());
        Assertions.assertSame(dtoBank, result2);
    }

    @Test
    void testToStringAndFromString_NotInCache() {
        var converter = GenericItemToStringConverter.getInstance(DtoBank::getName);

        DtoBank dtoBank = new DtoBank();
        dtoBank.setName("testbank");

        DtoBank result = converter.fromString(dtoBank.getName());
        Assertions.assertNull(result);
    }

    @Test
    void testToString_Null() {
        var converter = GenericItemToStringConverter.getInstance(DtoBank::getName);

        String result = converter.toString(null);
        Assertions.assertNull(result);
    }
}
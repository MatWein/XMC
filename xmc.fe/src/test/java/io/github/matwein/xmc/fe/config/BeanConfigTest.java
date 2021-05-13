package io.github.matwein.xmc.fe.config;

import io.github.matwein.xmc.fe.IntegrationTest;
import io.github.matwein.xmc.fe.common.dtos.DtoBankInformation;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Currency;
import java.util.List;

class BeanConfigTest extends IntegrationTest {
    @Autowired
    private List<DtoBankInformation> bankInformation;

    @Autowired
    private List<Currency> currencies;

    @Test
    void testBankInformation() {
        Assertions.assertEquals(4892, bankInformation.size());
    }

    @Test
    void testCurrencies() {
        Assertions.assertTrue(currencies.size() > 200);
        Assertions.assertTrue(currencies.contains(Currency.getInstance("EUR")));
        Assertions.assertTrue(currencies.contains(Currency.getInstance("USD")));
    }
}
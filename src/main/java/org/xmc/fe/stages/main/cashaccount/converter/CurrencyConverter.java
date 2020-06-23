package org.xmc.fe.stages.main.cashaccount.converter;

import org.springframework.stereotype.Component;

import java.util.Currency;
import java.util.function.Function;

@Component
public class CurrencyConverter implements Function<Currency, String> {
    @Override
    public String apply(Currency currency) {
        return String.format("[%s] %s", currency.getCurrencyCode(), currency.getDisplayName());
    }
}

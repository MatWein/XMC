package org.xmc.fe.stages.main.cashaccount.autocomplete;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.fe.stages.main.cashaccount.converter.CurrencyConverter;
import org.xmc.fe.ui.validation.components.IAutoCompleteController;

import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class CurrencyAutoCompleteController implements IAutoCompleteController<Currency> {
    private final List<Currency> currencies;
    private final CurrencyConverter currencyConverter;

    @Autowired
    public CurrencyAutoCompleteController(
            List<Currency> currencies,
            CurrencyConverter currencyConverter) {

        this.currencies = currencies;
        this.currencyConverter = currencyConverter;
    }

    @Override
    public List<Currency> search(String searchValue, int limit) {
        return currencies.stream()
                .filter(currency -> matches(searchValue, currency))
                .limit(limit)
                .collect(Collectors.toList());
    }

    private boolean matches(String searchValue, Currency currency) {
        String text = currencyConverter.apply(currency);
        return StringUtils.containsIgnoreCase(text, searchValue);
    }
}

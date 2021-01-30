package org.xmc.fe.ui.autocomplete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.fe.ui.converter.CurrencyConverter;
import org.xmc.fe.ui.validation.components.autocomplete.AutoCompleteByConverterController;

import java.util.Currency;
import java.util.List;

@Component
public class CurrencyAutoCompleteController extends AutoCompleteByConverterController<Currency> {
    @Autowired
    public CurrencyAutoCompleteController(
            List<Currency> currencies,
            CurrencyConverter currencyConverter) {

        super(currencies, currencyConverter);
    }
}

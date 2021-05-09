package io.github.matwein.xmc.fe.ui.autocomplete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import io.github.matwein.xmc.fe.ui.converter.CurrencyConverter;
import io.github.matwein.xmc.fe.ui.validation.components.autocomplete.AutoCompleteByConverterController;

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

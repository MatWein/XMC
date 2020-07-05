package org.xmc.fe.stages.main.cashaccount.autocomplete;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.xmc.fe.stages.main.cashaccount.converter.CurrencyConverter;
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

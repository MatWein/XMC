package org.xmc.be.services.currency;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class CurrencyService {
    public List<Currency> loadAllCurrencies() {
        return Currency.getAvailableCurrencies().stream()
                .sorted(Comparator.comparing(Currency::toString, Comparator.naturalOrder()))
                .collect(Collectors.toList());
    }
}

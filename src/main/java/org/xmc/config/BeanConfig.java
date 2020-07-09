package org.xmc.config;

import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.xmc.common.stubs.bank.DtoBankInformation;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class BeanConfig {
    @Bean
    public List<DtoBankInformation> bankInformation() throws IOException {
        try (var inputStreamReader = new InputStreamReader(getClass().getResourceAsStream("/blz-data.csv"))) {
            return new CsvToBeanBuilder<DtoBankInformation>(inputStreamReader)
                    .withType(DtoBankInformation.class)
                    .withSeparator(';')
                    .withSkipLines(1)
                    .build()
                    .parse();
        }
    }

    @Bean
    public List<Currency> currencies() {
        return Currency.getAvailableCurrencies().stream()
                .sorted(Comparator.comparing(Currency::toString, Comparator.naturalOrder()))
                .collect(Collectors.toList());
    }
}

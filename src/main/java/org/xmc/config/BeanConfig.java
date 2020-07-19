package org.xmc.config;

import com.opencsv.bean.CsvToBeanBuilder;
import org.springframework.boot.autoconfigure.info.ProjectInfoAutoConfiguration;
import org.springframework.boot.autoconfigure.info.ProjectInfoProperties;
import org.springframework.boot.info.BuildProperties;
import org.springframework.boot.info.GitProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.xmc.common.annotations.ConditionalOnMissingResource;
import org.xmc.common.stubs.bank.DtoBankInformation;

import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Comparator;
import java.util.Currency;
import java.util.List;
import java.util.Properties;
import java.util.stream.Collectors;

@Configuration
@Order(Ordered.LOWEST_PRECEDENCE)
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

    @Bean
    @ConditionalOnMissingResource(resources = "classpath:git.properties")
    public GitProperties gitProperties() {
        Properties properties = new Properties();
        properties.setProperty("branch", "development");
        properties.setProperty("commit.id", "development");
        properties.setProperty("commit.time", String.valueOf((int)(System.currentTimeMillis() / 1000)));

        return new GitProperties(properties);
    }

    @Bean
    @ConditionalOnMissingResource(resources = "classpath:META-INF/build-info.properties")
    public BuildProperties buildProperties() {
        Properties properties = new Properties();
        properties.setProperty("version", "development");

        return new BuildProperties(properties);
    }

    public static String loadVersionWithoutSprintContext() {
        try {
            return new ProjectInfoAutoConfiguration(new ProjectInfoProperties()).buildProperties().getVersion();
        } catch (Exception e) {
            return new BeanConfig().buildProperties().getVersion();
        }
    }
}

package org.xmc.fe.config;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.xmc.fe.Main;

import javax.sql.DataSource;

@Configuration
@EnableJpaRepositories(basePackageClasses = Main.class)
public class DynamicDataSourceConfig {
    public static String url;
    public static String username;
    public static String password;

    @Bean
    public DataSource dataSource() {
        DataSourceBuilder<?> dataSourceBuilder = DataSourceBuilder.create();

        dataSourceBuilder.url(url);
        dataSourceBuilder.username(username);
        dataSourceBuilder.password(password);

        return dataSourceBuilder.build();
    }
}

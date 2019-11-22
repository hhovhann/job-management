package com.hhovhann.jobmanagement.configuration;

import org.flywaydb.core.Flyway;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class FlywayConfiguration {
    public static final String FLYWAY = "flyway";
    private final DataSource dataSource;
    public FlywayConfiguration(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    @Bean(name = FLYWAY)
    public Flyway dataSource() {
        var flyway = Flyway.configure().dataSource(dataSource).load();
        flyway.migrate();
        return flyway;
    }
}

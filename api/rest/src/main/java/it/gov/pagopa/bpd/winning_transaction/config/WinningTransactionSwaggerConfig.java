package it.gov.pagopa.bpd.winning_transaction.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;

@Configuration
public class WinningTransactionSwaggerConfig {

    @Configuration
    @Profile("swaggerIT")
    @PropertySource({"classpath:/swagger/swagger_it_IT.properties",
            "classpath:/swagger/profile.properties"})

    public static class itConfig {

    }
}

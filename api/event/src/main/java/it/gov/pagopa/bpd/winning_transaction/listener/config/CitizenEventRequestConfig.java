package it.gov.pagopa.bpd.winning_transaction.listener.config;

import it.gov.pagopa.bpd.winning_transaction.listener.OnTransactionSaveRequestListener;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration class for {@link OnTransactionSaveRequestListener}
 */

@Configuration
@PropertySource("classpath:config/citizenEventRequestListener.properties")
public class CitizenEventRequestConfig {
}

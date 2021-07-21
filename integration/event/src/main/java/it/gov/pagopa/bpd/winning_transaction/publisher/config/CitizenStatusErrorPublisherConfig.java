package it.gov.pagopa.bpd.winning_transaction.publisher.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

/**
 * Configuration class for the TransactionErrorPublisherConnector
 */

@Configuration
@PropertySource("classpath:config/citizenStatusErrorPublisher.properties")
public class CitizenStatusErrorPublisherConfig {
}

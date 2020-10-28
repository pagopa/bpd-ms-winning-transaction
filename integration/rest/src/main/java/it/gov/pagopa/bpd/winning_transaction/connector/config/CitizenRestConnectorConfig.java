package it.gov.pagopa.bpd.winning_transaction.connector.config;

import it.gov.pagopa.bpd.common.connector.config.RestConnectorConfig;
import it.gov.pagopa.bpd.winning_transaction.connector.CitizenRestClient;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Import(RestConnectorConfig.class)
@EnableFeignClients(clients = CitizenRestClient.class)
@PropertySource("classpath:config/rest-client.properties")
public class CitizenRestConnectorConfig {
}

package it.gov.pagopa.bpd.winning_transaction.service.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:config/winningTransactionService.properties")
public class WinningTransactionServiceConfig {
}

package it.gov.pagopa.bpd.winning_transaction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.session.SessionAutoConfiguration;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication(exclude = {ErrorMvcAutoConfiguration.class, SessionAutoConfiguration.class})
@ComponentScan(basePackages = {"eu.sia.meda", "it.gov.pagopa.bpd"})
public class BpdWinningTransactionApplication {

	public static void main(String[] args) {
		SpringApplication.run(BpdWinningTransactionApplication.class, args);
	}

}

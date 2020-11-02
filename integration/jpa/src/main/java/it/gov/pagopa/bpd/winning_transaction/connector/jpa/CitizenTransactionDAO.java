package it.gov.pagopa.bpd.winning_transaction.connector.jpa;

import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.CitizenWinningTransaction;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CitizenTransactionDAO {

    List<CitizenWinningTransaction> findTransactionByFiscalCode(String fiscalCode, Long awardPeriodId);

}

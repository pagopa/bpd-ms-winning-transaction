package it.gov.pagopa.bpd.winning_transaction.connector.jpa;

import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.CitizenWinningTransaction;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
public class CitizenTransactionDAOImpl implements CitizenTransactionDAO {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<CitizenWinningTransaction> findTransactionByFiscalCode(String fiscalCode, Long awardPeriodId) {
        String QUERY_GET_TRANSACTION = "SELECT * FROM bpd_winning_transaction.get_citizen_transactions('" + fiscalCode + "', " + awardPeriodId + ")";
        //noinspection unchecked
        return em.createNativeQuery(QUERY_GET_TRANSACTION, CitizenWinningTransaction.class).getResultList();
    }
}

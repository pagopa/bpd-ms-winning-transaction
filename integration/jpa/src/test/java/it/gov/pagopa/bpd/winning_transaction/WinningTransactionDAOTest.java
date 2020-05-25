package it.gov.pagopa.bpd.winning_transaction;

import eu.sia.meda.layers.connector.query.CriteriaQuery;
import it.gov.pagopa.bpd.common.BaseCrudJpaDAOTest;
import it.gov.pagopa.bpd.winning_transaction.model.entity.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.model.entity.WinningTransactionId;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;
import java.util.function.Function;

public class WinningTransactionDAOTest extends
        BaseCrudJpaDAOTest<WinningTransactionDAO, WinningTransaction, WinningTransactionId> {

    @Data
    private static class WinningTransactionCriteria implements CriteriaQuery<WinningTransaction> {
        String idTrxAcquirer;
        String acquirerCode;
        OffsetDateTime trxDate;
    }


    @Autowired
    private WinningTransactionDAO dao;


    @Override
    protected CriteriaQuery<? super WinningTransaction> getMatchAlreadySavedCriteria() {
        WinningTransactionDAOTest.WinningTransactionCriteria criteriaQuery =
                new WinningTransactionDAOTest.WinningTransactionCriteria();
        criteriaQuery.setAcquirerCode(getStoredId().getAcquirerCode());
        criteriaQuery.setIdTrxAcquirer(getStoredId().getIdTrxAcquirer());
        criteriaQuery.setTrxDate(getStoredId().getTrxDate());

        return criteriaQuery;
    }


    @Override
    protected WinningTransactionDAO getDao() {
        return dao;
    }


    @Override
    protected void setId(WinningTransaction entity, WinningTransactionId id) {
        entity.setAcquirerCode(id.getAcquirerCode());
        entity.setTrxDate(id.getTrxDate());
        entity.setIdTrxAcquirer(id.getIdTrxAcquirer());
    }


    @Override
    protected WinningTransactionId getId(WinningTransaction entity) {
        WinningTransactionId winningTransactionId = new WinningTransactionId();
        winningTransactionId.setAcquirerCode(entity.getAcquirerCode());
        winningTransactionId.setIdTrxAcquirer(entity.getIdTrxAcquirer());
        winningTransactionId.setTrxDate(entity.getTrxDate());
        return winningTransactionId;
    }


    @Override
    protected void alterEntityToUpdate(WinningTransaction entity) {
        entity.setUpdateUser("userUpdate");
    }


    @Override
    protected Function<Integer, WinningTransactionId> idBuilderFn() {
        return (bias) -> {
            WinningTransactionId winningTransactionId = new WinningTransactionId();
            OffsetDateTime offsetDateTime = OffsetDateTime.parse("2020-04-09T16:22:45.304Z");
            winningTransactionId.setTrxDate(offsetDateTime);
            winningTransactionId.setIdTrxAcquirer(String.valueOf(bias));
            winningTransactionId.setAcquirerCode(bias.toString());
            return winningTransactionId;
        };
    }


    @Override
    protected String getIdName() {
        return "acquirerCode";
    }
}
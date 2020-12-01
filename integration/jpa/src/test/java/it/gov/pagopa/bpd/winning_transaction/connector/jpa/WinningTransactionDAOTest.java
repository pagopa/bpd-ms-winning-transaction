package it.gov.pagopa.bpd.winning_transaction.connector.jpa;

import eu.sia.meda.layers.connector.query.CriteriaQuery;
import eu.sia.meda.util.TestUtils;
import it.gov.pagopa.bpd.common.connector.jpa.BaseCrudJpaDAOTest;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransactionId;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
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
        entity.setUpdatable(true);
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
        entity.setUpdatable(true);
        entity.setUpdateUser("userUpdate");
    }

    @Override
    protected void compare(WinningTransaction entityToSave, WinningTransaction saved) {
        TestUtils.reflectionEqualsByName(entityToSave, saved, new String[]{"insertDate", "insertUser", "updateDate", "updateUser", "enabled", "updatable", "new", "isNew"});
    }

    @Override
    protected Function<Integer, WinningTransactionId> idBuilderFn() {
        return (bias) -> {
            WinningTransactionId winningTransactionId = new WinningTransactionId();
            OffsetDateTime offsetDateTime = LocalDateTime.parse("2020-04-09T16:22:45.304")
                    .atZone(ZoneId.systemDefault()).toOffsetDateTime();
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
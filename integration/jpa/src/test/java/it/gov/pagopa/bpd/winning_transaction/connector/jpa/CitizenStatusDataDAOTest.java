package it.gov.pagopa.bpd.winning_transaction.connector.jpa;

import eu.sia.meda.layers.connector.query.CriteriaQuery;
import it.gov.pagopa.bpd.common.connector.jpa.BaseCrudJpaDAOTest;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.CitizenStatusData;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.OffsetDateTime;
import java.util.function.Function;

public class CitizenStatusDataDAOTest extends
        BaseCrudJpaDAOTest<CitizenStatusDataDAO, CitizenStatusData, String> {

    @Data
    private static class CitizenStatusDataCriteria implements CriteriaQuery<CitizenStatusData> {
        String fiscalCode;
    }

    @Autowired
    private CitizenStatusDataDAO dao;

    @Override
    protected CriteriaQuery<? super CitizenStatusData> getMatchAlreadySavedCriteria() {
        CitizenStatusDataDAOTest.CitizenStatusDataCriteria criteriaQuery =
                new CitizenStatusDataDAOTest.CitizenStatusDataCriteria();
        criteriaQuery.setFiscalCode(getStoredId());
        return criteriaQuery;
    }

    @Override
    protected CitizenStatusDataDAO getDao() {
        return dao;
    }

    @Override
    protected void setId(CitizenStatusData entity, String id) {
        entity.setFiscalCode(id);
    }

    @Override
    protected String getId(CitizenStatusData entity) {
        return entity.getFiscalCode();
    }

    @Override
    protected void alterEntityToUpdate(CitizenStatusData entity) {
        entity.setUpdateDateTime(OffsetDateTime.now());
    }

    @Override
    protected Function<Integer, String> idBuilderFn() {
        return (bias) -> {
            return "fiscalCode_"+bias;
        };
    }

    @Override
    protected String getIdName() {
        return "fiscalCode";
    }

}

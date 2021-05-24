package it.gov.pagopa.bpd.winning_transaction.connector.jpa;

import it.gov.pagopa.bpd.common.connector.jpa.CrudJpaDAO;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransactionId;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransactionTransfer;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Data Access Object to manage all CRUD operations to the database
 */
@Repository
public interface WinningTransactionTransferDAO extends CrudJpaDAO<WinningTransactionTransfer, WinningTransactionId> {

    @Modifying
    @Query("delete from WinningTransactionTransfer t where t.fiscalCode = :fiscalCode")
    void deleteByFiscalCode(@Param("fiscalCode") String fiscalCode);

}

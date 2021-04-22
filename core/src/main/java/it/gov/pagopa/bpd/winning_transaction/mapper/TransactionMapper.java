package it.gov.pagopa.bpd.winning_transaction.mapper;

import it.gov.pagopa.bpd.winning_transaction.command.model.Transaction;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

/**
 * Class to be used to map a {@link Transaction} from an* {@link WinningTransaction}
 */

@Service
public class TransactionMapper {

    /**
     * @param transaction instance of an  {@link Transaction}, to be mapped into a {@link WinningTransaction}
     * @return {@link Transaction} instance from the input transaction,
     */
    public WinningTransaction map(
            Transaction transaction) {

        WinningTransaction winningTransaction = null;

        if (transaction != null) {
            winningTransaction = WinningTransaction.builder().build();
            BeanUtils.copyProperties(transaction, winningTransaction);
            winningTransaction.setOperationType(transaction.getOperationType().getCode());
//            winningTransaction.setPar(transaction.getPar());
        }

        return winningTransaction;

    }

}

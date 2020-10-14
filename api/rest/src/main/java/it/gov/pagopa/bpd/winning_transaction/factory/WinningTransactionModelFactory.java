package it.gov.pagopa.bpd.winning_transaction.factory;

import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.model.dto.WinningTransactionDTO;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

/**
 * Mapper between <WinningTransactionDTO> DTO class and <WinningTransaction> Entity class
 */
@Component
public class WinningTransactionModelFactory implements ModelFactory<WinningTransactionDTO, WinningTransaction>  {

    @SneakyThrows
    @Override
    public WinningTransaction createModel(WinningTransactionDTO dto) {
        WinningTransaction winningTransaction = WinningTransaction.builder().build();
        BeanUtils.copyProperties(winningTransaction, dto);
        return winningTransaction;
    }
}

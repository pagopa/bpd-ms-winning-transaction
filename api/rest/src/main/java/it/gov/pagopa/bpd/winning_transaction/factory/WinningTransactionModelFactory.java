package it.gov.pagopa.bpd.winning_transaction.factory;

import it.gov.pagopa.bpd.winning_transaction.model.dto.WinningTransactionDTO;
import it.gov.pagopa.bpd.winning_transaction.model.entity.WinningTransaction;
import lombok.SneakyThrows;
import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Component;

@Component
public class WinningTransactionModelFactory implements ModelFactory<WinningTransactionDTO, WinningTransaction>  {

    @SneakyThrows
    @Override
    public WinningTransaction createModel(WinningTransactionDTO dto) {
        WinningTransaction winningTransaction = new WinningTransaction();
        BeanUtils.copyProperties(winningTransaction, dto);
        return winningTransaction;
    }
}

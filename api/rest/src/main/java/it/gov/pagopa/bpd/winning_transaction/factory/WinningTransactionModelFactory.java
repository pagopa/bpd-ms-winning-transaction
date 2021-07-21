package it.gov.pagopa.bpd.winning_transaction.factory;

import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.resource.dto.WinningTransactionDTO;
import lombok.SneakyThrows;
import org.springframework.stereotype.Component;

/**
 * Mapper between <WinningTransactionDTO> DTO class and <WinningTransaction> Entity class
 */
@Component
public class WinningTransactionModelFactory implements ModelFactory<WinningTransactionDTO, WinningTransaction> {

    @SneakyThrows
    @Override
    public WinningTransaction createModel(WinningTransactionDTO dto) {
        WinningTransaction winningTransaction = WinningTransaction.builder().build();
        winningTransaction.setAcquirerId(dto.getAcquirerId());
        winningTransaction.setAcquirerCode(dto.getAcquirerCode());
        winningTransaction.setAmount(dto.getAmount());
        winningTransaction.setAmountCurrency(dto.getAmountCurrency());
        winningTransaction.setAwardPeriodId(dto.getAwardPeriodId());
        winningTransaction.setBin(dto.getBin());
        winningTransaction.setCircuitType(dto.getCircuitType());
        winningTransaction.setCorrelationId(dto.getCorrelationId());
        winningTransaction.setHpan(dto.getHpan());
        winningTransaction.setIdTrxAcquirer(dto.getIdTrxAcquirer());
        winningTransaction.setIdTrxIssuer(dto.getIdTrxIssuer());
        winningTransaction.setMcc(dto.getMcc());
        winningTransaction.setMccDescription(dto.getMccDescription());
        winningTransaction.setMerchantId(dto.getMerchantId());
        winningTransaction.setOperationType(dto.getOperationType());
        winningTransaction.setScore(dto.getScore());
        winningTransaction.setTerminalId(dto.getTerminalId());
        winningTransaction.setTrxDate(dto.getTrxDate());
        return winningTransaction;
    }
}

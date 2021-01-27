package it.gov.pagopa.bpd.winning_transaction.assembler;

import it.gov.pagopa.bpd.winning_transaction.resource.resource.FindWinningTransactionResource;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.WinningTransactionPage;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.WinningTransactionsOfTheDay;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Mapper between <WinningTransaction> Entity class and <FindWinningTransactionV2Resource> Resource class
 */
@Service
public class FindWinningTransactionV2ResourceAssembler {

    public WinningTransactionsOfTheDay toGroupingByDateAndCount(Map.Entry<LocalDate, List<FindWinningTransactionResource>> entry) {
        WinningTransactionsOfTheDay resource = null;

        if (entry != null) {
            resource = WinningTransactionsOfTheDay.builder().build();

            LocalDate trxDate = entry.getKey();
            if (trxDate != null){
                resource.setDate(trxDate);
            }

            List<FindWinningTransactionResource> winningTransactions = entry.getValue();
            if (winningTransactions != null && !winningTransactions.isEmpty()) {
                resource.setCount(winningTransactions.size());
                resource.setTransactions(winningTransactions);
            }
        }

        return resource;
    }

    public WinningTransactionPage toResource(Integer totalPages, Integer currentPage, List<WinningTransactionsOfTheDay> model){
        WinningTransactionPage resource = null;
        if (model != null) {
            resource = WinningTransactionPage.builder().build();
            if (currentPage != null && totalPages > currentPage+1) {
                resource.setNextCursor(currentPage+1);
            } else {
                resource.setNextCursor(null);
            }
            resource.setTransactions(model);
        }

        return resource;
    }
}

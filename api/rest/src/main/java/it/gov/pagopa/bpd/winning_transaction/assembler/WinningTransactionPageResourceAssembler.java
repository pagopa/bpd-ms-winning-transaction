package it.gov.pagopa.bpd.winning_transaction.assembler;

import it.gov.pagopa.bpd.winning_transaction.resource.resource.WinningTransactionMilestoneResource;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.WinningTransactionPage;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.WinningTransactionsOfTheDay;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * Mapper between <WinningTransaction> Entity class and <WinningTransactionPageResource> Resource class
 */
@Service
public class WinningTransactionPageResourceAssembler {

    public WinningTransactionsOfTheDay
    toWinningTransactionsOfTheDayResource(Map.Entry<LocalDate, List<WinningTransactionMilestoneResource>> entry) {
        WinningTransactionsOfTheDay resource = null;

        if (entry != null) {
            resource = WinningTransactionsOfTheDay.builder().build();
            resource.setDate(entry.getKey());
            resource.setTransactions(entry.getValue());
        }

        return resource;
    }

    public WinningTransactionPage toWinningTransactionPageResource(Integer totalPages, Integer currentPage, List<WinningTransactionsOfTheDay> model){
        WinningTransactionPage resource = null;
        if (model != null) {
            resource = WinningTransactionPage.builder().build();

            if (currentPage != null && currentPage > 0 && currentPage < totalPages) {
                resource.setPrevCursor(currentPage-1);
            } else {
                resource.setPrevCursor(null);
            }

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

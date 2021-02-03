package it.gov.pagopa.bpd.winning_transaction.assembler;

import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransactionByDateCount;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.WinningTransactionMilestoneResource;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.WinningTransactionPage;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.WinningTransactionsOfTheDay;
import org.springframework.stereotype.Service;

import java.sql.Date;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Mapper between <WinningTransaction> Entity class and <FindWinningTransactionV2Resource> Resource class
 */
@Service
public class FindWinningTransactionV2ResourceAssembler {

    public WinningTransactionsOfTheDay
        toMilestoneGroupingByDateAndCount(Map.Entry<LocalDate, List<WinningTransactionMilestoneResource>> entry,
                                  List<WinningTransactionByDateCount> winningTransactionByDateCount) {
        WinningTransactionsOfTheDay resource = null;

        if (entry != null) {
            resource = WinningTransactionsOfTheDay.builder().build();

            LocalDate trxDate = entry.getKey();
            if (trxDate != null){
                resource.setDate(trxDate);
                resource.setCount(getTransactionCountByDate(winningTransactionByDateCount, trxDate));
            }

            List<WinningTransactionMilestoneResource> winningTransactions = entry.getValue();
            if (winningTransactions != null && !winningTransactions.isEmpty()) {
                resource.setTransactions(winningTransactions);
            }
        }

        return resource;
    }

    public WinningTransactionPage toResourceWinningTransactionMilestoneResource(Integer totalPages, Integer currentPage, List<WinningTransactionsOfTheDay> model){
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

    private Integer getTransactionCountByDate(List<WinningTransactionByDateCount> winningTransactionByDateCounts, LocalDate trxDate){
        Optional<WinningTransactionByDateCount> opt = Optional.empty();
        if (winningTransactionByDateCounts != null && !winningTransactionByDateCounts.isEmpty()) {
            opt = winningTransactionByDateCounts.stream().filter(wdc -> new Date(wdc.getTrxDate().getTime()).toLocalDate().equals(trxDate)).findFirst();
        }

        return opt.isPresent() ? opt.map(WinningTransactionByDateCount::getCount).orElse(null) : null;
    }
}

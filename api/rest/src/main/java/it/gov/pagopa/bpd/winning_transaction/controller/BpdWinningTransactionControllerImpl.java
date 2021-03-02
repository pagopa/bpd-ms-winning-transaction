package it.gov.pagopa.bpd.winning_transaction.controller;

import eu.sia.meda.core.controller.StatelessController;
import it.gov.pagopa.bpd.winning_transaction.assembler.*;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.TrxCountByDay;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransactionMilestone;
import it.gov.pagopa.bpd.winning_transaction.factory.ModelFactory;
import it.gov.pagopa.bpd.winning_transaction.resource.dto.WinningTransactionDTO;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.*;
import it.gov.pagopa.bpd.winning_transaction.service.WinningTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityExistsException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.TreeMap;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @See BpdWinningTransactionController
 */
@RestController
class BpdWinningTransactionControllerImpl extends StatelessController implements BpdWinningTransactionController {

    private final ModelFactory<WinningTransactionDTO, WinningTransaction> winningTransactionFactory;
    private final WinningTransactionResourceAssembler winningTransactionResourceAssembler;
    private final FindWinningTransactionResourceAssembler findWinningTransactionResourceAssembler;
    private final WinningTransactionMilestoneResourceAssembler winningTransactionMilestoneResourceAssembler;
    private final WinningTransactionPageResourceAssembler winningTransactionPageResourceAssembler;
    private final TrxCountByDayResourceAssembler trxCountByDayResourceAssembler;
    private final WinningTransactionService winningTransactionService;

    @Autowired
    public BpdWinningTransactionControllerImpl(
            ModelFactory<WinningTransactionDTO, WinningTransaction> winningTransactionFactory,
            WinningTransactionResourceAssembler winningTransactionResourceAssembler,
            FindWinningTransactionResourceAssembler findWinningTransactionResourceAssembler,
            WinningTransactionMilestoneResourceAssembler winningTransactionMilestoneResourceAssembler, WinningTransactionPageResourceAssembler winningTransactionPageResourceAssembler, TrxCountByDayResourceAssembler trxCountByDayResourceAssembler, WinningTransactionService winningTransactionService) {
        this.winningTransactionFactory = winningTransactionFactory;
        this.winningTransactionResourceAssembler = winningTransactionResourceAssembler;
        this.findWinningTransactionResourceAssembler = findWinningTransactionResourceAssembler;
        this.winningTransactionMilestoneResourceAssembler = winningTransactionMilestoneResourceAssembler;
        this.winningTransactionPageResourceAssembler = winningTransactionPageResourceAssembler;
        this.trxCountByDayResourceAssembler = trxCountByDayResourceAssembler;
        this.winningTransactionService = winningTransactionService;
    }

    @Override
    public WinningTransactionResource createWinningTransaction(WinningTransactionDTO dto) {
        if (logger.isDebugEnabled()) {
            logger.debug("BpdWinningTransactionControllerImpl.createWinningTransaction");
            logger.debug("dto = [" + dto + "]");
        }
        try {
            WinningTransaction winningTransaction = winningTransactionFactory.createModel(dto);
            winningTransaction = winningTransactionService.create(winningTransaction);
            return winningTransactionResourceAssembler.toResource(winningTransaction);
        } catch (EntityExistsException e) {
            if (logger.isErrorEnabled()) {
                logger.error(e.getMessage(), e);
            }
            throw new ResponseStatusException(HttpStatus.CONFLICT);
        }
    }

    @Override
    public List<FindWinningTransactionResource> findWinningTransactions(String hpan, Long awardPeriodId, String fiscalCode) {
        if (logger.isDebugEnabled()) {
            logger.debug("BpdWinningTransactionControllerImpl.findWinningTransactions");
            logger.debug("hpan = [" + hpan + "], awardPeriodId = [" + awardPeriodId + "]");
        }
        List<WinningTransaction> winningTransactions = winningTransactionService
                .getWinningTransactions(hpan, awardPeriodId, fiscalCode);
        return winningTransactions.stream()
                .map(findWinningTransactionResourceAssembler::toResource)
                .collect(Collectors.toList());
    }

    @Override
    public WinningTransactionPage findWinningTransactionsMilestonePage(String hpan, Long awardPeriodId, String fiscalCode, Integer currentPage, Integer size) {
        if (logger.isDebugEnabled()) {
            logger.debug("BpdWinningTransactionControllerImpl.findWinningTransactions");
            logger.debug("hpan = [" + hpan + "], awardPeriodId = [" + awardPeriodId + "]");
        }
        Pageable pageable = PageRequest.of(currentPage, size, Sort.by(Sort.Order.desc("trxDate")));

        Page<WinningTransactionMilestone> winningTransactionsMilestonePage = winningTransactionService
                .getWinningTransactionsMilestonePage(hpan, awardPeriodId, fiscalCode, pageable);

        Supplier<TreeMap<LocalDate, List<WinningTransactionMilestoneResource>>> treeMapSupplier = () -> new TreeMap<>(Comparator.reverseOrder());

        TreeMap<LocalDate, List<WinningTransactionMilestoneResource>> localDateListTreeMap = winningTransactionsMilestonePage.stream()
                .map(winningTransactionMilestoneResourceAssembler::toWinningTransactionMilestoneResource)
                .collect(Collectors.groupingBy(resource -> resource.getTrxDate().toLocalDate(),
                        treeMapSupplier,
                        Collectors.toList()));

        List<WinningTransactionsOfTheDay> transactions =
                localDateListTreeMap
                        .entrySet().stream()
                        .map(winningTransactionPageResourceAssembler::toWinningTransactionsOfTheDayResource)
                        .collect(Collectors.toList());

        return winningTransactionPageResourceAssembler.toWinningTransactionPageResource(winningTransactionsMilestonePage.getTotalPages(), currentPage, transactions);
    }

    @Override
    public List<TrxCountByDayResource> getCountByDay(String hpan, Long awardPeriodId, String fiscalCode) {
        if (logger.isDebugEnabled()) {
            logger.debug("BpdWinningTransactionControllerImpl.getCountByDay");
            logger.debug("hpan = [" + hpan + "], awardPeriodId = [" + awardPeriodId + "]");
        }
        List<TrxCountByDay> trxCountByDays =
                winningTransactionService.getWinningTransactionByDateCount(hpan, awardPeriodId, fiscalCode);

        return trxCountByDays.stream()
                .map(trxCountByDayResourceAssembler::toTrxCountByDayResource)
                .sorted(Comparator.comparing(TrxCountByDayResource::getTrxDate, Collections.reverseOrder()))
                .collect(Collectors.toList());
    }

    @Override
    public void deleteByFiscalCode(String fiscalCode) {
        if (logger.isDebugEnabled()) {
            logger.debug("BpdWinningTransactionControllerImpl.deleteByFiscalCode");
            logger.debug("fiscalCode = [" + fiscalCode + "]");
        }
        winningTransactionService.deleteByFiscalCode(fiscalCode);
    }

    @Override
    public void rollback(String fiscalCode, OffsetDateTime requestTimestamp) {
        if (logger.isDebugEnabled()) {
            logger.debug("BpdWinningTransactionControllerImpl.rollback");
            logger.debug("fiscalCode = [" + fiscalCode + "], requestTimestamp = [" + requestTimestamp + "]");
        }
        winningTransactionService.reactivateForRollback(fiscalCode, requestTimestamp);
    }
}

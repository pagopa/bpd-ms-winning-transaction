package it.gov.pagopa.bpd.winning_transaction.controller;

import eu.sia.meda.core.controller.StatelessController;
import it.gov.pagopa.bpd.winning_transaction.assembler.FindWinningTransactionResourceAssembler;
import it.gov.pagopa.bpd.winning_transaction.assembler.FindWinningTransactionV2ResourceAssembler;
import it.gov.pagopa.bpd.winning_transaction.assembler.WinningTransactionResourceAssembler;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.factory.ModelFactory;
import it.gov.pagopa.bpd.winning_transaction.resource.dto.WinningTransactionDTO;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.FindWinningTransactionResource;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.WinningTransactionsOfTheDay;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.WinningTransactionPage;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.WinningTransactionResource;
import it.gov.pagopa.bpd.winning_transaction.service.WinningTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityExistsException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @See BpdWinningTransactionController
 */
@RestController
class BpdWinningTransactionControllerImpl extends StatelessController implements BpdWinningTransactionController {

    private final ModelFactory<WinningTransactionDTO, WinningTransaction> winningTransactionFactory;
    private final WinningTransactionResourceAssembler winningTransactionResourceAssembler;
    private final FindWinningTransactionResourceAssembler findWinningTransactionResourceAssembler;
    private final FindWinningTransactionV2ResourceAssembler findWinningTransactionV2ResourceAssembler;
    private final WinningTransactionService winningTransactionService;

    @Autowired
    public BpdWinningTransactionControllerImpl(
            ModelFactory<WinningTransactionDTO, WinningTransaction> winningTransactionFactory,
            WinningTransactionResourceAssembler winningTransactionResourceAssembler,
            FindWinningTransactionResourceAssembler findWinningTransactionResourceAssembler,
            FindWinningTransactionV2ResourceAssembler findWinningTransactionV2ResourceAssembler, WinningTransactionService winningTransactionService) {
        this.winningTransactionFactory = winningTransactionFactory;
        this.winningTransactionResourceAssembler = winningTransactionResourceAssembler;
        this.findWinningTransactionResourceAssembler = findWinningTransactionResourceAssembler;
        this.findWinningTransactionV2ResourceAssembler = findWinningTransactionV2ResourceAssembler;
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
    public WinningTransactionPage findWinningTransactionsPage(String hpan, Long awardPeriodId, String fiscalCode, Integer currentPage, Integer size) {
        if (logger.isDebugEnabled()) {
            logger.debug("BpdWinningTransactionControllerImpl.findWinningTransactions");
            logger.debug("hpan = [" + hpan + "], awardPeriodId = [" + awardPeriodId + "]");
        }
        Pageable pageable = PageRequest.of(currentPage, size, Sort.by(Sort.Order.desc("trx_timestamp_t")));

        Page<WinningTransaction> winningTransactionsPage = winningTransactionService
                .getWinningTransactionsV2(hpan, awardPeriodId, fiscalCode, pageable);

        Map<OffsetDateTime, List<FindWinningTransactionResource>> winningTransactionsByDate =
                winningTransactionsPage.stream()
                .map(findWinningTransactionResourceAssembler::toResource)
                .collect(Collectors.groupingBy(FindWinningTransactionResource::getTrxDate));

        List<WinningTransactionsOfTheDay> transactions =
                winningTransactionsByDate.entrySet().stream()
                        .map(findWinningTransactionV2ResourceAssembler::toGroupingByDateAndCount)
                        .collect(Collectors.toList());

        return findWinningTransactionV2ResourceAssembler.toResource(winningTransactionsPage.getTotalPages(), currentPage, transactions);
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

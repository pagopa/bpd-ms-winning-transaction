package it.gov.pagopa.bpd.winning_transaction.controller;

import eu.sia.meda.core.controller.StatelessController;
import it.gov.pagopa.bpd.winning_transaction.assembler.FindWinningTransactionResourceAssembler;
import it.gov.pagopa.bpd.winning_transaction.assembler.WinningTransactionResourceAssembler;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.factory.ModelFactory;
import it.gov.pagopa.bpd.winning_transaction.resource.dto.WinningTransactionDTO;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.FindWinningTransactionResource;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.PagedResources;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.WinningTransactionResource;
import it.gov.pagopa.bpd.winning_transaction.service.WinningTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityExistsException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @See BpdWinningTransactionController
 */
@RestController
class BpdWinningTransactionControllerImpl extends StatelessController implements BpdWinningTransactionController {

    private final ModelFactory<WinningTransactionDTO, WinningTransaction> winningTransactionFactory;
    private final WinningTransactionResourceAssembler winningTransactionResourceAssembler;
    private final FindWinningTransactionResourceAssembler findWinningTransactionResourceAssembler;
    private final WinningTransactionService winningTransactionService;

    @Autowired
    public BpdWinningTransactionControllerImpl(
            ModelFactory<WinningTransactionDTO, WinningTransaction> winningTransactionFactory,
            WinningTransactionResourceAssembler winningTransactionResourceAssembler,
            FindWinningTransactionResourceAssembler findWinningTransactionResourceAssembler,
            WinningTransactionService winningTransactionService) {
        this.winningTransactionFactory = winningTransactionFactory;
        this.winningTransactionResourceAssembler = winningTransactionResourceAssembler;
        this.findWinningTransactionResourceAssembler = findWinningTransactionResourceAssembler;
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
    public PagedResources<FindWinningTransactionResource> findWinningTransactionsPaged(
            String hpan, Long awardPeriodId, String fiscalCode, int pageNumber, int pageSize) {
        if (logger.isDebugEnabled()) {
            logger.debug("BpdWinningTransactionControllerImpl.findWinningTransactions");
            logger.debug("hpan = [" + hpan + "], awardPeriodId = [" + awardPeriodId + "]");
        }
        Page<WinningTransaction> winningTransactions = winningTransactionService
                .getWinningTransactionsPaged(hpan, awardPeriodId, fiscalCode, pageNumber, pageSize);

        List<FindWinningTransactionResource> winningTransactionResources =
                winningTransactions.stream()
                .map(findWinningTransactionResourceAssembler::toResource)
                .collect(Collectors.toList());

        PagedResources<FindWinningTransactionResource> pagedResources =
                new PagedResources<>();
        pagedResources.setResources(winningTransactionResources);
        pagedResources.setCurrentPage(winningTransactions.getNumber());
        pagedResources.setNumberOfElements(winningTransactions.getNumberOfElements());
        pagedResources.setCurrentSize(winningTransactions.getSize());
        pagedResources.setTotalElements(winningTransactions.getTotalElements());
        pagedResources.setTotalPages(winningTransactions.getTotalPages());

        return pagedResources;
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

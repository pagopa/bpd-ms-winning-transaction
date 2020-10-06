package it.gov.pagopa.bpd.winning_transaction.controller;

import eu.sia.meda.core.controller.StatelessController;
import it.gov.pagopa.bpd.winning_transaction.assembler.FindWinningTransactionResourceAssembler;
import it.gov.pagopa.bpd.winning_transaction.assembler.WinningTransactionResourceAssembler;
import it.gov.pagopa.bpd.winning_transaction.connector.jpa.model.WinningTransaction;
import it.gov.pagopa.bpd.winning_transaction.factory.ModelFactory;
import it.gov.pagopa.bpd.winning_transaction.model.dto.WinningTransactionDTO;
import it.gov.pagopa.bpd.winning_transaction.model.resource.FindWinningTransactionResource;
import it.gov.pagopa.bpd.winning_transaction.model.resource.TotalScoreResource;
import it.gov.pagopa.bpd.winning_transaction.model.resource.WinningTransactionResource;
import it.gov.pagopa.bpd.winning_transaction.service.WinningTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import javax.persistence.EntityExistsException;
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
            FindWinningTransactionResourceAssembler findWinningTransactionResourceAssembler, WinningTransactionService winningTransactionService) {
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
    public List<FindWinningTransactionResource> findWinningTransactions(String hpan, Long awardPeriodId) {
        if (logger.isDebugEnabled()) {
            logger.debug("BpdWinningTransactionControllerImpl.findWinningTransactions");
            logger.debug("hpan = [" + hpan + "], awardPeriodId = [" + awardPeriodId + "]");
        }
        List<WinningTransaction> winningTransactions = winningTransactionService
                .getWinningTransactions(hpan, awardPeriodId);
        return winningTransactions.stream()
                .map(findWinningTransactionResourceAssembler::toResource)
                .collect(Collectors.toList());
    }

    @Override
    public TotalScoreResource getTotalScore(String hpan, Long awardPeriodId) {
        if (logger.isDebugEnabled()) {
            logger.debug("BpdWinningTransactionControllerImpl.getTotalScore");
            logger.debug("hpan = [" + hpan + "], awardPeriodId = [" + awardPeriodId + "]");
        }

        return new TotalScoreResource(winningTransactionService.getTotalScore(hpan, awardPeriodId));

    }
}

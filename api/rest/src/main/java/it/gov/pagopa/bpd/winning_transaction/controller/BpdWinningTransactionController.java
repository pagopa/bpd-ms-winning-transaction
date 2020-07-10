package it.gov.pagopa.bpd.winning_transaction.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import it.gov.pagopa.bpd.winning_transaction.model.dto.WinningTransactionDTO;
import it.gov.pagopa.bpd.winning_transaction.model.resource.TotalScoreResource;
import it.gov.pagopa.bpd.winning_transaction.model.resource.WinningTransactionResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * Controller to expose MicroService
 */
@Api(tags = "Bonus Pagamenti Digitali winning-transaction Controller")
@RequestMapping("/bpd/winning-transactions")
@Validated
public interface BpdWinningTransactionController {


    @PostMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    WinningTransactionResource createWinningTransaction(
            @Valid @RequestBody WinningTransactionDTO winningTransactionDTO);

    @GetMapping(produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    List<WinningTransactionResource> findWinningTransactions(
            @ApiParam(value = "${swagger.winningTransaction.hashPan}", required = true)
            @NotBlank
            @RequestParam
                    String hpan,
            @ApiParam(value = "${swagger.winningTransaction.awardPeriodId}", required = true)
            @NotNull
            @RequestParam
                    Long awardPeriodId
    );

    @GetMapping(value = "/total-score", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(HttpStatus.OK)
    TotalScoreResource getTotalScore(
            @ApiParam(value = "${swagger.winningTransaction.hashPan}", required = true)
            @NotBlank
            @RequestParam
                    String hpan,
            @ApiParam(value = "${swagger.winningTransaction.awardPeriodId}", required = true)
            @NotNull
            @RequestParam
                    Long awardPeriodId
    );

}

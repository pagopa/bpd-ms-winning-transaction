package it.gov.pagopa.bpd.winning_transaction.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiParam;
import it.gov.pagopa.bpd.common.util.Constants;
import it.gov.pagopa.bpd.winning_transaction.resource.dto.WinningTransactionDTO;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.FindWinningTransactionResource;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.TrxCountByDayResource;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.WinningTransactionPage;
import it.gov.pagopa.bpd.winning_transaction.resource.resource.WinningTransactionResource;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.OffsetDateTime;
import java.util.List;

/**
 * Controller to expose MicroService
 */
@Api(tags = "Bonus Pagamenti Digitali winning-transaction Controller")
@RequestMapping("/bpd/winning-transactions")
@Validated
public interface BpdWinningTransactionController {


    @PostMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    WinningTransactionResource createWinningTransaction(
            @Valid @RequestBody WinningTransactionDTO winningTransactionDTO);

    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    List<FindWinningTransactionResource> findWinningTransactions(
            @ApiParam(value = "${swagger.winningTransaction.hashPan}", required = false)
            @RequestParam(required = false)
                    String hpan,
            @ApiParam(value = "${swagger.winningTransaction.awardPeriodId}", required = true)
            @NotNull
            @RequestParam
                    Long awardPeriodId,
            @ApiParam(value = "${swagger.winningTransaction.fiscalCode}", required = true)
            @NotNull
            @RequestParam
            @Valid @NotBlank @Size(min = 16, max = 16) @Pattern(regexp = Constants.FISCAL_CODE_REGEX)
                    String fiscalCode
    );

    @GetMapping(value = "/milestone/page", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    WinningTransactionPage findWinningTransactionsMilestonePage(
            @ApiParam(value = "${swagger.winningTransaction.hashPan}")
            @RequestParam(required = false)
                    String hpan,
            @ApiParam(value = "${swagger.winningTransaction.awardPeriodId}", required = true)
            @NotNull
            @RequestParam
                    Long awardPeriodId,
            @ApiParam(value = "${swagger.winningTransaction.fiscalCode}", required = true)
            @NotNull
            @RequestParam
            @Valid @NotBlank @Size(min = 16, max = 16) @Pattern(regexp = Constants.FISCAL_CODE_REGEX)
                    String fiscalCode,
            @ApiParam(value = "${swagger.winningTransaction.nextCursor}")
            @RequestParam(name = "nextCursor", defaultValue = "0")
                    Integer page,
            @ApiParam(value = "${swagger.winningTransaction.limit}")
            @RequestParam(name = "limit", defaultValue = "20")
                    Integer size
    );

    @GetMapping(value = "/countbyday", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.OK)
    List<TrxCountByDayResource> getCountByDay(
            @ApiParam(value = "${swagger.winningTransaction.hashPan}")
            @RequestParam(required = false)
                    String hpan,
            @ApiParam(value = "${swagger.winningTransaction.awardPeriodId}", required = true)
            @NotNull
            @RequestParam
                    Long awardPeriodId,
            @ApiParam(value = "${swagger.winningTransaction.fiscalCode}", required = true)
            @NotNull
            @RequestParam
            @Valid @NotBlank @Size(min = 16, max = 16) @Pattern(regexp = Constants.FISCAL_CODE_REGEX)
                    String fiscalCode
    );

    @DeleteMapping(value = "/{fiscalCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void deleteByFiscalCode(
            @ApiParam(value = "${swagger.winningTransaction.fiscalCode}", required = true)
            @PathVariable("fiscalCode")
            @NotBlank String fiscalCode
    );

    @PutMapping(value = "/rollback/{fiscalCode}", produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseStatus(HttpStatus.NO_CONTENT)
    void rollback(
            @ApiParam(required = true)
            @PathVariable("fiscalCode")
            @NotBlank
                    String fiscalCode,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
                    OffsetDateTime requestTimestamp
    );

}

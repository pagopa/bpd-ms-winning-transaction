package it.gov.pagopa.bpd.winning_transaction.connector;

import it.gov.pagopa.bpd.winning_transaction.connector.model.CitizenResource;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * Citizen Rest Client
 */
@FeignClient(name = "${rest-client.citizen.serviceCode}", url = "${rest-client.citizen.base-url}")
public interface CitizenRestClient {

    @GetMapping(value = "${rest-client.citizen.getTotalCashback.url}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    CitizenResource getTotalCashback(
            @RequestParam(value = "hpan", required = false)
                    String hpan,
            @NotBlank
            @RequestParam
                    String fiscalCode,
            @NotNull
            @RequestParam
                    Long awardPeriodId);
}

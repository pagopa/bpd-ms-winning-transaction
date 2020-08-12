package it.gov.pagopa.bpd.winning_transaction.model.resource;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"totalScore"}, callSuper = false)
public class TotalScoreResource {

    @JsonProperty(required = true)
    private Long totalScore;

}

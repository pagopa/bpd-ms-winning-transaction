package it.gov.pagopa.bpd.winning_transaction.connector.jpa.model;

import it.gov.pagopa.bpd.common.connector.jpa.model.BaseEntity;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.time.OffsetDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of = {"fiscalCode"}, callSuper = false)
@Table(name = "bpd_citizen_status_data")
public class CitizenStatusData extends BaseEntity implements Serializable {

    @Id
    @Column(name="fiscal_code_s")
    String fiscalCode;

    @Column(name="enabled_b")
    Boolean enabled;

    @Column(name = "update_timestamp_t")
    OffsetDateTime updateTimestamp;

}

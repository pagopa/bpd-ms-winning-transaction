package it.gov.pagopa.bpd.winning_transaction.resource.resource;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
public class PagedResources<T> {

    List<T> resources;
    private Integer currentPage;
    private Integer currentSize;
    private Integer numberOfElements;
    private Long totalElements;
    private Integer totalPages;

}

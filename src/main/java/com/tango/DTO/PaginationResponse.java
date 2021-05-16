package com.tango.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PaginationResponse<T> {

    private List<T> result;
    private Pagination pagination;

    public PaginationResponse(Page<T> result) {
        this.result = result.getContent();
        this.pagination = new Pagination(
                result.getPageable().getPageNumber(),
                result.getPageable().getPageSize(),
                result.getTotalPages());
    }

    @Data
    @AllArgsConstructor
    private static class Pagination {
        private int page;
        private int size;
        private int count;
    }
}

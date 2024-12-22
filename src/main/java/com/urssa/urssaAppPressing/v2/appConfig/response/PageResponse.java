package com.urssa.urssaAppPressing.v2.appConfig.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;


@Data
@AllArgsConstructor
public class PageResponse<T> {

    private List<T> content;
    private Long totalElements;
    private Integer totalPages;
    private Integer currentPage;
    private Integer pageSize;

}

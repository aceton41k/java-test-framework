package com.github.aceton41k.model.page;

import lombok.Getter;

@Getter
public class Pageable {
    private Integer pageNumber;
    private Integer pageSize;
    private Integer offset;
    private Boolean paged;
    private Boolean unpaged;
    private Sort sort;
}

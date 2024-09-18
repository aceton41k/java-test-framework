package com.github.aceton41k.model.page;

import lombok.Getter;

import java.util.List;

@Getter
public class Page<T> {
    private List<T> content;
    private Integer totalPages;
    private Integer totalElements;
    private Boolean last;
    private Boolean first;
    private Integer size;
    private Integer number;
    private Integer numberOfElements;
    private Boolean empty;
    private Sort sort;
    private Pageable pageable;

}
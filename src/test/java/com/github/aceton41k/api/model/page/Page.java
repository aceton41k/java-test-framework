package com.github.aceton41k.api.model.page;

import lombok.Getter;

import java.util.List;

@Getter
public class Page<T> {
    private List<T> content;
    private int totalPages;
    private int totalElements;
    private boolean last;
    private boolean first;
    private int size;
    private int number;
    private int numberOfElements;
    private boolean empty;
    private Sort sort;
    private Pageable pageable;

}
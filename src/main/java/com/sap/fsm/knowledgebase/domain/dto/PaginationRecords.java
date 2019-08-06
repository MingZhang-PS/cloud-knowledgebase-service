package com.sap.fsm.knowledgebase.domain.dto;

import lombok.Getter;

import org.springframework.data.domain.Page;

import java.util.List;

@Getter
public class PaginationRecords<T> {
    private List<T> results;
    private int size;
    private int number;
    private Boolean first;
    private Boolean last;
    private int numberOfElements;
    private Long totalElements;
    private int totalPages;

    public PaginationRecords(Page<T> pageRecords) {
        this.results = pageRecords.getContent();
        this.size = pageRecords.getSize();
        this.number = pageRecords.getNumber();
        this.first = pageRecords.isFirst();
        this.last = pageRecords.isLast();
        this.numberOfElements = pageRecords.getNumberOfElements();
        this.totalElements = pageRecords.getTotalElements();
        this.totalPages = pageRecords.getTotalPages();
    }
} 
package com.sap.fsm.knowledgebase.domain.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

@ApiModel(description = "Generic Pagination Wrapper")
@Data
public class PaginationRecord<T> {
    @ApiModelProperty(value = "List of entities")
    @JsonProperty("results")
    private List<T> content;
    @ApiModelProperty(value = "Page size of entity list", example = "20")
    private int size;
    @ApiModelProperty(value = "Page number of entity list", example = "0")
    private int number;
    @ApiModelProperty(value = "If the entity list is in first page")
    private boolean first;
    @ApiModelProperty(value = "If the entity list is in last page")
    private boolean last;
    @ApiModelProperty(value = "Number entities of the list", example = "20")
    private int numberOfElements;
    @ApiModelProperty(value = "Total number of entities in the backend", example = "100")
    private long totalElements;
    @ApiModelProperty(value = "Total page of entities in the backend", example = "5")
    private int totalPages;

    public PaginationRecord(Page<T> pageRecords) {
        this.content = pageRecords.getContent();
        this.size = pageRecords.getSize();
        this.number = pageRecords.getNumber();
        this.first = pageRecords.isFirst();
        this.last = pageRecords.isLast();
        this.numberOfElements = pageRecords.getNumberOfElements();
        this.totalElements = pageRecords.getTotalElements();
        this.totalPages = pageRecords.getTotalPages();
    }

    public PaginationRecord() {

    }
}
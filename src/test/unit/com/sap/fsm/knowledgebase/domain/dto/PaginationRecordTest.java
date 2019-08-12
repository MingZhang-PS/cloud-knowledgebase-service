package com.sap.fsm.knowledgebase.domain.dto;

import com.sap.fsm.springboot.starter.test.annotation.Unit;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Unit
class PaginationRecordTest {
    @DisplayName("test pagable KnowledgeBaseProviderTypeDto tranformation")
	@Test
	void shouldPaginationRecordBeConstructedWithGivenValues() {
        // given
        List<KnowledgeBaseProviderTypeDto> contents = new ArrayList<KnowledgeBaseProviderTypeDto>();
        KnowledgeBaseProviderTypeDto firstType = new KnowledgeBaseProviderTypeDto();
        firstType.setId(UUID.fromString("684a6910-7510-4458-969f-71d2199695cc"));
        firstType.setCode("SAP-Native");
        contents.add(firstType);
        KnowledgeBaseProviderTypeDto secondType = new KnowledgeBaseProviderTypeDto();
        secondType.setId(UUID.fromString("d9afb0a4-5857-4c96-b690-50e36d06a380"));
        secondType.setCode("MindTouch");
        contents.add(secondType);

        PageImpl<KnowledgeBaseProviderTypeDto> source = 
        new PageImpl<KnowledgeBaseProviderTypeDto>(contents, PageRequest.of(0, 10),2 );

		// when
        PaginationRecord<KnowledgeBaseProviderTypeDto> providerTypes = 
        new PaginationRecord<KnowledgeBaseProviderTypeDto>(source);

		// then
        assertThat(providerTypes.getContent()).isEqualTo(source.getContent());
        assertThat(providerTypes.getTotalElements()).isEqualTo(source.getTotalElements());
        assertThat(providerTypes.getTotalPages()).isEqualTo(source.getTotalPages());
        assertThat(providerTypes.getSize()).isEqualTo(source.getSize());
        assertThat(providerTypes.getFirst()).isEqualTo(source.isFirst());
        assertThat(providerTypes.getLast()).isEqualTo(source.isLast());
        assertThat(providerTypes.getNumber()).isEqualTo(source.getNumber());
        assertThat(providerTypes.getNumberOfElements()).isEqualTo(source.getNumberOfElements());
	}
}

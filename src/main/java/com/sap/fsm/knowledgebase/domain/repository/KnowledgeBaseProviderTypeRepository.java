package com.sap.fsm.knowledgebase.domain.repository;

import com.sap.fsm.knowledgebase.domain.model.KnowledgeBaseProviderType;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.List;

@Repository
public interface KnowledgeBaseProviderTypeRepository
        extends CrudRepository<KnowledgeBaseProviderType, UUID> {

        List<KnowledgeBaseProviderType> findByCode(String code);
}

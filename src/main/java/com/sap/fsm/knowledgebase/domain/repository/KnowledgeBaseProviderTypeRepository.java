package com.sap.fsm.knowledgebase.domain.repository;

import com.sap.fsm.knowledgebase.domain.model.KnowledgeBaseProviderType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.Optional;
import java.util.List;

@Repository
public interface KnowledgeBaseProviderTypeRepository extends JpaRepository<KnowledgeBaseProviderType, UUID> {
        List<KnowledgeBaseProviderType> findByCode(String code);
        Optional<KnowledgeBaseProviderType> findByIdAndCode(UUID id, String code);
        Page<KnowledgeBaseProviderType> findAll(Pageable pageable);
}

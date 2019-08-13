package com.sap.fsm.knowledgebase.domain.repository;

import com.sap.fsm.knowledgebase.domain.model.KnowledgeBaseProviderConfiguration;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.Optional;

@Repository
public interface KnowledgeBaseProviderConfigurationRepository extends JpaRepository<KnowledgeBaseProviderConfiguration, UUID> {
        Optional<KnowledgeBaseProviderConfiguration> findByProviderType(UUID providerTypeId);      
        //Optional<KnowledgeBaseProviderConfiguration> findByIdAndProviderType(UUID id, UUID providerType);
        Page<KnowledgeBaseProviderConfiguration> findAll(Pageable pageable);
}

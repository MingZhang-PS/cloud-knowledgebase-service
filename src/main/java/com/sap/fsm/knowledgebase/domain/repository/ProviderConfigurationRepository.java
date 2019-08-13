package com.sap.fsm.knowledgebase.domain.repository;

import com.sap.fsm.knowledgebase.domain.model.ProviderConfiguration;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.Optional;

@Repository
public interface ProviderConfigurationRepository extends JpaRepository<ProviderConfiguration, UUID> {
        Optional<ProviderConfiguration> findByProviderType(UUID providerTypeId);      
        //Optional<KnowledgeBaseProviderConfiguration> findByIdAndProviderType(UUID id, UUID providerType);
        Page<ProviderConfiguration> findAll(Pageable pageable);
}

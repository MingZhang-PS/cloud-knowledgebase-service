package com.sap.fsm.knowledgebase.domain.repository;

import com.sap.fsm.knowledgebase.domain.model.ProviderConfiguration;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.Optional;

@Repository
public interface ProviderConfigurationRepository extends JpaRepository<ProviderConfiguration, UUID> {
        Optional<ProviderConfiguration> findByProviderType(String providerTypeCode);

        boolean existsByIsActive(Boolean isActive);

        Page<ProviderConfiguration> findAll(Pageable pageable);

        @Modifying(clearAutomatically = true)
        @Transactional
        Long deleteByProviderType(String providerTypeCode);
}

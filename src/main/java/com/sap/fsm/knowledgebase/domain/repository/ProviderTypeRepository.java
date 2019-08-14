package com.sap.fsm.knowledgebase.domain.repository;

import com.sap.fsm.knowledgebase.domain.model.ProviderType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.Optional;

@Repository
public interface ProviderTypeRepository extends JpaRepository<ProviderType, UUID> {
        Optional<ProviderType> findByCode(String code);
        Optional<ProviderType> findByIdAndCode(UUID id, String code);
        Page<ProviderType> findAll(Pageable pageable);
}

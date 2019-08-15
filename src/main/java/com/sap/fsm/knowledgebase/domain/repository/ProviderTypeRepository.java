package com.sap.fsm.knowledgebase.domain.repository;

import com.sap.fsm.knowledgebase.domain.model.ProviderType;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;

import java.util.Optional;

@Repository
public interface ProviderTypeRepository extends JpaRepository<ProviderType, String> {
        Optional<ProviderType> findByCode(String code);

        Page<ProviderType> findAll(Pageable pageable);

        @Modifying(clearAutomatically = true)
        @Transactional
        Long deleteByCode(String code);
}

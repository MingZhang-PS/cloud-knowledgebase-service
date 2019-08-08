package com.sap.fsm.knowledgebase.domain.repository;

import com.sap.fsm.knowledgebase.domain.model.KnowledgeBaseGeneralSetting;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;
import java.util.Optional;

@Repository
public interface KnowledgeBaseGeneralSettingRepository extends JpaRepository<KnowledgeBaseGeneralSetting, UUID> {
        Optional<KnowledgeBaseGeneralSetting> findByKey(String key);
        Optional<KnowledgeBaseGeneralSetting> findByIdAndKey(UUID id, String key);
        Page<KnowledgeBaseGeneralSetting> findAll(Pageable pageable);
}

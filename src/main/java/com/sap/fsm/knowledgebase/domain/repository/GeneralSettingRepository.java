package com.sap.fsm.knowledgebase.domain.repository;

import com.sap.fsm.knowledgebase.domain.model.GeneralSetting;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;
import java.util.Optional;

@Repository
public interface GeneralSettingRepository extends JpaRepository<GeneralSetting, UUID> {
        Optional<GeneralSetting> findByKey(String key);
        Page<GeneralSetting> findAll(Pageable pageable);
        @Modifying(clearAutomatically = true)
        @Transactional
        Long deleteByKey(String key);
}

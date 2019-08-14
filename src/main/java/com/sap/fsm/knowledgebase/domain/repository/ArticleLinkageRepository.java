package com.sap.fsm.knowledgebase.domain.repository;

import com.sap.fsm.knowledgebase.domain.model.ArticleLinkage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface ArticleLinkageRepository extends JpaRepository<ArticleLinkage, UUID> {
    Page<ArticleLinkage> findByObjectTypeAndObjectId(String objectType, String objectId, Pageable pageable);

    Page<ArticleLinkage> findByArticleId(String articleId, Pageable pageable);

    Page<ArticleLinkage> findByProviderTypeAndArticleId(String providerType, String articleId, Pageable pageable);
}
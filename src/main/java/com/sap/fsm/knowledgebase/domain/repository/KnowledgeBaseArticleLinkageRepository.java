package com.sap.fsm.knowledgebase.domain.repository;

import com.sap.fsm.knowledgebase.domain.model.KnowledgeBaseArticleLinkage;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface KnowledgeBaseArticleLinkageRepository extends JpaRepository<KnowledgeBaseArticleLinkage, UUID> {
    // Retrieve by objectType and objectID
    Page<KnowledgeBaseArticleLinkage> findByObjectTypeAndObjectId(String objectType, String objectId, Pageable pageable);

    // Retrieve by articleId
    Page<KnowledgeBaseArticleLinkage> findByArticleId(String articleId, Pageable pageable);

    // Retrieve by articleId and providerType
    Optional<KnowledgeBaseArticleLinkage> findByProviderTypeAndArticleId(String providerType, String articleId);
}
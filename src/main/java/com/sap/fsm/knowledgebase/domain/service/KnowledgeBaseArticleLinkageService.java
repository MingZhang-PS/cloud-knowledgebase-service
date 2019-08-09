package com.sap.fsm.knowledgebase.domain.service;

import com.sap.fsm.knowledgebase.domain.dto.PaginationRecord;
import com.sap.fsm.knowledgebase.domain.model.KnowledgeBaseArticleLinkage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sap.fsm.knowledgebase.domain.repository.KnowledgeBaseArticleLinkageRepository;
import com.sap.fsm.knowledgebase.domain.dto.KnowledgeBaseArticleLinkageDto;
import org.springframework.transaction.annotation.Transactional;

import com.sap.fsm.knowledgebase.domain.exception.ArticleLinkageNotExistException;

import java.util.Optional;
import java.util.UUID;

@Service
@Transactional
public class KnowledgeBaseArticleLinkageService {
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private KnowledgeBaseArticleLinkageRepository articleLinkageRepository;

    // Create Article Linkage
    public KnowledgeBaseArticleLinkageDto createArticleLinkage(KnowledgeBaseArticleLinkageDto articleLinkageDto) {
        KnowledgeBaseArticleLinkage requestedLinkageModel = this.modelMapper.map(articleLinkageDto, KnowledgeBaseArticleLinkage.class);
        KnowledgeBaseArticleLinkage responseLinkageModel = this.articleLinkageRepository.save(requestedLinkageModel);

        return this.modelMapper.map(responseLinkageModel, KnowledgeBaseArticleLinkageDto.class);
    }

    // Delete Article Linkage by Id
    public void deleteArticleLinkageById(UUID articleLinkageId) {
        this.articleLinkageRepository.deleteById(articleLinkageId);
    }

    // Retrieve Article Linkage by ObjectId and ObjectType
    public PaginationRecord<KnowledgeBaseArticleLinkageDto> retrieveArticleLinkagesByObjectIDAndType(String objectType,
                                                                                                     String objectId,
                                                                                                     Pageable pageable) {
        Page<KnowledgeBaseArticleLinkageDto> results =
                this.articleLinkageRepository.findByObjectTypeAndObjectId(objectType, objectId, pageable).map(result -> {
                    return this.modelMapper.map(result, KnowledgeBaseArticleLinkageDto.class);
                });

        return new PaginationRecord<KnowledgeBaseArticleLinkageDto>(results);
    }

    // Retrieve Article Linkage by ArticleId and ProviderType
    public KnowledgeBaseArticleLinkageDto retrieveArticleLinkeageByProviderTypeAndArticleId(String providerType,
                                                                                            String articleId) {
        Optional<KnowledgeBaseArticleLinkage> result =
                this.articleLinkageRepository.findByProviderTypeAndArticleId(providerType, articleId);
        if (!result.isPresent()) {
            String errorMsg = String.format("ArticleLinkage not found by providerType(%s) and articleId(%s)", providerType, articleId);
            throw new ArticleLinkageNotExistException(errorMsg);
        }

        return this.modelMapper.map(result.get(), KnowledgeBaseArticleLinkageDto.class);
    }


    // Retrieve Article Linkage by ArticleId
    public PaginationRecord<KnowledgeBaseArticleLinkageDto> retrieveArticleLinkagesByArticleId (String articleId, Pageable pageable) {
        Page<KnowledgeBaseArticleLinkageDto> results =
                this.articleLinkageRepository.findByArticleId(articleId, pageable).map(result -> {
                    return this.modelMapper.map(result, KnowledgeBaseArticleLinkageDto.class);
                });

        return new PaginationRecord<KnowledgeBaseArticleLinkageDto>(results);
    }
}
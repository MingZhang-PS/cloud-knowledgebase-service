package com.sap.fsm.knowledgebase.domain.service;

import com.sap.fsm.knowledgebase.domain.dto.PaginationRecord;
import com.sap.fsm.knowledgebase.domain.exception.ArticleLinkageNotExistException;
import com.sap.fsm.knowledgebase.domain.exception.ArticleLinkageExistException;
import com.sap.fsm.knowledgebase.domain.model.KnowledgeBaseArticleLinkage;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sap.fsm.knowledgebase.domain.repository.KnowledgeBaseArticleLinkageRepository;
import com.sap.fsm.knowledgebase.domain.dto.KnowledgeBaseArticleLinkageDto;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Service
@Transactional
public class KnowledgeBaseArticleLinkageService {
    private static final Logger logger = LoggerFactory.getLogger(KnowledgeBaseArticleLinkageService.class);
    private final ModelMapper modelMapper;
    private final KnowledgeBaseArticleLinkageRepository articleLinkageRepository;

    @Autowired
    public KnowledgeBaseArticleLinkageService(ModelMapper modelMapper,
                                              KnowledgeBaseArticleLinkageRepository articleLinkageRepository) {
        this.modelMapper = modelMapper;
        this.articleLinkageRepository = articleLinkageRepository;
    }

    // Create Article Linkage
    public KnowledgeBaseArticleLinkageDto createArticleLinkage(KnowledgeBaseArticleLinkageDto articleLinkageDto) {
        if (null != articleLinkageDto.getId() && articleLinkageRepository.existsById(articleLinkageDto.getId())) {
            final String errorMsg = String.format("ArticleLinkage already present for id: %s",
                    articleLinkageDto.getId().toString());
            logger.error(errorMsg);
            throw new ArticleLinkageExistException(errorMsg);
        }

        KnowledgeBaseArticleLinkage requestedLinkageModel = this.modelMapper.map(articleLinkageDto,
                KnowledgeBaseArticleLinkage.class);
        KnowledgeBaseArticleLinkage responseLinkageModel = this.articleLinkageRepository.save(requestedLinkageModel);
        return this.modelMapper.map(responseLinkageModel, KnowledgeBaseArticleLinkageDto.class);
    }

    // Delete Article Linkage by Id
    public void deleteArticleLinkageById(UUID articleLinkageId) {
        if (this.articleLinkageRepository.existsById(articleLinkageId)) {
            this.articleLinkageRepository.deleteById(articleLinkageId);
        } else {
            ArticleLinkageNotExistException ex = new ArticleLinkageNotExistException(articleLinkageId);
            logger.error(ex.getMessage());
            throw ex;
        }
    }

    // Retrieve Article Linkage by ObjectId and ObjectType
    @Transactional(readOnly = true)
    public PaginationRecord<KnowledgeBaseArticleLinkageDto> retrieveArticleLinkagesByObjectIDAndType(String objectType,
                                                                                                     String objectId,
                                                                                                     Pageable pageable) {
        Page<KnowledgeBaseArticleLinkageDto> results =
                this.articleLinkageRepository.findByObjectTypeAndObjectId(objectType, objectId, pageable).
                        map(result -> { return this.modelMapper.map(result, KnowledgeBaseArticleLinkageDto.class);});

        return new PaginationRecord<KnowledgeBaseArticleLinkageDto>(results);
    }

    // Retrieve Article Linkage by ArticleId and ProviderType
    @Transactional(readOnly = true)
    public PaginationRecord<KnowledgeBaseArticleLinkageDto> retrieveArticleLinkagesByProviderTypeAndArticleId(String providerType,
                                                                                                              String articleId,
                                                                                                              Pageable pageable) {
        Page<KnowledgeBaseArticleLinkageDto> results =
                this.articleLinkageRepository.findByProviderTypeAndArticleId(providerType, articleId, pageable).
                        map(result -> { return this.modelMapper.map(result, KnowledgeBaseArticleLinkageDto.class);});

        return new PaginationRecord<KnowledgeBaseArticleLinkageDto>(results);
    }


    // Retrieve Article Linkage by ArticleId
    @Transactional(readOnly = true)
    public PaginationRecord<KnowledgeBaseArticleLinkageDto> retrieveArticleLinkagesByArticleId(String articleId,
                                                                                               Pageable pageable) {
        Page<KnowledgeBaseArticleLinkageDto> results =
                this.articleLinkageRepository.findByArticleId(articleId, pageable).map(result -> {
                    return this.modelMapper.map(result, KnowledgeBaseArticleLinkageDto.class);
                });

        return new PaginationRecord<KnowledgeBaseArticleLinkageDto>(results);
    }
}
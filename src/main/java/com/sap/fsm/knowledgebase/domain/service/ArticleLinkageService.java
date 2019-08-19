package com.sap.fsm.knowledgebase.domain.service;

import com.sap.fsm.knowledgebase.domain.dto.PaginationRecord;
import com.sap.fsm.knowledgebase.domain.exception.ArticleLinkageNotExistException;
import com.sap.fsm.knowledgebase.domain.exception.ArticleLinkageExistException;
import com.sap.fsm.knowledgebase.domain.exception.ResourceNotExistException;
import com.sap.fsm.knowledgebase.domain.model.ArticleLinkage;
import com.sap.fsm.knowledgebase.domain.repository.ProviderTypeRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.sap.fsm.knowledgebase.domain.repository.ArticleLinkageRepository;
import com.sap.fsm.knowledgebase.domain.dto.ArticleLinkageDto;
import org.springframework.transaction.annotation.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.UUID;

@Service
@Transactional
public class ArticleLinkageService {
    private static final Logger logger = LoggerFactory.getLogger(ArticleLinkageService.class);
    private final ModelMapper modelMapper;
    private final ArticleLinkageRepository articleLinkageRepository;
    private final ProviderTypeRepository providerTypeRepository;

    @Autowired
    public ArticleLinkageService(ModelMapper modelMapper,
                                 ArticleLinkageRepository articleLinkageRepository,
                                 ProviderTypeRepository providerTypeRepository) {
        this.modelMapper = modelMapper;
        this.articleLinkageRepository = articleLinkageRepository;
        this.providerTypeRepository = providerTypeRepository;
    }

    // Create Article Linkage
    public ArticleLinkageDto createArticleLinkage(ArticleLinkageDto articleLinkageDto) {
        if (null != articleLinkageDto.getId() && articleLinkageRepository.existsById(articleLinkageDto.getId())) {
            final String errorMsg = String.format("ArticleLinkage already present for id: %s",
                    articleLinkageDto.getId().toString());
            logger.error(errorMsg);
            throw new ArticleLinkageExistException(errorMsg);
        }

        providerTypeRepository
                .findByCode(articleLinkageDto.getProviderType())
                .orElseThrow(() -> new ResourceNotExistException(articleLinkageDto.getProviderType()));

        ArticleLinkage requestedLinkageModel = this.modelMapper.map(articleLinkageDto,
                ArticleLinkage.class);
        ArticleLinkage responseLinkageModel = this.articleLinkageRepository.save(requestedLinkageModel);
        return this.modelMapper.map(responseLinkageModel, ArticleLinkageDto.class);
    }

    // Delete Article Linkage by Id
    public void deleteArticleLinkageById(UUID articleLinkageId) {
        if (this.articleLinkageRepository.existsById(articleLinkageId)) {
            this.articleLinkageRepository.deleteById(articleLinkageId);
        } else {
            ArticleLinkageNotExistException ex = new ArticleLinkageNotExistException(articleLinkageId.toString());
            logger.error(ex.getMessage());
            throw ex;
        }
    }

    // Retrieve Article Linkage by ObjectId and ObjectType
    @Transactional(readOnly = true)
    public PaginationRecord<ArticleLinkageDto> retrieveArticleLinkagesByObjectIDAndType(String objectType,
                                                                                        String objectId,
                                                                                        Pageable pageable) {
        Page<ArticleLinkageDto> results =
                this.articleLinkageRepository.findByObjectTypeAndObjectId(objectType, objectId, pageable).
                        map(result -> { return this.modelMapper.map(result, ArticleLinkageDto.class);});

        return new PaginationRecord<ArticleLinkageDto>(results);
    }

    // Retrieve Article Linkage by ArticleId and ProviderType
    @Transactional(readOnly = true)
    public PaginationRecord<ArticleLinkageDto> retrieveArticleLinkagesByProviderTypeAndArticleId(String providerType,
                                                                                                 String articleId,
                                                                                                 Pageable pageable) {
        Page<ArticleLinkageDto> results =
                this.articleLinkageRepository.findByProviderTypeAndArticleId(providerType, articleId, pageable).
                        map(result -> { return this.modelMapper.map(result, ArticleLinkageDto.class);});

        return new PaginationRecord<ArticleLinkageDto>(results);
    }


    // Retrieve Article Linkage by ArticleId
    @Transactional(readOnly = true)
    public PaginationRecord<ArticleLinkageDto> retrieveArticleLinkagesByArticleId(String articleId,
                                                                                  Pageable pageable) {
        Page<ArticleLinkageDto> results =
                this.articleLinkageRepository.findByArticleId(articleId, pageable).map(result -> {
                    return this.modelMapper.map(result, ArticleLinkageDto.class);
                });

        return new PaginationRecord<ArticleLinkageDto>(results);
    }
}
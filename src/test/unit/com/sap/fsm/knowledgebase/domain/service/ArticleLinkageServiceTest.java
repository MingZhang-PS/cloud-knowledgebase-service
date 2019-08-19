package com.sap.fsm.knowledgebase.domain.service;

import com.sap.fsm.knowledgebase.domain.dto.ArticleLinkageDto;
import com.sap.fsm.knowledgebase.domain.dto.PaginationRecord;
import com.sap.fsm.knowledgebase.domain.exception.ArticleLinkageExistException;
import com.sap.fsm.knowledgebase.domain.exception.ArticleLinkageNotExistException;
import com.sap.fsm.knowledgebase.domain.model.ArticleLinkage;
import com.sap.fsm.knowledgebase.domain.model.ProviderType;
import com.sap.fsm.knowledgebase.domain.repository.ArticleLinkageRepository;
import com.sap.fsm.knowledgebase.domain.repository.ProviderTypeRepository;
import com.sap.fsm.springboot.starter.test.annotation.Unit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.junit.Assert.assertEquals;


@Unit
public class ArticleLinkageServiceTest {
    @Mock
    private ArticleLinkageRepository articleLinkageRepository;

    @Mock
    private ProviderTypeRepository providerTypeRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    ArticleLinkageService articleLinkageService;

    @BeforeEach
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createArticleLinkageForOk() {
        // Mock
        ArticleLinkageDto articleLinkageDto =
                new ArticleLinkageDto();
        articleLinkageDto.setId(UUID.randomUUID());

        ArticleLinkage articleLinkageModel =
                new ArticleLinkage();

        when(modelMapper.map(articleLinkageDto, ArticleLinkage.class)).thenReturn(articleLinkageModel);
        when(articleLinkageRepository.save(articleLinkageModel)).thenReturn(articleLinkageModel);
        when(articleLinkageRepository.existsById(articleLinkageDto.getId())).thenReturn(false);
        when(providerTypeRepository.findByCode(articleLinkageDto.getProviderType())).thenReturn(Optional.of(new ProviderType()));
        when(modelMapper.map(articleLinkageModel, ArticleLinkageDto.class)).thenReturn(articleLinkageDto);

        // Test
        ArticleLinkageDto result = articleLinkageService.createArticleLinkage(articleLinkageDto);
        assertEquals(result.getId(), articleLinkageDto.getId());
        verify(articleLinkageRepository, times(1)).save(articleLinkageModel);
    }

    @Test
    public void createArticleLinkageForFail() {
        ArticleLinkageDto articleLinkageDto =
                new ArticleLinkageDto();
        articleLinkageDto.setId(UUID.randomUUID());
        when(articleLinkageRepository.existsById(articleLinkageDto.getId())).thenReturn(true);

        Assertions.assertThrows(ArticleLinkageExistException.class, () -> {
                articleLinkageService.createArticleLinkage(articleLinkageDto);
                      }, String.format("ArticleLinkage already present for id: %s",
                      articleLinkageDto.getId().toString()));      
    }

    @Test
    public void deleteArticleLinkageByIdForOk() {
        UUID id = UUID.randomUUID();
        when(articleLinkageRepository.existsById(id)).thenReturn(true);

        articleLinkageService.deleteArticleLinkageById(id);

        verify(articleLinkageRepository, times(1)).deleteById(id);
    }

    @Test
    public void deleteArticleLinkageByIdForFail()  {
        UUID id = UUID.randomUUID();
        when(articleLinkageRepository.existsById(id)).thenReturn(false);

        Assertions.assertThrows(ArticleLinkageNotExistException.class, () -> {
                articleLinkageService.deleteArticleLinkageById(id);
                      }, new ArticleLinkageNotExistException(id.toString()).getMessage());     
    }

    @Test
    public void retrieveArticleLinkagesByObjectIDAndType() {
        String objectType = "Case", objectId = "case_1";
        Pageable pageable = PageRequest.of(0, 10);

        List<ArticleLinkage> modelContent =
                new ArrayList<ArticleLinkage>(0);
        Page<ArticleLinkage> page =
                new PageImpl<ArticleLinkage>(modelContent);
        when(articleLinkageRepository.findByObjectTypeAndObjectId(objectType, objectId, pageable)).thenReturn(page);


        PaginationRecord<ArticleLinkageDto> result =
                articleLinkageService.retrieveArticleLinkagesByObjectIDAndType(objectType, objectId, pageable);

        verify(articleLinkageRepository, times(1)).
                findByObjectTypeAndObjectId(objectType, objectId, pageable);
        assertEquals(result.getSize(), 0);
        assertEquals(result.getNumber(), 0);
    }

    @Test
    public void retrieveArticleLinkagesByProviderTypeAndArticleId() {
        String providerType = "MindTouch", articleId = "case_1";
        Pageable pageable = PageRequest.of(0, 10);

        List<ArticleLinkage> modelContent =
                new ArrayList<ArticleLinkage>(0);
        Page<ArticleLinkage> page =
                new PageImpl<ArticleLinkage>(modelContent);
        when(articleLinkageRepository.findByProviderTypeAndArticleId(providerType, articleId, pageable)).thenReturn(page);

        PaginationRecord<ArticleLinkageDto> result =
                articleLinkageService.retrieveArticleLinkagesByProviderTypeAndArticleId(providerType, articleId, pageable);

        verify(articleLinkageRepository, times(1)).
                findByProviderTypeAndArticleId(providerType, articleId, pageable);
        assertEquals(result.getSize(), 0);
        assertEquals(result.getNumber(), 0);
    }

    @Test
    public void retrieveArticleLinkagesByArticleId() {
        String articleId = "case_1";
        Pageable pageable = PageRequest.of(0, 10);

        List<ArticleLinkage> modelContent =
                new ArrayList<ArticleLinkage>(0);
        Page<ArticleLinkage> page =
                new PageImpl<ArticleLinkage>(modelContent);
        when(articleLinkageRepository.findByArticleId(articleId, pageable)).thenReturn(page);

        PaginationRecord<ArticleLinkageDto> result =
                articleLinkageService.retrieveArticleLinkagesByArticleId(articleId, pageable);

        verify(articleLinkageRepository, times(1)).
                findByArticleId(articleId, pageable);
        assertEquals(result.getSize(), 0);
        assertEquals(result.getNumber(), 0);
    }
}
package com.sap.fsm.knowledgebase.domain.service;

import com.sap.fsm.knowledgebase.domain.dto.KnowledgeBaseArticleLinkageDto;
import com.sap.fsm.knowledgebase.domain.dto.PaginationRecord;
import com.sap.fsm.knowledgebase.domain.exception.ArticleLinkageExistException;
import com.sap.fsm.knowledgebase.domain.exception.ArticleLinkageNotExistException;
import com.sap.fsm.knowledgebase.domain.model.KnowledgeBaseArticleLinkage;
import com.sap.fsm.knowledgebase.domain.repository.KnowledgeBaseArticleLinkageRepository;
import com.sap.fsm.springboot.starter.test.annotation.Unit;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
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
import java.util.UUID;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.times;
import static org.junit.Assert.assertEquals;


@Unit
public class KnowledgeBaseArticleLinkageServiceTest {
    @Mock
    private KnowledgeBaseArticleLinkageRepository articleLinkageRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    KnowledgeBaseArticleLinkageService articleLinkageService;

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void createArticleLinkageForOk() {
        // Mock
        KnowledgeBaseArticleLinkageDto articleLinkageDto =
                new KnowledgeBaseArticleLinkageDto();
        articleLinkageDto.setId(UUID.randomUUID());

        KnowledgeBaseArticleLinkage articleLinkageModel =
                new KnowledgeBaseArticleLinkage();

        when(modelMapper.map(articleLinkageDto, KnowledgeBaseArticleLinkage.class)).thenReturn(articleLinkageModel);
        when(articleLinkageRepository.save(articleLinkageModel)).thenReturn(articleLinkageModel);
        when(articleLinkageRepository.existsById(articleLinkageDto.getId())).thenReturn(false);
        when(modelMapper.map(articleLinkageModel, KnowledgeBaseArticleLinkageDto.class)).thenReturn(articleLinkageDto);

        // Test
        KnowledgeBaseArticleLinkageDto result = articleLinkageService.createArticleLinkage(articleLinkageDto);
        assertEquals(result.getId(), articleLinkageDto.getId());
        verify(articleLinkageRepository, times(1)).save(articleLinkageModel);
    }

    @Test
    public void createArticleLinkageForFail() throws ArticleLinkageExistException {
        KnowledgeBaseArticleLinkageDto articleLinkageDto =
                new KnowledgeBaseArticleLinkageDto();
        articleLinkageDto.setId(UUID.randomUUID());
        when(articleLinkageRepository.existsById(articleLinkageDto.getId())).thenReturn(true);

        thrown.expect(ArticleLinkageExistException.class);
        thrown.expectMessage(String.format("ArticleLinkage already present for id: %s",
                articleLinkageDto.getId().toString()));

        articleLinkageService.createArticleLinkage(articleLinkageDto);
    }

    @Test
    public void deleteArticleLinkageByIdForOk() {
        UUID id = UUID.randomUUID();
        when(articleLinkageRepository.existsById(id)).thenReturn(true);

        articleLinkageService.deleteArticleLinkageById(id);

        verify(articleLinkageRepository, times(1)).deleteById(id);
    }

    @Test
    public void deleteArticleLinkageByIdForFail() throws ArticleLinkageNotExistException {
        UUID id = UUID.randomUUID();
        when(articleLinkageRepository.existsById(id)).thenReturn(false);

        thrown.expect(ArticleLinkageNotExistException.class);
        thrown.expectMessage(new ArticleLinkageNotExistException(id).getMessage());

        articleLinkageService.deleteArticleLinkageById(id);
    }

    @Test
    public void retrieveArticleLinkagesByObjectIDAndType() {
        String objectType = "Case", objectId = "case_1";
        Pageable pageable = PageRequest.of(0, 10);

        List<KnowledgeBaseArticleLinkage> modelContent =
                new ArrayList<KnowledgeBaseArticleLinkage>(0);
        Page<KnowledgeBaseArticleLinkage> page =
                new PageImpl<KnowledgeBaseArticleLinkage>(modelContent);
        when(articleLinkageRepository.findByObjectTypeAndObjectId(objectType, objectId, pageable)).thenReturn(page);


        PaginationRecord<KnowledgeBaseArticleLinkageDto> result =
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

        List<KnowledgeBaseArticleLinkage> modelContent =
                new ArrayList<KnowledgeBaseArticleLinkage>(0);
        Page<KnowledgeBaseArticleLinkage> page =
                new PageImpl<KnowledgeBaseArticleLinkage>(modelContent);
        when(articleLinkageRepository.findByProviderTypeAndArticleId(providerType, articleId, pageable)).thenReturn(page);

        PaginationRecord<KnowledgeBaseArticleLinkageDto> result =
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

        List<KnowledgeBaseArticleLinkage> modelContent =
                new ArrayList<KnowledgeBaseArticleLinkage>(0);
        Page<KnowledgeBaseArticleLinkage> page =
                new PageImpl<KnowledgeBaseArticleLinkage>(modelContent);
        when(articleLinkageRepository.findByArticleId(articleId, pageable)).thenReturn(page);

        PaginationRecord<KnowledgeBaseArticleLinkageDto> result =
                articleLinkageService.retrieveArticleLinkagesByArticleId(articleId, pageable);

        verify(articleLinkageRepository, times(1)).
                findByArticleId(articleId, pageable);
        assertEquals(result.getSize(), 0);
        assertEquals(result.getNumber(), 0);
    }
}
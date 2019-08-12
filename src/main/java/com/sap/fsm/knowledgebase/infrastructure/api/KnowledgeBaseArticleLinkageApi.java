package com.sap.fsm.knowledgebase.infrastructure.api;

import com.sap.fsm.knowledgebase.domain.dto.KnowledgeBaseArticleLinkageDto;
import com.sap.fsm.knowledgebase.domain.dto.PaginationRecord;
import com.sap.fsm.knowledgebase.domain.service.KnowledgeBaseArticleLinkageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping(path = "/api/knowledge-base/v1/",
        consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE},
        produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
@Validated
public class KnowledgeBaseArticleLinkageApi {
    private static final String RESOURCE_URL_NAME = "article-linkages";
    private final KnowledgeBaseArticleLinkageService articleLinkageService;

    @Autowired
    public KnowledgeBaseArticleLinkageApi(KnowledgeBaseArticleLinkageService articleLinkageService) {
        this.articleLinkageService = articleLinkageService;
    }

    @PostMapping(value = RESOURCE_URL_NAME)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public KnowledgeBaseArticleLinkageDto createArticleLinkage(@RequestBody KnowledgeBaseArticleLinkageDto articleLinkageDto) {
        return articleLinkageService.createArticleLinkage(articleLinkageDto);
    }

    @DeleteMapping(value = RESOURCE_URL_NAME + "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteArticleLinkageById(@PathVariable(value = "id") UUID id) {
        articleLinkageService.deleteArticleLinkageById(id);
    }

    @GetMapping(value = RESOURCE_URL_NAME, params = {"objectType", "objectId"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public PaginationRecord<KnowledgeBaseArticleLinkageDto> findArticleLinkagesByObjectTypeAndId(@RequestParam(value = "objectType") String objectType,
                                                                                                 @RequestParam(value = "objectId") String objectId,
                                                                                                 Pageable pageable) {
        return articleLinkageService.retrieveArticleLinkagesByObjectIDAndType(objectType, objectId, pageable);
    }

    @GetMapping(value = RESOURCE_URL_NAME, params = {"articleId"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public PaginationRecord<KnowledgeBaseArticleLinkageDto> findArticleLinkagesByArticleId(@RequestParam(value = "articleId") String articleId,
                                                                                           Pageable pageable) {
        return articleLinkageService.retrieveArticleLinkagesByArticleId(articleId, pageable);
    }

    @GetMapping(value = RESOURCE_URL_NAME, params = {"providerType", "articleId"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public PaginationRecord<KnowledgeBaseArticleLinkageDto> findArtcileLinkagesByProviderTypeAndArticleId(@RequestParam(value = "providerType") String providerType,
                                                                                                          @RequestParam(value = "articleId") String articleId,
                                                                                                          Pageable pageable) {
        return articleLinkageService.retrieveArticleLinkeageByProviderTypeAndArticleId(providerType, articleId, pageable);
    }
}
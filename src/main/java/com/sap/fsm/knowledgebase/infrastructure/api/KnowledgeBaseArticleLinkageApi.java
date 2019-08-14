package com.sap.fsm.knowledgebase.infrastructure.api;

import com.sap.fsm.knowledgebase.domain.dto.ArticleLinkageDto;
import com.sap.fsm.knowledgebase.domain.dto.PaginationRecord;
import com.sap.fsm.knowledgebase.domain.service.ArticleLinkageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import java.util.UUID;

@Api(value = "KnowledgeBase Article Linkage API", tags = {"KnowledgeBase Article Linkage API"})
@RestController
@Validated
@RequestMapping(path = "/api/knowledge-base/v1/")
public class KnowledgeBaseArticleLinkageApi {
    private static final String RESOURCE_URL_NAME = "article-linkages";
    private final ArticleLinkageService articleLinkageService;

    @Autowired
    public KnowledgeBaseArticleLinkageApi(ArticleLinkageService articleLinkageService) {
        this.articleLinkageService = articleLinkageService;
    }

    @ApiOperation(value = "Create a KnowledgeBase Article Linkage")
    @PostMapping(value = RESOURCE_URL_NAME,
            consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE},
            produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseStatus(value = HttpStatus.CREATED)
    @ResponseBody
    public ArticleLinkageDto createArticleLinkage(@RequestBody ArticleLinkageDto articleLinkageDto) {
        return articleLinkageService.createArticleLinkage(articleLinkageDto);
    }

    @ApiOperation(value = "Delete a KnowledgeBase Article Linkage by articleLinkage Id")
    @DeleteMapping(value = RESOURCE_URL_NAME + "/{id}")
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void deleteArticleLinkageById(@PathVariable(value = "id") UUID id) {
        articleLinkageService.deleteArticleLinkageById(id);
    }

    @ApiIgnore()
    @GetMapping(value = RESOURCE_URL_NAME, params = {"objectType", "objectId"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public PaginationRecord<ArticleLinkageDto> findArticleLinkagesByObjectTypeAndId(@RequestParam(value = "objectType") String objectType,
                                                                                    @RequestParam(value = "objectId") String objectId,
                                                                                    Pageable pageable) {
        return articleLinkageService.retrieveArticleLinkagesByObjectIDAndType(objectType, objectId, pageable);
    }

    @ApiIgnore()
    @GetMapping(value = RESOURCE_URL_NAME, params = {"articleId"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public PaginationRecord<ArticleLinkageDto> findArticleLinkagesByArticleId(@RequestParam(value = "articleId") String articleId,
                                                                              Pageable pageable) {
        return articleLinkageService.retrieveArticleLinkagesByArticleId(articleId, pageable);
    }

    @ApiOperation(value = "Get list of KnowledgeBase Article Linkage by (providerType and articleId) or (articleId) or (objectType and objectId)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "objectType", dataType = "string", paramType = "query", value = "Object Type, such as: Case", required = true),
            @ApiImplicitParam(name = "objectId", dataType = "string", paramType = "query", value = "Object Id", required = true),
            @ApiImplicitParam(name = "page", dataType = "integer", paramType = "query", value = "Results page you want to retrieve (0..N)", defaultValue = "0"),
            @ApiImplicitParam(name = "size", dataType = "integer", paramType = "query", value = "Number of records per page.", defaultValue = "20"),
            @ApiImplicitParam(name = "sort", allowMultiple = true, dataType = "string", paramType = "query",
                    value = "Sorting criteria in the format: property(,asc|desc). Default sort order is ascending. Multiple sort criteria are supported.")
    })
    @GetMapping(value = RESOURCE_URL_NAME, params = {"providerType", "articleId"})
    @ResponseStatus(value = HttpStatus.OK)
    @ResponseBody
    public PaginationRecord<ArticleLinkageDto> findArticleLinkagesByProviderTypeAndArticleId(@RequestParam(value = "providerType") String providerType,
                                                                                             @RequestParam(value = "articleId") String articleId,
                                                                                             @ApiIgnore("Ignored because swagger ui shows the wrong params, " +
                                                                                                                  "instead they are explained in the implicit params")
                                                                                                                  Pageable pageable) {
        return articleLinkageService.retrieveArticleLinkagesByProviderTypeAndArticleId(providerType, articleId, pageable);
    }
}
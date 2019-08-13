package com.sap.fsm.knowledgebase.infrastructure.api;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import com.sap.fsm.knowledgebase.domain.model.KnowledgeBaseArticleLinkage;
import com.sap.fsm.knowledgebase.domain.repository.KnowledgeBaseArticleLinkageRepository;
import com.sap.fsm.knowledgebase.domain.dto.KnowledgeBaseArticleLinkageDto;
import com.sap.fsm.springboot.starter.test.annotation.Integration;

import org.junit.jupiter.api.*;
import org.modelmapper.ModelMapper;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.MultiValueMap;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.assertj.core.api.Assertions.assertThat;

@Integration
@Transactional
public class KnowledgeBaseArticleLinkageApiTest {

    private static final String RESOURCE_URL_NAME = "/api/knowledge-base/v1/article-linkages";
    private final MockMvc mockMvc;
    private final ObjectMapper objectMapper;
    private final ModelMapper modelMapper;
    private final KnowledgeBaseArticleLinkageRepository articleLinkageRepository;
    private KnowledgeBaseArticleLinkageDto articleLinkageDto;
    private UUID articleLinkageId;

    @Autowired
    public KnowledgeBaseArticleLinkageApiTest(MockMvc mockMvc,
                                              ObjectMapper objectMapper,
                                              ModelMapper modelMapper,
                                              KnowledgeBaseArticleLinkageRepository articleLinkageRepository) {
        this.mockMvc = mockMvc;
        this.objectMapper = objectMapper;
        this.modelMapper = modelMapper;
        this.articleLinkageRepository = articleLinkageRepository;
        this.articleLinkageDto = null;
        this.articleLinkageId = null;
    }

    private String asJsonString(final Object obj) {
        try {
            return this.objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private UUID addNewArticleLinkageToDB() {
        if (null != this.articleLinkageDto) {
            KnowledgeBaseArticleLinkage articleLinkageModel =
                    this.modelMapper.map(this.articleLinkageDto, KnowledgeBaseArticleLinkage.class);
            articleLinkageModel = this.articleLinkageRepository.save(articleLinkageModel);

            return articleLinkageModel.getId();
        } else {
            return null;
        }
    }

    private boolean verifyArticleLinkageFromDB(UUID articleLinkageId) {
        return this.articleLinkageRepository.existsById(articleLinkageId);
    }

    private KnowledgeBaseArticleLinkageDto createArticleLinkageDto(String providerType,
                                                                   String articleId,
                                                                   String objectType,
                                                                   String objectId) {
        KnowledgeBaseArticleLinkageDto articleLinkageDto =
                new KnowledgeBaseArticleLinkageDto();

        articleLinkageDto.setProviderType(providerType);
        articleLinkageDto.setArticleId(articleId);
        articleLinkageDto.setObjectType(objectType);
        articleLinkageDto.setObjectId(objectId);

        return articleLinkageDto;
    }

    private int prepareRetrievedData(int initialCapacity) {
        List<KnowledgeBaseArticleLinkage> articleLinkageDtoList =
                new ArrayList<>(initialCapacity);
        int nIndex = 0;
        KnowledgeBaseArticleLinkage articleLinkageModel = null;
        while (nIndex++ < initialCapacity) {
            articleLinkageModel = this.modelMapper.map(this.createArticleLinkageDto(
                    "MindTouch",
                    "article_" + nIndex,
                    "Case",
                    "case_" + nIndex), KnowledgeBaseArticleLinkage.class);
            articleLinkageDtoList.add(articleLinkageModel);

            articleLinkageModel = this.modelMapper.map(this.createArticleLinkageDto(
                    "SAP-Native",
                    "article_" + nIndex,
                    "Case",
                    "case_" + nIndex), KnowledgeBaseArticleLinkage.class);
            articleLinkageDtoList.add(articleLinkageModel);

            articleLinkageModel = this.modelMapper.map(this.createArticleLinkageDto(
                    "SAP-Native",
                    "article_" + nIndex,
                    "ServiceMoments",
                    "serviceMoments_" + nIndex), KnowledgeBaseArticleLinkage.class);
            articleLinkageDtoList.add(articleLinkageModel);
        }
        this.articleLinkageRepository.saveAll(articleLinkageDtoList);

        return articleLinkageDtoList.size();
    }

    @BeforeEach
    public void initTestResources() {
        this.articleLinkageRepository.deleteAll();
        this.articleLinkageDto =
                this.createArticleLinkageDto(
                        "MindTouch",
                        "article_1",
                        "Case",
                        "case_1");
    }

    @AfterEach
    public void cleanupInitResources() {
        this.articleLinkageDto = null;
    }

    @Test
    public void createArticleLinkage() throws Exception {
        // First save
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.
                post(RESOURCE_URL_NAME).
                content(this.asJsonString(this.articleLinkageDto)).
                contentType(MediaType.APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON_UTF8)).
                andExpect(status().isCreated()).
                andReturn();

        final KnowledgeBaseArticleLinkageDto resultDto =
                this.objectMapper.readValue(mvcResult.getResponse().getContentAsString(), KnowledgeBaseArticleLinkageDto.class);
        assertThat(resultDto.getId()).isNotNull();
        this.articleLinkageId = resultDto.getId();

        // Second save
        this.articleLinkageDto.setId(articleLinkageId);
        mockMvc.perform(MockMvcRequestBuilders.
                post(RESOURCE_URL_NAME).
                content(this.asJsonString(this.articleLinkageDto)).
                contentType(MediaType.APPLICATION_JSON_UTF8).
                accept(MediaType.APPLICATION_JSON_UTF8)).
                andExpect(status().is5xxServerError());
    }

    @Test
    public void deleteArticleLinkage() throws Exception {
        // Step 1, Add new articleLinkage record to db
        UUID articleLinkageId = this.addNewArticleLinkageToDB();

        // Step 2, Invoke delete endpoint to remove record from db
        if (null != articleLinkageId) {
            mockMvc.perform(MockMvcRequestBuilders.
                    delete(RESOURCE_URL_NAME + "/" + articleLinkageId.toString())).
                    andExpect(status().isNoContent());

            // Step 3, Verify this articleLinkage is not found from DB
            assertThat(this.verifyArticleLinkageFromDB(articleLinkageId)).isFalse();
        }

        // Step 4, delete again, it will throw exception
        if (null != articleLinkageId) {
            mockMvc.perform(MockMvcRequestBuilders.
                    delete(RESOURCE_URL_NAME + "/" + articleLinkageId.toString())).
                    andExpect(status().is4xxClientError());
        }
    }

    @Test
    public void findArticleLinkagesByObjectTypeAndId() throws Exception {
        // Prepare test data
        prepareRetrievedData(3);

        // Perform get endpoint
        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("objectType", "Case");
        params.set("objectId", "case_1");
        params.set("size", "10");
        params.set("page", "0");
        params.set("sort", "providerType,asc");

        // Test and verify
        mockMvc.perform(MockMvcRequestBuilders.
                get(RESOURCE_URL_NAME).
                accept(MediaType.APPLICATION_JSON_UTF8).
                params(params)).
                andExpect(status().isOk()).
                andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").value(2)).
                andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(2)).
                andExpect(MockMvcResultMatchers.jsonPath("$.results").isArray()).
                andExpect(MockMvcResultMatchers.jsonPath("$.results[0].id").exists()).
                andExpect(MockMvcResultMatchers.jsonPath("$.results[0].providerType").value("MindTouch")).
                andExpect(MockMvcResultMatchers.jsonPath("$.results[1].id").exists()).
                andExpect(MockMvcResultMatchers.jsonPath("$.results[1].providerType").value("SAP-Native")).
                andExpect(MockMvcResultMatchers.jsonPath("$.results[2].id").doesNotHaveJsonPath()).
                andReturn();
    }


    @Test
    public void findArticleLinkagesByArticleId() throws Exception {
        // Prepare test data
        prepareRetrievedData(3);

        // Perform get endpoint
        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("articleId", "article_1");
        params.set("size", "10");
        params.set("page", "0");
        params.set("sort", "providerType,asc");

        // Test and verify
        mockMvc.perform(MockMvcRequestBuilders.
                get(RESOURCE_URL_NAME).
                accept(MediaType.APPLICATION_JSON_UTF8).
                params(params)).
                andExpect(status().isOk()).
                andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").value(3)).
                andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(3)).
                andExpect(MockMvcResultMatchers.jsonPath("$.results").isArray()).
                andExpect(MockMvcResultMatchers.jsonPath("$.results[0].id").exists()).
                andExpect(MockMvcResultMatchers.jsonPath("$.results[0].providerType").value("MindTouch")).
                andExpect(MockMvcResultMatchers.jsonPath("$.results[1].id").exists()).
                andExpect(MockMvcResultMatchers.jsonPath("$.results[1].providerType").value("SAP-Native")).
                andExpect(MockMvcResultMatchers.jsonPath("$.results[2].id").exists()).
                andExpect(MockMvcResultMatchers.jsonPath("$.results[2].providerType").value("SAP-Native")).
                andReturn();
    }

    @Test
    public void findArticleLinkagesByProviderTypeAndArticleId() throws Exception {
        // Prepare test data
        prepareRetrievedData(3);

        // Perform get endpoint
        MultiValueMap<String, String> params = new HttpHeaders();
        params.set("articleId", "article_1");
        params.set("providerType", "SAP-Native");
        params.set("size", "10");
        params.set("page", "0");

        // Test and verify
        mockMvc.perform(MockMvcRequestBuilders.
                get(RESOURCE_URL_NAME).
                accept(MediaType.APPLICATION_JSON_UTF8).
                params(params)).
                andExpect(status().isOk()).
                andExpect(MockMvcResultMatchers.jsonPath("$.numberOfElements").value(2)).
                andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(2)).
                andExpect(MockMvcResultMatchers.jsonPath("$.results").isArray()).
                andExpect(MockMvcResultMatchers.jsonPath("$.results[0].id").exists()).
                andExpect(MockMvcResultMatchers.jsonPath("$.results[0].objectType").value("Case")).
                andExpect(MockMvcResultMatchers.jsonPath("$.results[1].id").exists()).
                andExpect(MockMvcResultMatchers.jsonPath("$.results[1].objectType").value("ServiceMoments")).
                andReturn();
    }
}
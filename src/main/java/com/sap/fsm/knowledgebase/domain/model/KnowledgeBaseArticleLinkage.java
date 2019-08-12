package com.sap.fsm.knowledgebase.domain.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Valid
@Data
@Entity(name = "knowledgebasearticlelinkage")
public class KnowledgeBaseArticleLinkage {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid2")
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private UUID id;

    @NotNull
    @Column(name = "articleId", nullable = false, length = 512)
    private String articleId;

    @NotNull
    @Column(name = "providerType", nullable = false, length = 512)
    private String providerType;

    @NotNull
    @Column(name = "objectType", nullable = false, length = 512)
    private String objectType;

    @NotNull
    @Column(name = "objectId", nullable = false, length = 512)
    private String objectId;
}
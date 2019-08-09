package com.sap.fsm.knowledgebase.domain.model;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import javax.validation.Valid;
import java.util.UUID;

@Entity
@Table(name = "knowledgebasearticlelinkage")
@Valid
@Data
public class KnowledgeBaseArticleLinkage {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid2")
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private UUID id;

    @Column(name = "articleId", nullable = false, length = 512)
    private String articleId;

    @Column(name = "providerType", nullable = false, length = 512)
    private String providerType;

    @Column(name = "objectType", nullable = false, length = 512)
    private String objectType;

    @Column(name = "objectId", nullable = false, length = 512)
    private String objectId;
}
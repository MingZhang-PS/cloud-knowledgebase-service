package com.sap.fsm.knowledgebase.domain.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.*;

@Entity
@Table(name = "knowledgebaseprovidertype")
@Valid
@Data
public class KnowledgeBaseProviderType {
    @Id
    @GeneratedValue(generator="system-uuid")
    @GenericGenerator(name="system-uuid", strategy = "uuid2")
    @Column(name = "id", unique = true)
    private UUID id;

    @NotNull
    @Column(name = "lastchanged")
    @Temporal(TemporalType.TIMESTAMP)
    @Version
    private Date lastChanged;

    @NotEmpty
    @Column(nullable = false, updatable = false)
    @Length(max = 512)
    private String code;

    @Column
    @Length(max = 512)
    private String name;

    @PrePersist
    @PreUpdate
    private void beforeUpdate() {
        this.lastChanged = this.lastChanged ==  null? new Date(): this.lastChanged;
    }  
}

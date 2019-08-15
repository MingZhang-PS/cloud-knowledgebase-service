package com.sap.fsm.knowledgebase.domain.model;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.Date;

@Entity
@Table(name = "knowledgebaseprovidertype")
@Valid
@Data
public class ProviderType {
    @Id
    @NotBlank
    @Column(name = "code", nullable = false, updatable = false,  unique = true)
    @Length(max = 512)
    private String code;

    @NotNull
    @Column(name = "lastchanged")
    @Temporal(TemporalType.TIMESTAMP)
    @Version
    private Date lastChanged;

    @Column
    @Length(max = 512)
    private String name;

/*     @OneToOne(mappedBy = "providerType", fetch = FetchType.LAZY)
    private KnowledgeBaseProviderConfiguration providerConfiguration; */
 
    @PrePersist
    @PreUpdate
    private void beforeUpdate() {
        this.lastChanged = this.lastChanged ==  null? new Date(): this.lastChanged;
    }  
}

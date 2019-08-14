package com.sap.fsm.knowledgebase.domain.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.hibernate.validator.constraints.Length;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;

import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.Date;
import java.util.UUID;;

@Entity
@Table(name = "knowledgebaseproviderconfiguration")
@Valid
@Data
public class ProviderConfiguration {
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

    @NotNull
    @Column(name = "providertype", nullable = false, updatable = false,  unique = true)
    private UUID providerType;
    
    
/*     @JoinColumn(name = "providertype", referencedColumnName = "id", nullable = false, unique = true, updatable = false)
    @OneToOne( fetch = FetchType.LAZY, optional = false)
    private KnowledgeBaseProviderType providerType; */

    @Column
    @NotNull
    private Boolean isActive;

    @Column
    @Length(max = 255)
    private String adapterAuthType;

    @Column
    @Length(max = 512)
    private String adapterURL;

    @Column
    private String adapterCredential;
    
    @Column
    @Length(max = 255)
    private String siteAuthType;

    @Column
    @Length(max = 512)
    private String siteURL;

    @Column
    private String siteCredential;

    @PrePersist
    @PreUpdate
    private void beforeUpdate() {
        this.lastChanged = this.lastChanged ==  null? new Date(): this.lastChanged;
    }  
}

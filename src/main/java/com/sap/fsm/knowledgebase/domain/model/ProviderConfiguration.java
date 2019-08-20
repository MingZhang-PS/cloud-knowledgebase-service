package com.sap.fsm.knowledgebase.domain.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;
import org.springframework.data.domain.AfterDomainEventPublication;
import org.springframework.data.domain.DomainEvents;

import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.Collection;
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
    private String providerType;
    
    
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

    @DomainEvents
    public Collection<Object> domainEvents() {
        // TODO: Event raised, and suppose Kafka event processor will listen and react
        // https://wiselyman.iteye.com/blog/2380261
        return null;
    }

    @AfterDomainEventPublication
    public void callbackMethod() {
        System.out.println("callbackMethod什么时候调用呢");
    }
}

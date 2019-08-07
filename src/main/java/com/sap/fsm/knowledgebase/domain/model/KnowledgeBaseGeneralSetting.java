package com.sap.fsm.knowledgebase.domain.model;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.validator.constraints.Length;

import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.util.UUID;
import java.util.Date;

@Entity
@Table(name = "knowledgebasegeneralsetting")
@Valid
@Data
public class KnowledgeBaseGeneralSetting {
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

    @NotBlank
    @Column(nullable = false, updatable = false)
    @Length(max = 512)
    private String key;

    @Column
    @Length(max = 512)
    private String value;

    @PrePersist
    @PreUpdate
    private void beforeUpdate() {
        this.lastChanged = this.lastChanged ==  null? new Date(): this.lastChanged;
    }  
}

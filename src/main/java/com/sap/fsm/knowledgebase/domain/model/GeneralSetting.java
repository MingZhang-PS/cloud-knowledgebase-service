package com.sap.fsm.knowledgebase.domain.model;

import org.hibernate.validator.constraints.Length;

import lombok.Data;

import javax.persistence.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import java.util.Date;

@Entity
@Table(name = "knowledgebasegeneralsetting")
@Valid
@Data
public class GeneralSetting { 
    @Id
    @Column(name = "key", unique = true, nullable = false,  updatable = false)
    @Length(max = 255)
    private String key;

    @NotNull
    @Column(name = "lastchanged")
    @Temporal(TemporalType.TIMESTAMP)
    @Version
    private Date lastChanged;

    @Column
    @Length(max = 255)
    private String value;

    @PrePersist
    @PreUpdate
    private void beforeUpdate() {
        this.lastChanged = this.lastChanged ==  null? new Date(): this.lastChanged;
    }  
}

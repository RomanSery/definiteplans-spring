package com.definiteplans.dom;

import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "enum_value")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "dp")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class EnumValue implements Serializable {
    private static final long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private int id;

    @Column(name = "type", nullable = false)
    private int type;

    @Column(name = "value", nullable = false)
    private String enumValue;

    public EnumValue() {
    }

    public EnumValue(int id, String enumValue) {
        this.id = id;
        this.enumValue = enumValue;
    }
}

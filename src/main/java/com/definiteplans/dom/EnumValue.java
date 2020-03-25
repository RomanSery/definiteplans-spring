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


@Entity
@Table(name = "enum_value")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "dp")
public class EnumValue implements Serializable {
    private static final long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
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

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEnumValue() {
        return this.enumValue;
    }

    public void setEnumValue(String enumValue) {
        this.enumValue = enumValue;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        EnumValue enumValue = (EnumValue) o;

        if (id != enumValue.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}

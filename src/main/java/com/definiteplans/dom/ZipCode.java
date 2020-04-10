package com.definiteplans.dom;

import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "zip_code")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "dp")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ZipCode implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "zip")
    @EqualsAndHashCode.Include
    private String zip;

    @Column(name = "primary_city")
    private String primaryCity;

    @Column(name = "state")
    private String state;

    @Column(name = "timezone")
    private String timezone;

    @Column(name = "latitude")
    private float latitude;

    @Column(name = "longitude")
    private float longitude;

    public ZipCode() {

    }
}

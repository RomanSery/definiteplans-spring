package com.definiteplans.dom;

import java.io.Serializable;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;


@Entity
@Table(name = "zip_code")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "dp")
public class ZipCode implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "zip")
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

    public String getZip() {
        return this.zip;
    }


    public void setZip(String zip) {
        this.zip = zip;
    }


    public String getPrimaryCity() {
        return this.primaryCity;
    }


    public void setPrimaryCity(String primaryCity) {
        this.primaryCity = primaryCity;
    }


    public String getState() {
        return this.state;
    }


    public void setState(String state) {
        this.state = state;
    }


    public String getTimezone() {
        return this.timezone;
    }


    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }


    public float getLatitude() {
        return this.latitude;
    }


    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }


    public float getLongitude() {
        return this.longitude;
    }


    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}

package com.definiteplans.dom;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "enum_value")
public class Unsubscriber implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "email")
    private String email;


    @Column(name = "unsub_date")
    private LocalDateTime unsubDate;

    public Unsubscriber() {

    }

    public String getEmail() {
        return this.email;
    }


    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getUnsubDate() {
        return unsubDate;
    }

    public void setUnsubDate(LocalDateTime unsubDate) {
        this.unsubDate = unsubDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Unsubscriber that = (Unsubscriber) o;

        if (!email.equals(that.email)) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return email.hashCode();
    }
}

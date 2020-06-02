package com.definiteplans.dom;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "unsubscriber")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Unsubscriber implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "email")
    @EqualsAndHashCode.Include
    private String email;

    @Column(name = "unsub_date")
    private LocalDateTime unsubDate;

    public Unsubscriber() {

    }
}

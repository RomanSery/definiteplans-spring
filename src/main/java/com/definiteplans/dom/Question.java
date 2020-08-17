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
@Table(name = "question")
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_ONLY, region = "dp")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Question implements Serializable {
    private static final long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private int id;

    @Column(name = "question_txt", nullable = false)
    private String questionText;

    public Question() {
    }
}

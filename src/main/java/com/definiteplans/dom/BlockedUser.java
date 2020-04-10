package com.definiteplans.dom;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;


@Entity
@Table(name = "blocked_user")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class BlockedUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    @EqualsAndHashCode.Include
    private int id;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "blocked_user_id", nullable = false)
    private int blockedUserId;

    @Column(name = "blocked_date", nullable = false)
    private LocalDateTime blockedDate;

    public BlockedUser() {

    }

}

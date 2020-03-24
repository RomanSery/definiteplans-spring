package com.coderdreams.definiteplans.dom;

import java.io.Serializable;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "blocked_user")
public class BlockedUser implements Serializable {
    private static final long serialVersionUID = 1L;

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "user_id", nullable = false)
    private int userId;

    @Column(name = "blocked_user_id", nullable = false)
    private int blockedUserId;

    @Column(name = "blocked_date", nullable = false)
    private LocalDateTime blockedDate;

    public BlockedUser() {
    }

    public BlockedUser(int userId, int blockedUserId) {
        this.userId = userId;
        this.blockedUserId = blockedUserId;
    }

    public int getUserId() {
        return this.userId;
    }


    public void setUserId(int userId) {
        this.userId = userId;
    }


    public int getBlockedUserId() {
        return this.blockedUserId;
    }


    public void setBlockedUserId(int blockedUserId) {
        this.blockedUserId = blockedUserId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public LocalDateTime getBlockedDate() {
        return blockedDate;
    }

    public void setBlockedDate(LocalDateTime blockedDate) {
        this.blockedDate = blockedDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        BlockedUser that = (BlockedUser) o;

        if (id != that.id) return false;

        return true;
    }

    @Override
    public int hashCode() {
        return id;
    }
}

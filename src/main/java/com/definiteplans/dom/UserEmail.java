package com.definiteplans.dom;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user_email")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserEmail implements Serializable {
	private static final long serialVersionUID=1L;

	@Id
	@Column(name = "email")
	@EqualsAndHashCode.Include
	private String email;

	@Column(name = "user_id")
	@EqualsAndHashCode.Include
	private Integer userId;

	public UserEmail() {

	}

	public UserEmail(Integer userId, String email) {
		this.userId = userId;
		this.email = email;
	}

}
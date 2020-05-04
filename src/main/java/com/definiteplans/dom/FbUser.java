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
@Table(name = "fb_user")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class FbUser implements Serializable {
	private static final long serialVersionUID=1L;

	@Id
	@Column(name = "user_id", nullable = false)
	@EqualsAndHashCode.Include
	private int userId;

	@Column(name = "fb_id", nullable = false)
	private String fbId;


	@Column(name = "access_token")
	private String accessToken;

	public FbUser() {
		super();
	}

}
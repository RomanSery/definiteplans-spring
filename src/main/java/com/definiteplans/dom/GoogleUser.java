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
@Table(name = "google_user")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GoogleUser implements Serializable {
	private static final long serialVersionUID=1L;

	@Id
	@Column(name = "user_id", nullable = false)
	@EqualsAndHashCode.Include
	private int userId;

	@Column(name = "sub_id", nullable = false)
	private String subId;

	@Column(name = "access_token")
	private String accessToken;

	@Column(name = "id_token")
	private String idToken;

	@Column(name = "img_url")
	private String imgUrl;

	public GoogleUser() {
		super();
	}

}
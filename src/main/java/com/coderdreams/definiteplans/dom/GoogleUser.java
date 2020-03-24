package com.coderdreams.definiteplans.dom;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "google_user")
public class GoogleUser implements Serializable {
	private static final long serialVersionUID=1L;

	@Id
	@Column(name = "user_id", nullable = false)
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

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public String getSubId() {
		return subId;
	}

	public void setSubId(String subId) {
		this.subId = subId;
	}

	public String getIdToken() {
		return idToken;
	}

	public void setIdToken(String idToken) {
		this.idToken = idToken;
	}

	public String getImgUrl() {
		return imgUrl;
	}

	public void setImgUrl(String imgUrl) {
		this.imgUrl = imgUrl;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		GoogleUser fbUser = (GoogleUser) o;

		if (userId != fbUser.userId) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return userId;
	}

}
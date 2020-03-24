package com.coderdreams.definiteplans.dom;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "fb_user")
public class FbUser implements Serializable {
	private static final long serialVersionUID=1L;

	@Id
	@Column(name = "user_id", nullable = false)
	private int userId;

	@Column(name = "fb_id", nullable = false)
	private String fbId;


	@Column(name = "access_token")
	private String accessToken;

	public FbUser() {
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

	public String getFbId() {
		return fbId;
	}

	public void setFbId(String fbId) {
		this.fbId = fbId;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		FbUser fbUser = (FbUser) o;

		if (userId != fbUser.userId) return false;

		return true;
	}

	@Override
	public int hashCode() {
		return userId;
	}

}
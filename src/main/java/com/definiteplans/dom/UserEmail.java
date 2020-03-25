package com.definiteplans.dom;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "user_email")
public class UserEmail implements Serializable {
	private static final long serialVersionUID=1L;

	@Id
	@Column(name = "email")
	private String email;

	@Column(name = "user_id")
	private Integer userId;

	public UserEmail() {

	}

	public UserEmail(Integer userId, String email) {
		this.userId = userId;
		this.email = email;
	}

	public Integer getUserId() { return userId; }
	public void setUserId(Integer userId) { this.userId = userId; }

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;

		UserEmail that = (UserEmail) o;

		if (!userId.equals(that.userId)) return false;
		if (!email.equals(that.email)) return false;

		return true;
	}

	@Override
	public int hashCode() {
		int result = userId.hashCode();
		result = 31 * result + email.hashCode();
		return result;
	}
}
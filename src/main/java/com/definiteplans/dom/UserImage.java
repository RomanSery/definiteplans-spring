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
@Table(name = "user_image")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UserImage implements Serializable {
	private static final long serialVersionUID=1L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "id")
	@EqualsAndHashCode.Include
	private int id;

	@Column(name = "user_id", nullable = false)
	private int userId;

	@Column(name = "timestamp", nullable = false)
	private String timestamp;

	@Column(name = "full_img_url")
	private String fullImgUrl;

	@Column(name = "thumb_img_url")
	private String thumbImgUrl;

	@Column(name = "file_name")
	private String fileName;

	@Column(name = "mime_type")
	private String mimeType;

	@Column(name = "creation_date")
	private LocalDateTime creationDate;

	public UserImage() {

	}
}
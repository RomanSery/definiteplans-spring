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
@Table(name = "chat_msg")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class ChatMsg implements Serializable {
	private static final long serialVersionUID=1L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "id")
	@EqualsAndHashCode.Include
	private int id;

	@Column(name = "from_id", nullable = false)
	private int fromId;

	@Column(name = "to_id", nullable = false)
	private int toId;

	@Column(name = "sent_date", nullable = false)
	private LocalDateTime sentDate;

	@Column(name = "message", nullable = false)
	private String message;

	public ChatMsg() {

	}


}
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
@Table(name = "answer")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class Answer implements Serializable {
	private static final long serialVersionUID=1L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "id")
	@EqualsAndHashCode.Include
	private int id;

	@Column(name = "user_id")
	@EqualsAndHashCode.Include
	private Integer userId;

	@Column(name = "question_id")
	@EqualsAndHashCode.Include
	private Integer questionId;

	@Column(name = "question")
	private String questionTxt;

	@Column(name = "answer")
	private String answerTxt;

	@Column(name = "answered_date")
	private LocalDateTime dateAnswered;

	public Answer() {

	}


}
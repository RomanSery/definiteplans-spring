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
@Table(name = "sent_email")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class SentEmail implements Serializable {
	private static final long serialVersionUID=1L;

	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	@Column(name = "id")
	@EqualsAndHashCode.Include
	private int id;
	
	@Column(name = "email_subject", nullable = false)
	private String emailSubject;
	
	@Column(name = "email_body", nullable = false)
	private String emailBody;
	
	@Column(name = "from_addr", nullable = false)
	private String fromAddr;
	
	@Column(name = "to_addr", nullable = false)
	private String toAddr;
	
	@Column(name = "sent_date")
	protected LocalDateTime emailSentDate;

	public SentEmail() {
		
	}

	public SentEmail(String emailSubject, String emailBody, String fromAddr, String toAddr) {
		super();
		this.emailSubject = emailSubject;
		this.emailBody = emailBody;
		this.fromAddr = fromAddr;
		this.toAddr = toAddr;
	}

}
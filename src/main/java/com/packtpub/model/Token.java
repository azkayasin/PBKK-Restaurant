package com.packtpub.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name="tokens")
@EntityListeners(AuditingEntityListener.class)
public class Token {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="idToken")
	private Integer idToken;
	
	@Column(name="stringToken")
	private String stringToken;
	
	@Column(name="idUser")
	private Integer idUser;
	
	@Column(name="expiredAt")
	private Date expiredAt;
	
	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	@Column(name="createdAt")
	private Date createdAt;
	
	public Integer getIdToken() {
		return idToken;
	}
	public void setIdToken(Integer idToken) {
		this.idToken = idToken;
	}
	public String getStringToken() {
		return stringToken;
	}
	public void setStringToken(String stringToken) {
		this.stringToken = stringToken;
	}
	public Integer getIdUser() {
		return idUser;
	}
	public void setIdUser(Integer idUser) {
		this.idUser = idUser;
	}
	public Date getExpiredAt() {
		return expiredAt;
	}
	public void setExpiredAt(Date expiredAt) {
		this.expiredAt = expiredAt;
	}
	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
	

}

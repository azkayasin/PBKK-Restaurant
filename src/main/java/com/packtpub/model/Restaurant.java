package com.packtpub.model;

import java.sql.Time;
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

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name="restaurants")
@EntityListeners(AuditingEntityListener.class)

public class Restaurant {

	public Restaurant() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Restaurant(Integer id, String name,String alamat, Integer resto_id,String deskripsi, Time buka , Time tutup, Integer status,Integer kondisi) {
		super();
		this.id = id;
		this.name = name;
		this.alamat = alamat;
		this.resto_id = resto_id;
		this.deskripsi = deskripsi;
		this.buka = buka;
		this.tutup = tutup;
		this.kondisi = kondisi;
		this.status = status;
		
	}

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="alamat")
	private String alamat;
	
	@Column(name="resto_id")
	private Integer resto_id;
	
	@Column(name="deskripsi")
	private String deskripsi;
	
	@Column(name="buka")
	private Time buka;
	
	@Column(name="tutup")
	private Time tutup;
	
	@Column(name="kategori")
	private String kategori;
	
	@Column(name="status")
	private Integer status;
	
	@Column(name="kondisi")
	private Integer kondisi;
	
	@Column(name="deals")
	private String deals;
	
	@Column(name="gambar")
	private String gambar;

	@Temporal(TemporalType.TIMESTAMP)
	@LastModifiedDate
	@Column(name="createdAt")
	private Date createdAt;
	
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}

	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDeals() {
		return deals;
	}
	public void setDeals(String deals) {
		this.deals = deals;
	}
	
	public String getAlamat() {
		return alamat;
	}
	public void setAlamat(String alamat) {
		this.alamat = alamat;
	}
	
	public Integer getResto_id() {
		return resto_id;
	}
	public void setResto_id(Integer resto_id) {
		this.resto_id =resto_id;
	}
	
	public String getDeskripsi() {
		return deskripsi;
	}
	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}
	public Time getBuka() {
		return buka;
	}
	public void setBuka(Time buka) {
		this.buka = buka;
	}
	
	public Time getTutup() {
		return tutup;
	}
	public void setTutup(Time tutup) {
		this.tutup = tutup;
	}
	
	public String getKategori() {
		return kategori;
	}
	public void setKategori(String kategori) {
		this.kategori = kategori;
	}
		
	public Integer getKondisi() {
		return kondisi;
	}
	public void setKondisi(Integer kondisi) {
		this.kondisi = kondisi;
	}

	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

}

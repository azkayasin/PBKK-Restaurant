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

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@Table(name="Menus")
@EntityListeners(AuditingEntityListener.class)

public class Menu {
	public Menu() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public Menu(Integer id, String name, Double harga, Integer stock, String deskripsi, Integer res_id, String kategori, Integer status) {
		super();
		this.id = id;
		this.name = name;
		this.harga = harga;
		this.stock = stock;
		this.deskripsi = deskripsi;
		this.res_id = res_id;
		this.kategori = kategori;
		this.status = status;
	}
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Integer id;
	
	@Column(name="name")
	private String name;
	
	@Column(name="harga")
	private Double harga;
	
	@Column(name="stock")
	private Integer stock;
	
	@Column(name="deskripsi")
	private String deskripsi;
	
	@Column(name="gambar")
	private String gambar;
	
	@Column(name="res_id")
	private Integer res_id;
	
	@Column(name="kategori")
	private String kategori;
	
	@Column(name="status")
	private Integer status;
	
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
	
	public String getGambar() {
		return gambar;
	}
	
	public void setGambar(String gambar) {
		this.gambar = gambar;
	}
	
	public Double getHarga() {
		return harga;
	}
	public void setHarga(Double harga ) {
		this.harga = harga;
	}
	
	public Integer getStock() {
		return stock;
	}
	public void setStock(Integer stock) {
		this.stock = stock;
	}
	
	public String getDeskripsi() {
		return deskripsi;
	}
	public void setDeskripsi(String deskripsi) {
		this.deskripsi = deskripsi;
	}
	
	public String getKategori() {
		return kategori;
	}
	public void setKategori(String kategori) {
		this.kategori = kategori;
	}
	
	public Integer getRes_id() {
		return res_id;
	}
	public void setRes_id(Integer res_id ) {
		this.res_id = res_id;
	}
	
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status ) {
		this.status = status;
	}

	public Date getCreatedAt() {
		return createdAt;
	}
	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}
}

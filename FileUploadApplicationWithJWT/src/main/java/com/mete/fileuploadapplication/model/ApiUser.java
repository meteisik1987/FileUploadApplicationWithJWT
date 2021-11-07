package com.mete.fileuploadapplication.model;

import java.util.ArrayList;
import java.util.Collection;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.UniqueConstraint;

@Entity
public class ApiUser {

	public ApiUser() {
		super();
	}

	public ApiUser(Long id, String name, String password, Collection<ApiRole> roles) {
		super();
		Id = id;
		this.name = name;
		this.password = password;
		this.roles = roles;
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long Id;

	@Column(unique = true)
	private String name;

	@Column
	private String password;

	@ManyToMany(fetch = FetchType.EAGER)
	private Collection<ApiRole> roles = new ArrayList<>();

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Collection<ApiRole> getRoles() {
		return roles;
	}

	public void setRoles(Collection<ApiRole> roles) {
		this.roles = roles;
	}

}

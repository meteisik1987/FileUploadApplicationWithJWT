package com.mete.fileuploadapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mete.fileuploadapplication.model.ApiRole;

public interface RoleRepository extends JpaRepository<ApiRole, Long> {

	public ApiRole findByName(String name);

}

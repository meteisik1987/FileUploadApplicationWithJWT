package com.mete.fileuploadapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mete.fileuploadapplication.model.ApiUser;

public interface UserRepository extends JpaRepository<ApiUser, Long> {

	public ApiUser findByName(String Name);
	

}

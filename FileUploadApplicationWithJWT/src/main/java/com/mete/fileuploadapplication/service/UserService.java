package com.mete.fileuploadapplication.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.mete.fileuploadapplication.model.ApiRole;
import com.mete.fileuploadapplication.model.ApiUser;

@Service
public interface UserService {

	public ApiUser saveUser(ApiUser user);

	public ApiRole saveRole(ApiRole role);

	public void addRoleToUser(String username, String rolename);

	public ApiUser getUser(String name);

	public List<ApiUser> getUsers();

}

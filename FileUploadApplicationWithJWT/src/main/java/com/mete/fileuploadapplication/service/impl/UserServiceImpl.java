package com.mete.fileuploadapplication.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.mete.fileuploadapplication.model.ApiRole;
import com.mete.fileuploadapplication.model.ApiUser;
import com.mete.fileuploadapplication.repository.RoleRepository;
import com.mete.fileuploadapplication.repository.UserRepository;
import com.mete.fileuploadapplication.service.UserService;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

	@Autowired
	public UserRepository userRepository;

	@Autowired
	public RoleRepository roleRepository;

	@Autowired
	public PasswordEncoder encoder;

	@Bean
	BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
		ApiUser user = userRepository.findByName(userName);

		if (user == null) {
			throw new UsernameNotFoundException("User not found");
		}

		Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

		for (ApiRole role : user.getRoles()) {
			authorities.add(new SimpleGrantedAuthority(role.getName()));
		}

		return new User(user.getName(), user.getPassword(), authorities);
	}

	@Override
	public ApiUser saveUser(ApiUser user) {
		user.setPassword(encoder.encode(user.getPassword()));
		return userRepository.save(user);
	}

	@Override
	public ApiRole saveRole(ApiRole role) {
		return roleRepository.save(role);
	}

	@Override
	public void addRoleToUser(String username, String rolename) {
		ApiUser user = userRepository.findByName(username);
		ApiRole role = roleRepository.findByName(rolename);
		user.getRoles().add(role);
		userRepository.save(user);
	}

	@Override
	public ApiUser getUser(String name) {
		return userRepository.findByName(name);

	}

	@Override
	public List<ApiUser> getUsers() {
		return userRepository.findAll();
	}

}

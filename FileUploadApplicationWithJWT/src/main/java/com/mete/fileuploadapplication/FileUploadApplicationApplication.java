package com.mete.fileuploadapplication;

import java.util.ArrayList;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

import com.mete.fileuploadapplication.configuration.FileUploadProperties;
import com.mete.fileuploadapplication.model.ApiRole;
import com.mete.fileuploadapplication.model.ApiUser;
import com.mete.fileuploadapplication.service.UserService;

@SpringBootApplication
@EnableConfigurationProperties({ FileUploadProperties.class })
public class FileUploadApplicationApplication {

	public static void main(String[] args) {
		SpringApplication.run(FileUploadApplicationApplication.class, args);
	}
	
	@Bean
	CommandLineRunner run(UserService userService) {
		return args -> {
			userService.saveRole(new ApiRole(null, "ROLE_USER"));
			userService.saveRole(new ApiRole(null, "ROLE_ADMIN"));

			userService.saveUser(new ApiUser(null, "manager", "12345", new ArrayList<>()));
			userService.saveUser(new ApiUser(null, "user", "12345", new ArrayList<>()));

			userService.addRoleToUser("manager", "ROLE_ADMIN");
			userService.addRoleToUser("manager", "ROLE_USER");
			
			userService.addRoleToUser("user", "ROLE_USER");

		};
	}

}

package com.mete.fileuploadapplication.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.mete.fileuploadapplication.model.FileInfo;
import com.mete.fileuploadapplication.payload.ApiResponse;
import com.mete.fileuploadapplication.service.FileUploadService;

@RestController
@RequestMapping("/file")
public class FileController {

	@Autowired
	private FileUploadService fileUploadService;

	@PostMapping("/uploadFile")
	public ResponseEntity<FileInfo> uploadFile(@RequestParam("file") MultipartFile file) {
		return fileUploadService.saveFile(file);
	}

	@DeleteMapping("/deleteFile/{id}")
	public ResponseEntity<ApiResponse> uploadFile(@PathVariable(name="id") Long id) {
		return fileUploadService.deleteFile(id);
	}

	@PutMapping("/updateFile/{id}")
	public ResponseEntity<ApiResponse> updateFile(@PathVariable(name = "id") Long id,
			@RequestParam("file") MultipartFile file) {
		return fileUploadService.updateFile(id, file);
	}

	@GetMapping("/getAllFiles")
	public ResponseEntity<List<FileInfo>> getAllFiles() {
		return fileUploadService.getAllFiles();
	}

}

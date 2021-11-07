package com.mete.fileuploadapplication.service;

import java.util.List;

import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.mete.fileuploadapplication.model.FileInfo;
import com.mete.fileuploadapplication.payload.ApiResponse;

@Service
public interface FileUploadService {

	public ResponseEntity<FileInfo> saveFile(MultipartFile file);

	public ResponseEntity<ApiResponse> deleteFile(Long id);

	public ResponseEntity<ApiResponse> getFileAsByteArray(String fileName);
	
	public ResponseEntity<ApiResponse> updateFile(Long id, MultipartFile file);
	
	public ResponseEntity<List<FileInfo>> getAllFiles();
	
	public Resource loadFileAsResource(String fileName);

}

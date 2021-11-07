package com.mete.fileuploadapplication.service.impl;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.mete.fileuploadapplication.exception.FileUploadException;
import com.mete.fileuploadapplication.model.FileInfo;
import com.mete.fileuploadapplication.payload.ApiResponse;
import com.mete.fileuploadapplication.repository.FileUploadRepository;
import com.mete.fileuploadapplication.service.FileUploadService;

@Service
public class FileUploadServiceImpl implements FileUploadService {

	@Autowired
	private FileUploadRepository fileUploadRepository;

	private static List<String> prefixList = new ArrayList<>();;

	private final Path root = Paths.get("upload");

	public FileUploadServiceImpl() {
		try {
			if (!Files.exists(root)) {
				Files.createDirectory(root);
			}
			prefixList.add("png");
			prefixList.add("jpeg");
			prefixList.add("jpg");
			prefixList.add("docx");
			prefixList.add("pdf");
			prefixList.add("xlsx");
		} catch (Exception ex) {
			throw new FileUploadException(
					"Could not create the directory. Please create an upload directory on the root.", ex);
		}
	}

	@Override
	public ResponseEntity<FileInfo> saveFile(MultipartFile file) {

		String fileName = StringUtils.cleanPath(file.getOriginalFilename());
		try {
			if (fileName.contains("..")) {
				throw new FileUploadException("Invalid filepath " + fileName);
			}

			if (!checkFileType(file)) {
				throw new FileUploadException(
						"Please use one of the following file types. png, jpeg, jpg, docx, pdf, xlsx");
			}

			// Check if the file exists. If so, throw error. If not, save and copy the file
			// to the directory.
			FileInfo foundFile = fileUploadRepository.getFileInfoByNameAndType(fileName, file.getContentType());

			if (foundFile == null) {
				Path targetLocation = root.resolve(fileName);
				FileInfo fileInfo = new FileInfo(fileName, targetLocation.toString(), file.getSize(),
						file.getContentType());
				fileInfo = fileUploadRepository.save(fileInfo);
				Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);
				return new ResponseEntity<>(fileInfo, HttpStatus.OK);
			} else {
				throw new FileUploadException("File already exists. Please use update. " + fileName);
			}
		} catch (IOException e) {
			throw new FileUploadException("Could not save the file " + fileName);
		} catch (Exception e) {
			throw new FileUploadException(e.getMessage());
		}
	}

	@Override
	public ResponseEntity<ApiResponse> deleteFile(Long id) {

		FileInfo file = fileUploadRepository.getById(id);

		if (file == null) {
			throw new FileUploadException("Error finding file with id: " + id);
		} else {
			Path fileToDeletePath = Paths.get(file.getPath()).normalize();
			try {
				Files.delete(fileToDeletePath);
				fileUploadRepository.deleteById(id);
			} catch (Exception e) {
				throw new FileUploadException("Error finding file at " + fileToDeletePath);
			}
		}

		return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "File deleted successfully"), HttpStatus.OK);

	}

	@Override
	public ResponseEntity<ApiResponse> getFileAsByteArray(String fileName) {
		Path fileToDeletePath = root.resolve(fileName).normalize();
		try {
			byte[] data = Files.readAllBytes(fileToDeletePath);
		} catch (Exception e) {
			throw new FileUploadException("Error finding file at " + fileToDeletePath);
		}

		return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "File deleted successfully"), HttpStatus.OK);

	}

	@Override
	public Resource loadFileAsResource(String fileName) {

		try {
			Path filePath = root.resolve(fileName).normalize();
			Resource resource = new UrlResource(filePath.toUri());
			if (resource.exists()) {
				return resource;
			} else {
				throw new FileUploadException("File not found" + fileName);
			}
		} catch (Exception e) {
			throw new FileUploadException("File not found" + fileName);
		}
	}

	@Override
	public ResponseEntity<ApiResponse> updateFile(Long id, MultipartFile file) {

		String deleteLocation = "";
		String fileName = StringUtils.cleanPath(file.getOriginalFilename());

		if (fileName.contains("..")) {
			throw new FileUploadException("Invalid filepath " + fileName);
		}

		if (!checkFileType(file)) {
			throw new FileUploadException(
					"Please use one of the following file types. png, jpeg, jpg, docx, pdf, xlsx");
		}

		try {
			FileInfo foundFile = fileUploadRepository.getById(id);

			if (foundFile == null) {
				throw new FileUploadException("File not found" + fileName);
			} else {
				deleteLocation = foundFile.getPath();
				Path targetLocation = root.resolve(fileName);
				foundFile.setName(fileName);
				foundFile.setFileType(file.getContentType());
				foundFile.setPath(targetLocation.toString());
				foundFile.setSize(file.getSize());
				fileUploadRepository.save(foundFile);

				Files.delete(Paths.get(deleteLocation));
				Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

			}
		} catch (Exception e) {
			throw new FileUploadException("Error while updating the file");
		}

		return new ResponseEntity<>(new ApiResponse(Boolean.TRUE, "File updated successfully"), HttpStatus.OK);
	}

	@Override
	public ResponseEntity<List<FileInfo>> getAllFiles() {
		List<FileInfo> files = new ArrayList<>();
		try {
			files = fileUploadRepository.findAll();
			return new ResponseEntity(files, HttpStatus.OK);
		} catch (Exception e) {
			throw new FileUploadException("Error while updating the file");
		}
	}

	public boolean checkFileType(MultipartFile file) {
		try {
			String prefix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf(".") + 1);

			if (prefixList.contains(prefix)) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new FileUploadException("There is something wrong with the file prefix");
		}
	}

}

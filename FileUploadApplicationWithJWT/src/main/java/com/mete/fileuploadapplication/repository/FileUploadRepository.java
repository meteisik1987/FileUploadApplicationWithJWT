package com.mete.fileuploadapplication.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.mete.fileuploadapplication.model.FileInfo;

public interface FileUploadRepository extends JpaRepository<FileInfo, Long> {

	@Query("Select f From FileInfo f Where f.name=:name And f.fileType=:filetype")
	public FileInfo getFileInfoByNameAndType(@Param("name") String name, @Param("filetype") String filetype);

	public FileInfo getFileInfoByName(String name);

}

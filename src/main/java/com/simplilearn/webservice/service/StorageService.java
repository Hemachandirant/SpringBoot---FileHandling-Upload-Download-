package com.simplilearn.webservice.service;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.simplilearn.webservice.exception.StorageException;

@Service
public class StorageService {
	
	@Value("${upload.filepath}")
	private String filepath;
	
	public void uploadFile(MultipartFile file) {
		if(file.isEmpty()) {
			throw new StorageException("Failed to upload file -> File is Empty, Upload a valid file");
		}
		
		System.out.println("Filename -> "+getFileNameExtension(file));
		
		try {
			String filename = file.getOriginalFilename();
			InputStream sourcefile = file.getInputStream();
			Files.copy(sourcefile, Paths.get(filepath+filename), StandardCopyOption.REPLACE_EXISTING);
			
		} catch (Exception e) {
			throw new StorageException("Failed to upload file");
		}
	}
	
	public String getFilePath(String filename) {
		return filepath+filename;
	}
	
	public String getFileNameExtension(MultipartFile file) {
		String name = file.getOriginalFilename();
		int lastIndexOf = name.lastIndexOf(".");
		if(lastIndexOf==-1) {
			return "";
		}else {
			return name.substring(lastIndexOf);
		}
	}

	
	
}

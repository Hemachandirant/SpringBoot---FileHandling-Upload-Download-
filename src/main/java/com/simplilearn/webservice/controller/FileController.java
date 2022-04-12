package com.simplilearn.webservice.controller;

import java.awt.PageAttributes.MediaType;
import java.io.File;
import java.io.FileInputStream;
import java.net.http.HttpHeaders;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.MediaType;

import com.simplilearn.webservice.exception.StorageException;
import com.simplilearn.webservice.service.StorageService;




@Controller
public class FileController {

	
	@Autowired
	StorageService storageService;
	
	@RequestMapping(value="/", method = RequestMethod.GET )
	public String index() {
		return "index.html";
	}
	
	@RequestMapping(value="/upload", method = RequestMethod.POST, consumes= {"multipart/form-data"})
	public String upload(@RequestParam MultipartFile file) {
		storageService.uploadFile(file);
		return "redirect:/success.html";
	}


	@RequestMapping(value="/download", method=RequestMethod.GET)
	public ResponseEntity<InputStreamResource> download(@RequestParam("filename") String filename) {
		try {
			// read file
			File file = new File(storageService.getFilePath(filename));
			FileInputStream fileInputStream = new FileInputStream(file);
			InputStreamResource resource = new InputStreamResource(fileInputStream);
			
			//create http headers
			org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
			headers.add("Content-Disposition", String.format("attachment; filename=\"%s\"", file.getName()));
			
			return ResponseEntity.ok().headers(headers)
					.contentLength(file.length()).contentType(MediaType.parseMediaType("application/text"))
					.body(resource);
		} catch (Exception e) {
			throw new StorageException("Falied to download a file !");
		}
		
		
	}
	
	@ExceptionHandler(StorageException.class)
	public String handStorageException() {
		return "redirect:/failure.html";
	}
}
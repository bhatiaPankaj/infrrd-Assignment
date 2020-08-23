package com.fileoperation.base.rest;

import java.io.*;
import java.net.URLConnection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fileoperation.base.common.FilesManager;
import com.fileoperation.base.common.ReturnType;
import org.springframework.web.client.RestTemplate;

@Controller
@RequestMapping("/file")
public class FileController {

	Logger logger = LogManager.getLogger(FileController.class);
	private static RestTemplate restTemplate = new RestTemplate();

	@Autowired
	FilesManager fileManager;

	@Autowired
	FileBiz fileBiz;

	@GetMapping(produces = MediaType.APPLICATION_OCTET_STREAM_VALUE)
	public void getFile(HttpServletRequest request, HttpServletResponse response) throws IOException {
		byte[] reportBytes = null;

		List<String> fileNames = fileManager.getFile();
		if (fileNames.isEmpty()) {
			throw new FileNotFoundException("File does not Exist");
		}
		File result = new File(fileNames.get(0));
		if (result.exists()) {
			try (InputStream inputStream = new FileInputStream(result.getAbsoluteFile())) {
				logger.info("File found starting download");
				String type = URLConnection.guessContentTypeFromName(result.getAbsolutePath());
				response.setHeader("Content-Disposition", "attachment; filename=" + result.getName());
				response.setHeader("Content-Type", type);
				reportBytes = new byte[100];
				OutputStream os = response.getOutputStream();
				int read = 0;
				while ((read = inputStream.read(reportBytes)) != -1) {
					os.write(reportBytes, 0, read);
				}
				os.flush();
				os.close();
			}

		} else {
			logger.error("Error in downloading file not found");
			throw new FileNotFoundException("File does not exist");
		}
	}

	@DeleteMapping(value = "/delete", produces = "application/json")
	public ResponseEntity<ReturnType> deleteFile(HttpServletRequest request, HttpServletResponse response)
			throws IOException {
		List<String> fileNames = fileManager.getFile();
		if (fileNames.isEmpty()) {
			throw new FileNotFoundException("File Not Found deletion can't be done");
		}
		File result = new File(fileNames.get(0));
		ReturnType res = new ReturnType();
		if (result.delete()) {
			fileNames.remove(0);
			res.setMessage("File Deleted");
			return new ResponseEntity<ReturnType>(res, HttpStatus.OK);
		}
		res.setMessage("Error in File deletion");
		return new ResponseEntity<ReturnType>(res, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@PutMapping(value = "/copy",produces = "application/json")
	public ResponseEntity<ReturnType> copyFile(HttpServletRequest request, HttpServletResponse response)
			throws  FileNotFoundException {
		ReturnType res = new ReturnType();
		try {
			fileBiz.copyFile();
		} catch (Exception e) {
			logger.error("Error in copying files");
		}
		return new ResponseEntity<ReturnType>(res, HttpStatus.OK);
	}

}

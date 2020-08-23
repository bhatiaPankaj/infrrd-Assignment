package com.fileoperation.base.rest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fileoperation.base.common.FilesManager;

@Service
public class FileBizImpl implements FileBiz {

	@Autowired
	FilesManager fileManager;
	
	@Override
	public void copyFile() throws IOException {
		List<String> fileNames = fileManager.getFile();
		int fileCount = fileNames.size();
		File result = new File(fileNames.get(0));
		Path sourceFile = Paths.get(result.getAbsolutePath());
		String fileName = result.getAbsolutePath().substring(0, result.getAbsolutePath().lastIndexOf('_'));
		String extension = result.getAbsolutePath().substring(result.getAbsolutePath().lastIndexOf('.') + 1, result.getAbsolutePath().length());
		Path targetFile = Paths.get(fileName + "_" + fileCount + '.' + extension);

		try {
			Files.copy(sourceFile, targetFile, StandardCopyOption.REPLACE_EXISTING);
			fileNames.add(fileName + "_" + fileCount + '.' + extension);
			fileCount++;

		} catch (IOException ex) {
			throw new IOException("Error in copying file");
		}

	}

}

package com.fileoperation.base.common;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternUtils;
import org.springframework.stereotype.Component;

@Component
public class FilesManager {

	@Value("${file.path}")
	private String FILE_PATH;

	private ResourceLoader resourceLoader;

	private List<String> files;

	@Autowired
	public FilesManager(ResourceLoader resourceLoader) {
		this.resourceLoader = resourceLoader;
	}

	@PostConstruct
	public void loadResources() throws IOException {
		Resource[] resArr = ResourcePatternUtils.getResourcePatternResolver(resourceLoader).getResources(FILE_PATH);
		files = Arrays.asList(resArr).stream().map(x->{
			try {
				return x.getFile().getAbsolutePath();
			} catch (IOException e) {
				e.printStackTrace();
			}
			return FILE_PATH;
		}).collect(Collectors.toList());;
	}

	public List<String> getFile() {
		return files;
	}
}

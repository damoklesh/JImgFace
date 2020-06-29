package com.steve.app;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.function.Predicate;
import java.util.stream.Stream;

public class FileWalker {

	public Stream<Path> extractImageFilesFromDir(String dir) throws IOException {

		return Files.walk(Paths.get(dir)).filter(imageFilter());
	}

	public File loadResource(String resourceName) {
		return new File(getClass().getClassLoader().getResource(resourceName).getFile());
	}

	private Predicate<? super Path> imageFilter() {
		return img -> img.toString().endsWith(".jpg") || img.toString().endsWith(".png");
	}
}

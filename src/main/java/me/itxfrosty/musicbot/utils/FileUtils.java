package me.itxfrosty.musicbot.utils;

import java.io.File;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;

public class FileUtils {

	/**
	 * Get's file from Resource Folder.
	 *
	 * @param fileName File Name.
	 * @return File or throws URISyntaxException.
	 */
	public static File getFileFromResource(String fileName) throws URISyntaxException {
		ClassLoader classLoader = FileUtils.class.getClassLoader();
		URL resource = classLoader.getResource(fileName);
		if (resource == null) {
			throw new IllegalArgumentException("file not found! " + fileName);
		} else {
			return new File(resource.toURI());
		}
	}

	private static InputStream getFileFromResourceAsStream(String fileName) {
		ClassLoader classLoader = FileUtils.class.getClassLoader();
		InputStream inputStream = classLoader.getResourceAsStream(fileName);

		if (inputStream == null) {
			throw new IllegalArgumentException("file not found! " + fileName);
		} else {
			return inputStream;
		}
	}

}
package com.yangyang.cloud.common.util;

public class FileUtil {
	// linux windows
	public static String dirSplit = "\\";
	public static final String FILE_SEPARATOR = System.getProperty("file.separator");
	//public static final String FILE_SEPARATOR = File.separator;

	public static String getRealFilePath(String path) {
		return path.replace("/", FILE_SEPARATOR).replace("\\", FILE_SEPARATOR);
	}

	public static String getHttpURLPath(String path) {
		return path.replace("\\", "/");
	}
}

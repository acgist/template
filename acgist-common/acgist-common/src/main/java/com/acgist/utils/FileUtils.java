package com.acgist.utils;

import java.io.File;
import java.io.FileNotFoundException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;

/**
 * <p>utils - 文件</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public final class FileUtils {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(FileUtils.class);

	/**
	 * <p>通过Spring资源工具获取文件</p>
	 * <p>支持：{@code file:}、{@code classpath:}</p>
	 * 
	 * @param path 文件路径
	 * 
	 * @return 文件
	 */
	public static final File getFile(String path) {
		try {
			return ResourceUtils.getFile(path);
		} catch (FileNotFoundException e) {
			LOGGER.error("获取文件异常：", path, e);
		}
		return null;
	}
	
	/**
	 * <p>生成目录</p>
	 * 
	 * @param filePath 目录路径
	 * @param isFile 是否是文件
	 */
	public static final void mkdirs(String filePath, boolean isFile) {
		File file = new File(filePath);
		if (file.exists()) {
			return;
		}
		if (isFile) {
			file = file.getParentFile();
			if (file.exists()) {
				return;
			}
		}
		file.mkdirs();
	}
	
}

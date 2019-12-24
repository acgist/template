package com.acgist.utils;

import java.io.File;

/**
 * <p>utils - 文件</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
public final class FileUtils {

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

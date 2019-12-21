package com.api.utils;

import java.io.File;

/**
 * utils - 通用
 */
public class CommonUtils {

	/**
	 * 生成目录
	 * @param filePath 目录地址
	 * @param isFile 是否是文件，如果是文件生成父目录
	 */
	public static final void mkdirs(String filePath, boolean isFile) {
		File file = new File(filePath);
		if(file.exists()) {
			return;
		}
		if(isFile) {
			file = file.getParentFile();
			if(file.exists()) {
				return;
			}
		}
		file.mkdirs();
	}

}

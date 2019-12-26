package com.acgist.core.www.service;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.acgist.core.config.AcgistConst;
import com.acgist.utils.FileUtils;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * <p>service - FreeMarker</p>
 * 
 * @author acgist
 * @since 1.0.0
 */
@Service
public class FreeMarkerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FreeMarkerService.class);

	@Autowired
	private Configuration configuration;

	/**
	 * <p>生成静态文件</p>
	 * 
	 * @param templatePath 模板路径：index.ftl
	 * @param data 数据
	 * @param htmlPath 生成HTML路径：/article/
	 * @param htmlName 生成HTML文件名称：index.html
	 * 
	 * @return 是否成功
	 */
	public boolean build(String templatePath, Map<Object, Object> data, String htmlPath, String htmlName) {
		if (StringUtils.isEmpty(htmlPath)) {
			LOGGER.warn("生成静态文件路径错误：{}", htmlPath);
			return false;
		}
		if (!htmlPath.endsWith("/")) {
			htmlPath += "/";
		}
		final File htmlFile = FileUtils.file(htmlPath + htmlName);
		FileUtils.mkdirs(htmlFile.getPath(), true);
		try (final Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(htmlFile), AcgistConst.DEFAULT_CHARSET))) {
			final Template template = this.configuration.getTemplate(templatePath, AcgistConst.DEFAULT_CHARSET);
			template.process(data, writer);
			writer.flush();
		} catch (TemplateException | IOException e) {
			LOGGER.error("生成静态文件异常", e);
		}
		return true;
	}

	/**
	 * <p>生成文本</p>
	 * 
	 * @param content 模板内容
	 * @param data 数据
	 * 
	 * @return 文本
	 */
	public String templateConvert(String content, Map<String, Object> data) {
		final StringTemplateLoader loader = new StringTemplateLoader();
		loader.putTemplate("template", content);
		this.configuration.setTemplateLoader(loader);
		try (final Writer writer = new StringWriter()) {
			final Template template = this.configuration.getTemplate("template", AcgistConst.DEFAULT_CHARSET);
			template.process(data, writer);
			content = writer.toString();
		} catch (TemplateException | IOException e) {
			LOGGER.error("生成文本异常", e);
		}
		return content;
	}

}

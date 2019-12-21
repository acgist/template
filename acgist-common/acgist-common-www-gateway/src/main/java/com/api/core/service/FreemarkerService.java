package com.api.core.service;

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

import com.acgist.core.service.FreemarkerService;
import com.acgist.utils.FileUtils;

import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * service - freemarker
 */
@Service
public class FreemarkerService {

	private static final Logger LOGGER = LoggerFactory.getLogger(FreemarkerService.class);

	@Autowired
	private Configuration configuration;

	/**
	 * 生成静态文件
	 * @param templatePath 模板路径
	 * @param data 数据
	 * @param htmlPath 生成HTML路径：/article/
	 * @param htmlName 生成HTML文件名称：index.html
	 */
	public boolean build(String templatePath, Map<Object, Object> data, String htmlPath, String htmlName) {
		if (StringUtils.isEmpty(htmlPath)) {
			LOGGER.warn("生成静态文件路径错误：{}", htmlPath);
			return false;
		}
		if (!htmlPath.endsWith("/")) {
			htmlPath += "/";
		}
		final File htmlFile = new File(htmlPath + htmlName);
		FileUtils.mkdirs(htmlFile.getPath(), true);
		try (Writer writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(htmlFile), "UTF-8"))) {
			final Template template = this.configuration.getTemplate(templatePath, "UTF-8");
			template.process(data, writer);
			writer.flush();
		} catch (TemplateException | IOException e) {
			LOGGER.error("freemarker模板异常", e);
		}
		return true;
	}

	/**
	 * 根据文本模板生产文本
	 * @param content 模板文本
	 * @param data 数据
	 */
	public String templateConvert(String content, Map<String, Object> data) {
		final StringTemplateLoader loader = new StringTemplateLoader();
		loader.putTemplate("template", content);
		configuration.setTemplateLoader(loader);
		try (Writer writer = new StringWriter()) {
			final Template template = configuration.getTemplate("template", "UTF-8");
			template.process(data, writer);
			content = writer.toString();
		} catch (TemplateException | IOException e) {
			LOGGER.error("freemarker模板异常", e);
		}
		return content;
	}

}

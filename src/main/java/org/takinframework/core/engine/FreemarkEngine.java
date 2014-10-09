package org.takinframework.core.engine;

import java.io.IOException;

import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
/**
 * Freemark模板Engine
 * @author twg
 *
 */
public class FreemarkEngine {
	/**
	 * 资源
	 */
	private Configuration configuration;

	public Configuration getConfiguration() {
		return configuration;
	}
	public void setConfiguration(Configuration configuration) {
		this.configuration = configuration;
	}
	
	/**
	 * 填充模板数据
	 * @param templateName
	 * @param model
	 * @return
	 * @throws IOException
	 * @throws TemplateException
	 */
	public String mergeTemplateIntoString(String templateName, Object model)
			throws IOException, TemplateException{
		    Template template = this.configuration.getTemplate(templateName);
		    return FreeMarkerTemplateUtils.processTemplateIntoString(template, model);
	}
	
}

package org.takinframework.core.util;

import java.io.InputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.dom4j.Document;
import org.dom4j.Element;

public class ConfigUtil {
	private Document doc = null;
	private static ConfigUtil config = null;

	private static Lock lock = new ReentrantLock();

	private ConfigUtil() {
		InputStream is = getClass().getClassLoader().getResourceAsStream(
				"conf/viewconfig.xml");
		this.doc = Dom4jUtil.loadXml(is);
	}

	public static ConfigUtil getInstance() {
		if (config == null) {
			lock.lock();
			try {
				if (config == null)
					config = new ConfigUtil();
			} finally {
				lock.unlock();
			}
		}
		return config;
	}

	public String getValue(String category, String id) {
		String template = "category[@id='%s']/view[@name='%s']";
		String filter = String.format(template, new Object[] { category, id });
		Element root = this.doc.getRootElement();
		Element el = (Element) root.selectSingleNode(filter);
		if (el != null)
			return el.attributeValue("value");
		return "";
	}

	public static String getVal(String category, String id) {
		return getInstance().getValue(category, id);
	}

}

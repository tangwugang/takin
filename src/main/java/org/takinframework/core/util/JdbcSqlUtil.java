package org.takinframework.core.util;

import java.io.InputStream;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import org.dom4j.Document;
import org.dom4j.Element;

/**
 * jdbcTemplet中sql配置xml
 * @author twg
 *
 */
public class JdbcSqlUtil {
	
	private Document doc = null;
	private static JdbcSqlUtil config = null;

	private static Lock lock = new ReentrantLock();

	private JdbcSqlUtil() {
		InputStream is = getClass().getClassLoader().getResourceAsStream(
				"conf/sqlconfig.xml");
		this.doc = Dom4jUtil.loadXml(is);
//		String path = "src/main/resources/conf/sqlconfig.xml";
//		this.doc = Dom4jUtil.readXml(path);
	}

	public static JdbcSqlUtil getInstance() {
		if (config == null) {
			lock.lock();
			try {
				if (config == null)
					config = new JdbcSqlUtil();
			} finally {
				lock.unlock();
			}
		}
		return config;
	}
	//sql语句
	public String getValue(String category,String methId, String id) {
//		Element root = this.doc.getRootElement();
//		Element show =(Element) root.selectSingleNode("/students/student[@id='1000']");
//		System.err.println(show.attributeValue("value"));
		String template = "/root/category[@id='%s']/methord[@id='%s']/sql[@name='%s']";
		String filter = String.format(template, new Object[] { category,methId, id });
		Element root = this.doc.getRootElement();
		Element el = (Element) root.selectSingleNode(filter);
		if (el != null)
			return el.attributeValue("value");
		return "";
	}
	//sql语句的参数
	public String getParam(String category,String methId, String id) {
		String template = "/root/category[@id='%s']/methord[@id='%s']/sql[@name='%s']";
		String filter = String.format(template, new Object[] { category,methId, id });
		Element root = this.doc.getRootElement();
		Element el = (Element) root.selectSingleNode(filter);
		if (el != null)
			return el.attributeValue("param");
		return "";
	}
	//sql不显示的列表字段
	public String getExclude(String category,String methId, String id) {
		String template = "/root/category[@id='%s']/methord[@id='%s']/sql[@name='%s']";
		String filter = String.format(template, new Object[] { category,methId, id });
		Element root = this.doc.getRootElement();
		Element el = (Element) root.selectSingleNode(filter);
		if (el != null)
			return el.attributeValue("exclude");
		return "";
	}
	//sql中别名
	public String getAlias(String category,String methId, String id) {
		String template = "/root/category[@id='%s']/methord[@id='%s']/sql[@name='%s']";
		String filter = String.format(template, new Object[] { category,methId, id });
		Element root = this.doc.getRootElement();
		Element el = (Element) root.selectSingleNode(filter);
		if (el != null)
			return el.attributeValue("alias");
		return "";
	}
	
	 

	public static String getVal(String category,String methId, String id) {
		return getInstance().getValue(category,methId,id);
	}
	
	public static String getPar(String category,String methId, String id) {
		return getInstance().getParam(category,methId, id);
	}
	
	public static String getExc(String category,String methId, String id) {
		return getInstance().getExclude(category,methId, id);
	}
	public static String getAlia(String category,String methId, String id) {
		return getInstance().getAlias(category,methId, id);
	}

}

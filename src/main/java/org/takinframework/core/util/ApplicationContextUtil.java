package org.takinframework.core.util;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.google.common.collect.Sets;

/**
 * spring 上下文工具类
 * @author  twg
 *
 */
public class ApplicationContextUtil implements ApplicationContextAware {

	private static ApplicationContext applicationContext;
	private static ServletContext servletContext;
	private static HttpSession sessionContext;
	private static Set<String> sessions = Collections.synchronizedSet(new TreeSet<String>());
//	private static Map<Long, OnlineUser> onlineUsers = new LinkedHashMap();

	public static void init(ServletContext _servletContext) {
		servletContext = _servletContext;
	}

	public void setApplicationContext(ApplicationContext context)
			throws BeansException {
		applicationContext = context;
	}

	public static ApplicationContext getContext() {
		return applicationContext;
	}

	public static ServletContext getServletContext() throws Exception {
		return servletContext;
	}

	public static <T> T getBean(Class<T> cls) {
		return applicationContext.getBean(cls);
	}

	public static Object getBean(String beanId) {
		return applicationContext.getBean(beanId);
	}

	public static String getAppAbsolutePath() {
		return servletContext.getRealPath("/");
	}

	public static String getRealPath(String path) {
		return servletContext.getRealPath(path);
	}
	
	

//	public static Map<Long, OnlineUser> getOnlineUsers() {
//		return onlineUsers;
//	}
	
	


	public static String getClasspath() {
		String classPath = Thread.currentThread().getContextClassLoader()
				.getResource("").getPath();
		String rootPath = "";

		if ("\\".equals(File.separator)) {
			rootPath = classPath.substring(1);
			rootPath = rootPath.replace("/", "\\");
		}

		if ("/".equals(File.separator)) {
			rootPath = classPath.substring(1);
			rootPath = rootPath.replace("\\", "/");
		}
		return rootPath;
	}

	public static Set<String> getSessions() {
		return sessions;
	}

	public static void setSessions(Set<String> sessions) {
		ApplicationContextUtil.sessions = sessions;
	}
	
	public static void removeSession(String sessionId){
		Set<String> s = ApplicationContextUtil.getSessions();
		if(StringUtil.isNotEmpty(s) && s.size()>0){
			s.remove(sessionId);
		}
		
	}

	public static void main(String[] args) {
	}
}

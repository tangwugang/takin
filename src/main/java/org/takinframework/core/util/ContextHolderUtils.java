package org.takinframework.core.util;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.opensymphony.webwork.ServletActionContext;

/**
 * @ClassName: ContextHolderUtils
 * @Description: TODO(上下文工具类)
 */
public class ContextHolderUtils {
	private static ThreadLocal<Locale> curLocale = new ThreadLocal<Locale>();
	/**
	 * SpringMvc下获取request
	 * 
	 * @return
	 */
	public static HttpServletRequest getRequest() {
		if(StringUtil.isNotEmpty(RequestContextHolder.getRequestAttributes())){
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder
					.getRequestAttributes()).getRequest();
			return request;
		}
		return null;

	}

	/**
	 * SpringMvc下获取session
	 * 
	 * @return
	 */
	public static HttpSession getSession() {
		if(StringUtil.isNotEmpty(getRequest())){
			HttpSession session = getRequest().getSession();
			return session;
		}
		return null;
	}

	public static Locale getLocale() {
		if (curLocale.get() != null) {
			return (Locale) curLocale.get();
		}
		setLocale(new Locale("zh", "CN"));
		return (Locale) curLocale.get();
	}

	public static void setLocale(Locale locale) {
		curLocale.set(locale);
	}
	
	/**
	 * 获取HTTP名
	 * @return
	 */
	public static String getServerName(HttpServletRequest request){
		return request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath()+"/";
	}

}

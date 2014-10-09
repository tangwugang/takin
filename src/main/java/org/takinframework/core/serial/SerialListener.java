package org.takinframework.core.serial;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

/**
 * 串口通信监听器
 * @author twg
 *
 */
public class SerialListener extends SerialManager implements ServletContextListener {
	private static SerialManager serialManager = null;
	
	
	static {
		if(null == serialManager){
			serialManager = new SerialManager();
			serialManager.init();
		}
	}

	public static SerialManager serialManager(){
		if(null == serialManager){
			serialManager = new SerialManager();
			serialManager.init();
		}
		return serialManager;
	}

	public void contextInitialized(ServletContextEvent sce) {
		
	}

	public void contextDestroyed(ServletContextEvent sce) {
		serialManager.ClosePort();		
	}

}

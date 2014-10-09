package org.takinframework.core.util;

/**
 * 
 *
 */
public class ImportUtil {
	/**
	 * 得到实体类
	 * @param fullentity
	 * @return
	 */
	 @SuppressWarnings("rawtypes")
	public static  Class getEntityClass(String fullentity){
		 Class entityClass = null;
		try {
			entityClass = (Class) Class.forName(fullentity);
		} catch (ClassNotFoundException e) {			
			e.printStackTrace();
		} 
		 return entityClass;
	 }  
	 
	 
}

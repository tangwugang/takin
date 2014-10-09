package org.takinframework.core.util;


public class NameUtils {
	
	/**
	 * java实体名转换为数据库下划线“_”分隔的格式
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getUnderlineName(T entity){
		String className = entity.getClass().getSimpleName();
		StringBuffer sbTemp = new StringBuffer();
		for (int i = 1, size = className.length(); i < size; i++) {  
            char c = className.charAt(i);  
            if (Character.isUpperCase(c)) {  
                sbTemp.append("_").append(c);  
            } else {  
                sbTemp.append(c);  
            }  
        }
		
		return (T) (className.substring(0, 1)+sbTemp.toString()).toLowerCase();
	}
	
	
	/**
	 * java实体名转换为数据库下划线“_”分隔的格式
	 * @param entity
	 * @return 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getUnderlineName(Class<T> entity){
		String className = entity.getSimpleName();
		StringBuffer sbTemp = new StringBuffer();
		for (int i = 1, size = className.length(); i < size; i++) {  
            char c = className.charAt(i);  
            if (Character.isUpperCase(c)) {  
                sbTemp.append("_").append(c);  
            } else {  
                sbTemp.append(c);  
            }  
        }
		
		return (T) (className.substring(0, 1)+sbTemp.toString()).toLowerCase();
	}
	
	
	public static void main(String[] args){
		
	}
	

}

package org.takinframework.core.extend.datasource;
/**
 *类名：DataSourceContextHolder.java
 *功能：获得和设置上下文环境的类，主要负责改变上下文数据源的名称
 */
public class DataSourceContextHolder {

	private static final ThreadLocal<DataSourceType> contextHolder=new ThreadLocal<DataSourceType>();
	private static final ThreadLocal<String> dbTypeHolder=new ThreadLocal<String>();
	
	public static void setDataSourceType(DataSourceType dataSourceType){
		contextHolder.set(dataSourceType);
	}
	
	public static DataSourceType getDataSourceType(){
		return (DataSourceType) contextHolder.get();
	}
	
	public static void clearDataSourceType(){
		contextHolder.remove();
	}
	
	public static void setDbType(String dbType){
		dbTypeHolder.set(dbType);
	}
	
	public static String getDbType(){
		return (String) dbTypeHolder.get();
	}
	
	public static void clearDbType(){
		dbTypeHolder.remove();
	}
	
}

package org.takinframework.codegenerator.util;

/**
 * 字段类型转换类
 * @author twg
 *
 */
public class CodeType {
	//从数据库表生产实体
	/**
	 * 从数据库表生产实体获取字段类型
	 * @param type 字段类型
	 * @return
	 */
	public static String getType(String type){
    	type=type.toLowerCase();
    	if(type.contains("char")){
			return "String";
		}else if(type.startsWith("int")){
			return "Integer";
		}else if(type.startsWith("bigint")||type.startsWith("number")||type.startsWith("long")){
			return "Long";
		}else if (type.startsWith("double")||type.startsWith("float") || type.startsWith("decimal")) {
			return "Double";
		}else if("timestamp".equals(type) || "date".equals(type)  || "datetime".equals(type)){
			return "Date";
		}else if ("blob".equals(type)||"clob".equals(type)) {
			return "byte[]";
		}else if (type.startsWith("smallint")) {
			return "Short";
		} 
    	return "";
    }
	/**
	 * 生成数据库表
	 * @param dataType 类型int、char、number等
	 * @param columnLength 长度
	 * @param precisions 精度
	 * @param scale 小数点几位
	 * @return
	 */
	public static String setMySqlType(String dataType, int columnLength, int precisions, int scale){
		dataType=dataType.toLowerCase();
    	if(dataType.indexOf("char")>0){
			return "VARCHAR(" + columnLength + ')';
		}else if("int".equals(dataType) || "integer".equals(dataType)){
			return "BIGINT(" + columnLength + ")";
		}else  if ("number".equals(dataType)) {
		    return "DECIMAL(" + columnLength + "," + scale + ")";
		}else if ("double".equals(dataType)||"float".equals(dataType)) {
			return "DOUBLE(" + columnLength + "," + scale + ")";
		}else if("date".equals(dataType)){
			return "TIMESTAMP";
		}else if ("blob".equals(dataType)||"clob".equals(dataType)) {
			return "TEXT";
		}
    	return "";
    }

}

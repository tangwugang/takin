package org.takinframework.core.util;

import java.util.List;


/**
 * spring jdbc CRUD工具类
 * @author twg
 *
 */
public class CRUDUtil {
	
	public static <T> T update(T entity){
		
		return entity;
		
	}
	
	/**
	 * delete语句
	 * @param entity
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T delete(T entity){
		String sql = "delete from "+NameUtils.getUnderlineName(entity)+" where 1=1 ";
		StringBuilder sb = new StringBuilder(sql);
		List<String> fields = ClassReflectUtil.getFields(entity);
		for (int i = 0; i < fields.size(); i++) {
			if(fields.get(i).equals("id")){
				sb.append(" and ");
				sb.append(fields.get(i));
				sb.append("= ?");
			}
		}
		
		return (T) sb.toString();
	}
	/**
	 * delete语句
	 * @param entity
	 * @param params 参数
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T delete(T entity,Object...params){
		String sql = "delete from "+NameUtils.getUnderlineName(entity)+" where 1=1 ";
		StringBuilder sb = new StringBuilder(sql);
		List<String> fields = ClassReflectUtil.getFields(entity);
		for (int j = 0; j < params.length; j++) {
			Object object = params[j];
			for (int i = 0; i < fields.size(); i++) {
				if(fields.get(i).equals(object)){
					sb.append(" and ");
					sb.append(fields.get(i));
					sb.append("= ?");
					break;
				}
				
			}
		}
		return (T) sb.toString();
	}
	
	public static <T> T delete(Class<T> entity,Object...params){
		String sql = "delete from "+NameUtils.getUnderlineName(entity)+" where 1=1 ";
		StringBuilder sb = new StringBuilder(sql);
		List<String> fields = ClassReflectUtil.getFields(entity);
		for (int j = 0; j < params.length; j++) {
			Object object = params[j];
			for (int i = 0; i < fields.size(); i++) {
				if(fields.get(i).equals(object)){
					sb.append(" and ");
					sb.append(fields.get(i));
					sb.append("= ?");
					break;
				}
				
			}
		}
		return (T) sb.toString();
	}
	
	/**
	 * select语句
	 * @param entity 实体
	 * @param exclude select中不显示的列
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T select(Class<T> entity,String alias,String sql,String exclude){
		List<String> f = ClassReflectUtil.getFields(entity);
		StringBuilder b = new StringBuilder();
		String s = "select ";
		if(sql.startsWith("from") || sql.startsWith("FROM")){
			String c = " * ";
			if(StringUtil.isEmpty(alias)){
				for (String field : f) {
					b.append(field);
					b.append(",");
				}
				c = b.toString();
				String p[] = exclude.split(",");
				for (String string : p) {
					String d = string+",";
					if(c.contains(d)){
						c = c.replace(d, "");
					}
				}
			}else if(StringUtil.isNotEmpty(alias)){
				String p[] = alias.split(",");
				for (String string : p) {
					b.append(string);
					b.append(".*");
					b.append(",");
				}
				c = b.toString();
			}else{
				for (String field : f) {
					field = alias+"."+field;
					b.append(field);
					b.append(",");
				}
				c = b.toString();
				String p[] = exclude.split(",");
				for (String string : p) {
					String d = alias+"."+string+",";
					if(c.contains(d)){
						c = c.replace(d, "");
					}
				}
			}
			
			return (T) (s +=c.substring(0, c.length()-1)+" "+sql);
		}else{
			return (T) (s +=sql);
		}
		
	}

}

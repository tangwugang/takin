package org.takinframework.codegenerator.util;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.takinframework.core.util.ClassReflectUtil;
import org.takinframework.core.util.StringUtil;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 生成代码工具类
 * @author twg
 *
 */
public class CodeUtil {
	
	/**
	 * 获取所有列名字(按|分隔)
	 * @param columnList 数据库表字段类集合
	 * @return
	 * @throws SQLException
	 */
    public static String getColumnSplit(List<ColumnData> columnList) throws SQLException{
 	     StringBuffer commonColumns=new StringBuffer();
 	     for(ColumnData data : columnList){
 	    	 commonColumns.append(data.getColumnName()+"|");
 	     }
 	     return commonColumns.delete(commonColumns.length()-1, commonColumns.length()).toString();
    }
	
	 /**
     * 生成修改记录SQL(mybatis)
     * @param tableName 数据库表名
     * @param columnsList 字段
     * @return
     * @throws SQLException
     */
    public static String getUpdateSql(String tableName,String[] columnsList)throws SQLException{
    	 StringBuffer sb=new StringBuffer();
    	 
	     for(int i=1;i<columnsList.length;i++){
	    	  String column=columnsList[i];
	    	  if("CREATETIME".equals(column.toUpperCase()))
	    		  continue;
	    	  
	    	  if("UPDATETIME".equals(column.toUpperCase()))
	    		  sb.append(column+"=now()");
	    	  else
	    		  sb.append(column+"=#{"+column+"}");
	    	  //最后一个字段不需要","
	    	  if((i+1) < columnsList.length){
	    		  sb.append(",");
	    	  }
	     }
    	 String update = "update "+tableName+" set "+sb.toString()+" where "+columnsList[0]+"=#{"+columnsList[0]+"}";
	     return update;
    }
    /**
     * 生成修改记录SQL(mybatis)
     * @param tableName 数据库表名
     * @param columnList 字段
     * @return
     * @throws SQLException
     */
    public static String getUpdateSelectiveSql(String tableName,List<ColumnData> columnList)throws SQLException{
   	 StringBuffer sb=new StringBuffer();
   	    ColumnData cd = columnList.get(0); //获取第一条记录，主键
   	 	sb.append("\t<trim  suffixOverrides=\",\" >\n");
   	 	 for(int i=1;i<columnList.size();i++){
   	 		 ColumnData data = columnList.get(i);
   	 		 String columnName=data.getColumnName();
   	 		 sb.append("\t<if test=\"").append(columnName).append(" != null ");
   	 		 //String 还要判断是否为空
   	 		 if("String" == data.getDataType()){
   	 			sb.append(" and ").append(columnName).append(" != ''");
   	 		 }
   	 		 sb.append(" \">\n\t\t");
   	 		 sb.append(columnName+"=#{"+columnName+"},\n");
   	 		 sb.append("\t</if>\n");
	     }
	     sb.append("\t</trim>");
	     String update = "update "+tableName+" set \n"+sb.toString()+" where "+cd.getColumnName()+"=#{"+cd.getColumnName()+"}";
	     return update;
   }
    
    /**
     * 生成修改记录SQL(非mybatis)
     * @param tableName 数据库表名
     * @param columnList 字段
     * @param o 实体
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getUpdateSql(String tableName,List<ColumnData> columnList,Object o)throws Exception{
      	 StringBuffer sb = new StringBuffer();
      	 Map<String, Object> paramMap = Maps.newConcurrentMap();
      	 List<ColumnData> colums = Lists.newArrayList();
      	    ColumnData cd = columnList.get(0); //获取第一条记录，主键
      	    List<String> filedList = ClassReflectUtil.getFields(o);
      	    for (String string : filedList) {
      	    	if(null == ClassReflectUtil.getFieldValue(o, string)||
      	    			ClassReflectUtil.getFieldValue(o, string).equals("")){//||ClassReflectUtil.getFieldValue(o, string).equals("")
      	    		continue;
      	    	}
      	    	for(int i=1;i<columnList.size();i++){
         	 		 ColumnData data = columnList.get(i);
         	 		 String columnName=data.getColumnName();
         	 		 if(string.equals(columnName)){
         	 			 sb.append(columnName).append("= ?");
             	 		 sb.append(",");
             	 		 colums.add(data);
         	 			 break;
         	 		 }
      	    	}
			}
      	 colums.add(cd);	 
   	     String update = "update "+tableName+" set "+sb.toString().subSequence(0, sb.toString().length()-1)+" where "+cd.getColumnName()+"= ? ";
   	     paramMap.put("sql", update);
   	     paramMap.put("colums", colums);
   	     return paramMap;
      }
    /**
     * 生成查询记录SQL(非mybatis)
     * @param tableName 数据库表名
     * @param columnList 字段
     * @param param (where 条件对应实体中的属性)
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getSelectSql(String tableName,List<ColumnData> columnList,Object... param)throws Exception{
    	StringBuffer sb = new StringBuffer();
     	 Map<String, Object> paramMap = Maps.newConcurrentMap();
     	 List<ColumnData> colums = Lists.newArrayList();
	    	for(int i=0;i<columnList.size();i++){
		 		 ColumnData data = columnList.get(i);
		 		 String columnName=data.getColumnName();
		 		 for (int j = 0; j < param.length; j++) {
		 			if(param[j].equals(columnName)){
	   	 			 sb.append(" and ");
	   	 			 sb.append(columnName).append("= ?");
	       	 		 colums.add(data);
	   	 			 break;
	   	 		 }else if(null!=param[j] && 
	   	 				 ((String)param[j]).startsWith(columnName)){
	   	 			 sb.append(" and ");
	   	 			 sb.append(param[j]).append("?");
	       	 		 colums.add(data);
	   	 		 }
				}
	    	}
     	 String columns= CodeUtil.getColumnSplit(columnList);
     	 String select = "select "+columns.replaceAll("\\|", ",")+" from "+tableName+" where 1=1"+sb.toString();
 	     paramMap.put("sql", select);
 	     paramMap.put("colums", colums);
 	     return paramMap;
    	
    }
    
    
    
    /**
     * 生成查询记录SQL(非mybatis)
     * @param tableName 数据库表名
     * @param columnList 字段
     * @param param (where 条件对应实体中的属性)
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getSelectSql(String tableName,List<ColumnData> columnList,Map<String, Object> param)throws Exception{
    	StringBuffer sb = new StringBuffer();
     	 Map<String, Object> paramMap = Maps.newConcurrentMap();
	    	for(int i=0;i<columnList.size();i++){
		 		 ColumnData data = columnList.get(i);
		 		 String columnName=data.getColumnName();
		 		Set<String> key = param.keySet();
		 		for (Object ob : key) {
		 			if(ob.equals(columnName)){
		 				sb.append(" and ");
		 				sb.append(columnName).append("='");
		 				sb.append(param.get(ob));
		 				sb.append("'");
		 			}else if(null !=ob && 
		 					((String)ob).startsWith(columnName)){
		 				sb.append(" and ");
		 				sb.append(ob).append("'");
		 				sb.append(param.get(ob));
		 				sb.append("'");
		 			}
				}
	    	}
     	 String columns= CodeUtil.getColumnSplit(columnList);
     	 String select = "select "+columns.replaceAll("\\|", ",")+" from "+tableName+" where 1=1"+sb.toString();
 	     paramMap.put("sql", select);
 	     return paramMap;
    	
    }
    
    /**
     * 生成查询总数记录SQL(非mybatis)
     * @param tableName 数据库表名
     * @param columnList 字段
     * @param param (where 条件对应实体中的属性)
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getCountSql(String tableName,List<ColumnData> columnList,Map<String, Object> param)throws Exception{
    	StringBuffer sb = new StringBuffer();
     	 Map<String, Object> paramMap = Maps.newConcurrentMap();
	    	for(int i=0;i<columnList.size();i++){
		 		 ColumnData data = columnList.get(i);
		 		 String columnName=data.getColumnName();
		 		Set<String> key = param.keySet();
		 		for (Object ob : key) {
		 			if(ob.equals(columnName)){
		 				sb.append(" and ");
		 				sb.append(columnName).append("='");
		 				sb.append(param.get(ob));
		 				sb.append("'");
		 			}else if(null !=ob && 
		 					((String)ob).startsWith(columnName)){
		 				sb.append(" and ");
		 				sb.append(ob).append("'");
		 				sb.append(param.get(ob));
		 				sb.append("'");
		 			}
				}
		 		
		 		 
	    	}
     	 String select = "select count(1) from "+tableName+" where 1=1"+sb.toString();
 	     paramMap.put("sql", select);
 	     return paramMap;
    }
    
    
    /**
     * 生成查询总数记录SQL(非mybatis)
     * @param tableName 数据库表名
     * @param columnList 字段
     * @param param (where 条件对应实体中的属性)
     * @return
     * @throws Exception
     */
    public static Map<String, Object> getCountSql(String tableName,List<ColumnData> columnList,Object... param)throws Exception{
    	StringBuffer sb = new StringBuffer();
     	 Map<String, Object> paramMap = Maps.newConcurrentMap();
     	 List<ColumnData> colums = Lists.newArrayList();
	    	for(int i=0;i<columnList.size();i++){
		 		 ColumnData data = columnList.get(i);
		 		 String columnName=data.getColumnName();
		 		 for (int j = 0; j < param.length; j++) {
		 			if(param[j].equals(columnName)){
	   	 			 sb.append(" and ");
	   	 			 sb.append(columnName).append("= ?");
	       	 		 colums.add(data);
	   	 			 break;
	   	 		 }else if(null!=param[j] && 
	   	 				((String)param[j]).startsWith(columnName)){
	   	 			 sb.append(" and ");
	   	 			 sb.append(param[j]).append("?");
	       	 		 colums.add(data);
	   	 		 }
				}
		 		 
	    	}
     	 String select = "select count(1) from "+tableName+" where 1=1"+sb.toString();
 	     paramMap.put("sql", select);
 	     paramMap.put("colums", colums);
 	     return paramMap;
    	
    }
    /**
     * 生成查询记录SQL(mybatis)
     * @param tableName 数据库表名
     * @param columnList 字段
     * @return
     * @throws SQLException
     */
    public static String getSelectWhereSql(String tableName,List<ColumnData> columnList)throws SQLException{
      	 StringBuffer sb=new StringBuffer();
      	 	sb.append("where 1=1\n ");
      	 	sb.append("\t<trim  suffixOverrides=\"and|or\" >\n");
      	 	 for(int i=1;i<columnList.size();i++){
      	 		 ColumnData data = columnList.get(i);
      	 		 String columnName=data.getColumnName();
      	 		 sb.append("\t<if test=\"").append(columnName).append(" != null ");
      	 		 //String 还要判断是否为空
      	 		 if("String" == data.getDataType()){
      	 			sb.append(" and ").append(columnName).append(" != ''");
      	 		 }
      	 		 sb.append(" \">\n\t\t");
      	 		 sb.append(" and ");
      	 		 sb.append(columnName+"=#{"+columnName+"}\n");
      	 		 sb.append("\t</if>\n");
   	     }
   	     sb.append("\t</trim>");
   	     return sb.toString();
      }
	
	/**
     * 获取字段数组
     * @param columns
     * @return
     * @throws SQLException
     */
    public static String[] getColumnList(String columns)throws SQLException{
    	 String[] columnList=columns.split("[|]");
	     return columnList;
    }
	
	/**
     * 获取所有的字段，并按","分割
     * @param columns
     * @return
     * @throws SQLException
     */
    public static String getColumnFields(String columns)throws SQLException{
    	String fields = columns;
    	if(fields != null && !"".equals(fields)){
    		fields = fields.replaceAll("[|]", ",");
    	}
    	return fields;
    }
	
	 /**
     * 根据id查询
     * @param tableName
     * @param columnsList
     * @return
     * @throws SQLException
     */
    public static String getSelectByIdSql( String tableName,String[] columnsList)throws SQLException{
    	 StringBuffer sb=new StringBuffer();
    	 sb.append("select <include refid=\"Base_Column_List\" /> \n");
 	     sb.append("\t from ").append(tableName).append(" where ");
 	     sb.append(columnsList[0]).append(" = #{").append(columnsList[0]).append("}");
    	return sb.toString();
    }
	
	/**
     * 删除语句(mybatis)
     * @param tableName 数据库表名
     * @param columnsList 字段
     * @return
     * @throws SQLException
     */
    public static String getDeleteSql( String tableName,String[] columnsList)throws SQLException{
	   	 StringBuffer sb=new StringBuffer();
	   	 sb.append("delete ");
	     sb.append("\t from ").append(tableName).append(" where ");
	     sb.append(columnsList[0]).append(" = #{").append(columnsList[0]).append("}");
	   	return sb.toString();
   }
	 
  /**
	 * 表名转换成类名 每_首字母大写
	 * @param tableName 数据库表名
	 * @return
	 */
	public static String getTablesNameToClassName(String tableName){
    	String [] split=tableName.split("_");
    	if(split.length>1){
    		StringBuffer sb=new StringBuffer();
            for(int i=0;i<split.length;i++){
            	String tempTableName=split[i].substring(0, 1).toUpperCase()+split[i].substring(1, split[i].length());
                sb.append(tempTableName);
            }
            return sb.toString();
    	}else{
    		String tempTables=split[0].substring(0, 1).toUpperCase()+split[0].substring(1, split[0].length());
    		return tempTables;
    	}
    }
	/**
	 * 类名转换成表名除第一个首字母，别的遇到大写的字母时前面加_
	 * @param className 实体名
	 * @return
	 */
	public static String getTablesNameFromClassName(String className){
		char[] c = className.toCharArray();
		StringBuilder s = new StringBuilder();
		for (int i = 0; i < c.length; i++) {
			if(StringUtil.isMatch(String.valueOf(c[i]), "^[A-Z]+$") && i>0){
				s.append("_");
				s.append(String.valueOf(c[i]).toLowerCase());
			}else{
				s.append(String.valueOf(c[i]).toLowerCase());
			}
		}
		return s.toString();
		
	}

}

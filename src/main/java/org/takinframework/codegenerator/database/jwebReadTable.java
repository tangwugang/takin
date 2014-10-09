package org.takinframework.codegenerator.database;

import java.sql.SQLException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.springframework.jdbc.core.JdbcTemplate;
import org.takinframework.codegenerator.util.CodeType;
import org.takinframework.codegenerator.util.CodeUtil;
import org.takinframework.codegenerator.util.ColumnData;
import org.takinframework.codegenerator.util.DbType;

import com.google.common.collect.Lists;

/**
 * 读取数据库表信息工具类
 * @author twg
 *
 */
public class jwebReadTable {
	/**
	 * 存放表字段信息集合
	 */
	private static final Map<Object, Object> columDataMap = Collections.synchronizedMap(new WeakHashMap<Object, Object>());
	/**
	 * 生成实体时的set、get方法
	 */
	private static String method;
	/**
	 * 生成实体时的属性
	 */
    private static String argv;
    /**
     * 生成实体的属性+set,get方法
     * @param tableName 数据库表名
     * @param dbName 数据库名
     * @param jdbcTemplate spring jdbc
     * @param dbType 数据库类型mysql、oracle
     * @return
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
	public static String getBeanFeilds(String tableName,String dbName,JdbcTemplate jdbcTemplate,String dbType) throws SQLException{
    	List<ColumnData> dataList = null;
    	if(null != columDataMap && columDataMap.size()>0){
    		dataList = (List<ColumnData>) columDataMap.get(tableName);
    	}else{
    		dataList = getColumnDatas(tableName,dbName,jdbcTemplate,dbType);
    		columDataMap.put(tableName, dataList);
    	}
    	StringBuffer str = new StringBuffer();
    	StringBuffer getset = new StringBuffer();
    	
        for(ColumnData d : dataList){
        	
        	String name = d.getColumnName();
			String type =  d.getDataType();
			String comment =  d.getColumnComment();
			String defaultValue = d.getDefaultValue();
			if(name.equals("id")){
				continue;
	        }
			if(name.equals("createBy")){
				continue;
	        }
			if(name.equals("createName")){
				continue;
	        }
			if(name.equals("createDate")){
				continue;
	        }
			if(name.equals("updateDate")){
				continue;
	        }
			if(name.equals("updateBy")){
				continue;
	        }
			if(name.equals("updateName")){
				continue;
	        }
			
			String maxChar=name.substring(0, 1).toUpperCase();
			if(null!= defaultValue){
				str.append("\r\t").append("private ").append(type+" ").append(name).append(" = ").append(type+".valueOf(\"").append(defaultValue).append("\")").append(";//   ").append(comment);
				
			}else{
				str.append("\r\t").append("private ").append(type+" ").append(name).append(";//   ").append(comment);
			}
			
			String method=maxChar+name.substring(1, name.length());
			getset.append("\r\t").append("public ").append(type+" ").append("get"+method+"() {\r\t");
			getset.append("    return this.").append(name).append(";\r\t}");
			getset.append("\r\t").append("public void ").append("set"+method+"("+type+" "+name+") {\r\t");
			getset.append("    this."+name+"=").append(name).append(";\r\t}");
        }
        argv=str.toString();
        method=getset.toString();
		return argv+method;
    }
    
    /**
     * 查询表的字段，封装成List
     * @param tableName 数据库表名
     * @param dbName 数据库名
     * @param jdbcTemplate spring jdbc
     * @param dbType 数据库类型mysql、oracle
     * @return
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
	public static List<ColumnData> getColumnDatas(String tableName,String dbName,JdbcTemplate jdbcTemplate,String dbType) throws SQLException{
    	if(null != columDataMap && columDataMap.size()>0){
    		
    		if(null!=((List<ColumnData>) columDataMap.get(tableName)) &&
    				((List<ColumnData>) columDataMap.get(tableName)).size()>0){
    			return (List<ColumnData>) columDataMap.get(tableName);
    		}
    		return getColumnDataList(tableName, dbName, jdbcTemplate, dbType);
    	}
		return getColumnDataList(tableName, dbName, jdbcTemplate, dbType);
    }
    
    private static List<ColumnData> getColumnDataList(String tableName,String dbName,JdbcTemplate jdbcTemplate,String dbType){
    	String SQLColumns="";
    	List<ColumnData> dataList = Lists.newArrayList();
    	if (dbType.equals(DbType.MySQL.toString())||dbType.toUpperCase().equals(DbType.MySQL.toString())) {
    		SQLColumns="SELECT column_name,column_type,column_comment,column_default,column_key,IS_NULLABLE,CHARACTER_OCTET_LENGTH LENGTH, NUMERIC_PRECISION PRECISIONS,NUMERIC_SCALE SCALE,TABLE_NAME FROM  INFORMATION_SCHEMA.COLUMNS  WHERE TABLE_SCHEMA=DATABASE() AND TABLE_NAME='"+tableName+"'";
		}
    	if (dbType.equals(DbType.ORACLE.toString())||dbType.toUpperCase().equals(DbType.ORACLE.toString())) {
    		SQLColumns="select a.column_name,a.data_type,a.data_length,b.comments from cols a,user_col_comments b  where a.column_name=b.column_name and a.table_name=b.table_name and b.table_name='"+tableName+"'";
		}
    	List<Map<String, Object>> list = jdbcTemplate.queryForList(SQLColumns);
    	for (Map<String, Object> map : list) {
    		String name = (String) map.get("column_name");
    		String comment = (String) map.get("column_comment");
    		String defaultValue = (String) map.get("column_default");
    		String type = CodeType.getType((String) map.get("column_type"));
    		ColumnData cd= new ColumnData();
			cd.setColumnName(name);
			cd.setDataType(type);
			cd.setColumnComment(comment);
			cd.setDefaultValue(defaultValue);
			dataList.add(cd);
		}
    	StringBuffer str = new StringBuffer();
		StringBuffer getset=new StringBuffer();
        argv=str.toString();
        method=getset.toString();
		columDataMap.put(tableName, dataList);
		return dataList;
    	
    }
    
    /**
     * 生成(mybaits)sql语句
     * @param tableName 数据库表名
     * @param dbName 数据库名
     * @param jdbcTemplate spring jdbc
     * @param dbType 数据库类型mysql、oracle
     * @return
     * @throws Exception
     */
	 public static Map<String,Object> getAutoCreateSql(String tableName,String dbName,JdbcTemplate jdbcTemplate,String dbType) throws Exception{
   	 	 Map<String,Object> sqlMap=new HashMap<String, Object>();
   	 	 List<ColumnData> columnDatas = jwebReadTable.getColumnDatas(tableName,dbName,jdbcTemplate,dbType);
	     String columns= CodeUtil.getColumnSplit(columnDatas);
	     String[] columnList =  CodeUtil.getColumnList(columns);  //表所有字段
	     String columnFields =  CodeUtil.getColumnFields(columns); //表所有字段 按","隔开
	     String insert="insert into "+tableName+"("+columns.replaceAll("\\|", ",")+")\n values(#{"+columns.replaceAll("\\|", "},#{")+"})";
	     String update= CodeUtil.getUpdateSql(tableName, columnList);
	     String updateSelective = CodeUtil.getUpdateSelectiveSql(tableName, columnDatas);
	     String selectById = CodeUtil.getSelectByIdSql(tableName, columnList);
	     String delete = CodeUtil.getDeleteSql(tableName, columnList);
	     String whereSql =  CodeUtil.getSelectWhereSql(tableName, columnDatas);
	     sqlMap.put("columnList",columnList);
	     sqlMap.put("columnFields",columnFields);
	     sqlMap.put("whereSql", whereSql);
	     sqlMap.put("insert", insert.replace("#{createDate}", "now()").replace("#{updateDate}", "now()"));
	     sqlMap.put("update", update.replace("#{updateDate}", "now()"));
	     sqlMap.put("delete", delete);
	     sqlMap.put("updateSelective", updateSelective);
	     sqlMap.put("selectById", selectById);
	     sqlMap.put("orderBy", "order by ${page.orderBy}");
	     return sqlMap;
   }
    

}

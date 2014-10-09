package org.takinframework.core.common.dao.impl;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.PreparedStatementSetter;
import org.springframework.stereotype.Component;
import org.takinframework.codegenerator.database.jwebReadTable;
import org.takinframework.codegenerator.util.CodeUtil;
import org.takinframework.codegenerator.util.ColumnData;
import org.takinframework.core.common.dao.JdbcDao;
import org.takinframework.core.common.exception.BusinessException;
import org.takinframework.core.extend.datasource.DataSourceContextHolder;
import org.takinframework.core.page.PageBean;
import org.takinframework.core.util.CRUDUtil;
import org.takinframework.core.util.ClassReflectUtil;
import org.takinframework.core.util.JdbcSqlUtil;
import org.takinframework.core.util.LogUtil;
import org.takinframework.core.util.NameUtils;
import org.takinframework.core.util.Page;
import org.takinframework.core.util.PageUtil;
import org.takinframework.core.util.StringUtil;
import org.takinframework.spring.jdbc.core.MyBatchPreparedStatementSetter;
import org.takinframework.spring.jdbc.core.MyBeanPropertyRowMapper;

import com.google.common.collect.Lists;

/**
 * jdbc数据源
 * @author twg
 * @param <T>
 */
@Component
public class JdbcDaoImpl implements JdbcDao {
	
	@Autowired
	private  JdbcTemplate jdbcTemplate;
	
	
	/**
	 * 添加，更新，删除接口
	 */
	public <T> int addOrUpdateOrDelete(String sql, final Object[] parameters, Class<T> cl) {
		int num = 0;
		if (parameters == null || parameters.length == 0){
			num = jdbcTemplate.update(sql);
		}else {
			num = jdbcTemplate.update(sql, new PreparedStatementSetter() {
				public void setValues(PreparedStatement ps)
						throws SQLException {
					for (int i = 0; i < parameters.length; i++){
						ps.setObject(i + 1, parameters[i]);
					}
				}
			});
		}
		return num;
	}
	
	
	/**
	 * 批量添加
	 */
	public <T> int[] batchSave(List<T> entitys) throws SQLException {
		String tableName = "";
		for (T t : entitys) {
			tableName = (String) NameUtils.getUnderlineName(t);
			break;
		}
		if(StringUtil.isEmpty(tableName))
			throw new BusinessException("实体对应数据库表名失败");
		List<ColumnData> columnDatas = jwebReadTable.getColumnDatas(tableName,ResourceBundle.getBundle("jweb/jweb_database").getString("database_name"),jdbcTemplate,getDBType().toString());
		String columns= CodeUtil.getColumnSplit(columnDatas);
	    String columnFields =  CodeUtil.getColumnFields(columns);
		String sql = "insert into  "+tableName+"("+columnFields+") values (";
	    for (int i = 0; i < columnDatas.size(); i++) {
			sql+="?";
			sql+=",";
		}
	    sql = sql.substring(0, sql.length()-1);
	    sql+=")";
	    LogUtil.info("sql语句:"+sql);
		return jdbcTemplate.batchUpdate(sql, new MyBatchPreparedStatementSetter<T>(entitys,columnDatas));
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> int[] batchUpdate(List<T> entitys) throws Exception {
		String tableName = "";
		for (T t : entitys) {
			tableName = (String) NameUtils.getUnderlineName(t);
			break;
		}
		if(StringUtil.isEmpty(tableName))
			throw new BusinessException("实体对应数据库表名失败");
		List<ColumnData> columnDatas = jwebReadTable.getColumnDatas(tableName,ResourceBundle.getBundle("jweb/jweb_database").getString("database_name"),jdbcTemplate,getDBType().toString());
		Map<String, Object> paramMap = CodeUtil.getUpdateSql(tableName, columnDatas, entitys.get(0));
		String sql= (String) paramMap.get("sql");
		columnDatas = (List<ColumnData>) paramMap.get("colums");
	    LogUtil.info("sql语句:"+sql);
	    
		return jdbcTemplate.batchUpdate(sql, new MyBatchPreparedStatementSetter<T>(entitys,columnDatas));
	}
	
	
	/**
	 * 检测实体是否存在(按sql)
	 */
	public <T> T checkEntityExits(Class<T> entity,String methord) {
		String className = ClassReflectUtil.getEntityName(entity);
		String sql = JdbcSqlUtil.getVal(className,methord, "checkEntityExits");
		String par = JdbcSqlUtil.getPar(className,methord, "checkEntityExits");
		String exclude = JdbcSqlUtil.getExc(className,methord, "checkEntityExits");
		String alias = JdbcSqlUtil.getAlia(className,methord, "checkEntityExits");
		String[] param = par.split(",");
		List<Object> list = Lists.newLinkedList();
		for (String string : param) {
			try {
				list.add(ClassReflectUtil.getFieldValue(entity, string));
			} catch (Exception e) {
				LogUtil.error("ClassReflectUtil反射java实体的属性值失败", e);
			}
		}
		for (Iterator<Object> iterator = list.iterator(); iterator.hasNext();) {
			par +=","+ iterator.next();
			
		}
		sql = (String) CRUDUtil.select(entity,alias, sql,exclude);
		LogUtil.info("sql语句:"+sql+" - param参数:"+par);
		List<Map<String, Object>> res = jdbcTemplate.queryForList(sql, list.toArray());
		return BeanPropertyRowMapper(res,entity);
		
	}
	/**
	 * 检测实体是否存在(按id)
	 */
	public <T> T checkEntityExits(T entity) throws Exception {
		List<Object> list = Lists.newLinkedList();
		list.add("id");
		return checkEntityExits(entity,list.toArray());
	}
	/**
	 * 检测实体是否存在(按自定义参数)
	 */
	@SuppressWarnings("unchecked")
	public <T> T checkEntityExits(T entity, Object... param)
			throws Exception {
		String tableName = (String) NameUtils.getUnderlineName(entity);
		List<ColumnData> columnDatas = jwebReadTable.getColumnDatas(tableName,ResourceBundle.getBundle("jweb/jweb_database").getString("database_name"),jdbcTemplate,getDBType().toString());
		Map<String, Object> paramMap = CodeUtil.getSelectSql(tableName, columnDatas,param);
		String sql = (String) paramMap.get("sql");
		columnDatas = (List<ColumnData>) paramMap.get("colums");
		LogUtil.info("sql语句:"+sql);
		List<Object> list = Lists.newLinkedList();
		for (ColumnData c : columnDatas) {
			try {
				list.add(ClassReflectUtil.getFieldValue(entity, c.getColumnName()));
			} catch (Exception e) {
				LogUtil.error("ClassReflectUtil反射java实体的属性值失败", e);
			}
		}
		List<Map<String, Object>> res = jdbcTemplate.queryForList(sql, list.toArray());
		return BeanPropertyRowMapper(res,entity);
	}
	
	/**
	 * 删除实体集合
	 */
	public <T> void delete(Collection<T> entities) {
		for (T t : entities) {
			delete(t);
		}
	}
	/**
	 * 按id删除
	 */
	public <T> void delete(T entity) {
		List<Object> list = Lists.newLinkedList();
		list.add("id");
		delete(entity, list.toArray());
	}
	
	/**
	 * 按自定义属性删除
	 */
	public <T> void delete(T entity, Object... params) {
		String sql = (String) CRUDUtil.delete(entity,params);
		List<Object> list = Lists.newLinkedList();
		for (int i = 0; i < params.length; i++) {
			Object object = params[i];
			try {
	    		list.add(ClassReflectUtil.getFieldValue(entity, (String)object));
			} catch (Exception e) {
				LogUtil.error("ClassReflectUtil反射java实体的属性值失败", e);
			}
		}
		LogUtil.info("sql语句:"+sql);
		executeSql(sql, list.toArray());
	}
	/**
	 * 按自定义SQL删除
	 */
	public <T> boolean delete(Class<T> entity,String methord, Object... param) {
		String className = ClassReflectUtil.getEntityName(entity);
		String sql = JdbcSqlUtil.getVal(className,methord, "delete");
		String par = JdbcSqlUtil.getPar(className,methord, "delete");
		List<Object> list = Lists.newLinkedList();
		for (Object object : param) {
			list.add(object);
		}
		for (Iterator<Object> iterator = list.iterator(); iterator.hasNext();) {
			par +=","+ iterator.next();
		}
		LogUtil.info("sql语句:"+sql+" - param参数:"+par);
		if(executeSql(sql,list.toArray())>0){
			return true;
		}
		return false;
	}
	
	
	public Integer executeSql(String sql, Object... param) {
		return jdbcTemplate.update(sql, param);
	}
	
	
	/**
	 * 查询接口
	 */
	public <T> List<T> find(String sql, Object[] parameters, Class<T> cl) {
		List<T> resultList = null;
		if (parameters != null && parameters.length > 0){
			resultList = jdbcTemplate.query(sql, parameters, new BeanPropertyRowMapper<T>(cl));
		}else {
			resultList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<T>(cl));
		}
		
		
		return resultList;
	}
	
	/**
	 * 查询接口List<Map<String, Object>>
	 * @param sql
	 * @return
	 */
	public  List<Map<String, Object>> findForJdbc(String sql){
		return jdbcTemplate.queryForList(sql);
	}
	
	public <T> List<T> findList(Class<T> entity,String methord, Serializable id) {
		String className = ClassReflectUtil.getEntityName(entity);
		String sql = JdbcSqlUtil.getVal(className,methord, "findList");
		String par = JdbcSqlUtil.getPar(className,methord, "findList");
		String exclude = JdbcSqlUtil.getExc(className,methord, "findList");
		String alias = JdbcSqlUtil.getAlia(className,methord, "findList");
		List<Object> list = Lists.newLinkedList();
		list.add(id);
		for (Iterator<Object> iterator = list.iterator(); iterator.hasNext();) {
			par +=","+ iterator.next();
		}
		sql = (String) CRUDUtil.select(entity,alias, sql,exclude);
		LogUtil.info("sql语句:"+sql+" - param参数:"+par);
		List<T> res = jdbcTemplate.query(sql, new BeanPropertyRowMapper<T>(entity),list.toArray());
		return res;
	}
	
	
	public <T> List<T> findList(Class<T> entity,String methord, Object... param) {
		String className = ClassReflectUtil.getEntityName(entity);
		String sql = JdbcSqlUtil.getVal(className,methord, "findList");
		String par = JdbcSqlUtil.getPar(className,methord, "findList");
		String exclude = JdbcSqlUtil.getExc(className,methord, "findList");
		String alias = JdbcSqlUtil.getAlia(className,methord, "findList");
		List<Object> list = Lists.newLinkedList();
		for (Object object : param) {
			list.add(object);
		}
		for (Iterator<Object> iterator = list.iterator(); iterator.hasNext();) {
			par +=","+ iterator.next();
		}
		sql = (String) CRUDUtil.select(entity,alias, sql,exclude);
		LogUtil.info("sql语句:"+sql+" - param参数:"+par);
		List<T> res = jdbcTemplate.query(sql, new BeanPropertyRowMapper<T>(entity),list.toArray());
		return res;
	}
	
	
	public <T> List<T> findList(T entity) throws Exception {
		List<Object> list = Lists.newLinkedList();
		list.add("id");
		return findList(entity,list.toArray());
	}
	@SuppressWarnings("unchecked")
	public <T> List<T> findList(T entity, Object... param) throws Exception {
		String tableName = (String) NameUtils.getUnderlineName(entity);
		List<ColumnData> columnDatas = jwebReadTable.getColumnDatas(tableName,ResourceBundle.getBundle("jweb/jweb_database").getString("database_name"),jdbcTemplate,getDBType().toString());
		Map<String, Object> paramMap = CodeUtil.getSelectSql(tableName, columnDatas,param);
		String sql = (String) paramMap.get("sql");
		List<ColumnData> columns = (List<ColumnData>) paramMap.get("colums");
		
		LogUtil.info("sql语句:"+sql);
		List<Object> list = Lists.newLinkedList();
		for (ColumnData c : columns) {
			try {
				list.add(ClassReflectUtil.getFieldValue(entity, c.getColumnName()));
			} catch (Exception e) {
				LogUtil.error("ClassReflectUtil反射java实体的属性值失败", e);
			}
		}
		List<T> res = jdbcTemplate.query(sql, new MyBeanPropertyRowMapper<T>(entity, columnDatas), list.toArray());
		return res;
	}
	
	public <T> List<T> findList(T entity, Map<String, Object> param)
			throws Exception {
		String tableName = (String) NameUtils.getUnderlineName(entity);
		List<ColumnData> columnDatas = jwebReadTable.getColumnDatas(tableName,ResourceBundle.getBundle("jweb/jweb_database").getString("database_name"),jdbcTemplate,getDBType().toString());
		Map<String, Object> paramMap = CodeUtil.getSelectSql(tableName, columnDatas,param);
		String sql = (String) paramMap.get("sql");
		LogUtil.info("sql语句:"+sql);
		return jdbcTemplate.query(sql, new MyBeanPropertyRowMapper<T>(entity, columnDatas));
	}
	
	@SuppressWarnings("unchecked")
	public <T> List<T> findListByPage(T entity, Object... param)
			throws Exception {
		String tableName = (String) NameUtils.getUnderlineName(entity);
		List<ColumnData> columnDatas = jwebReadTable.getColumnDatas(tableName,ResourceBundle.getBundle("jweb/jweb_database").getString("database_name"),jdbcTemplate,getDBType().toString());
		Map<String, Object> paramMap = CodeUtil.getSelectSql(tableName, columnDatas,param);
		String sql = (String) paramMap.get("sql");
		
		List<ColumnData> columns = (List<ColumnData>) paramMap.get("colums");
		String dbType = DataSourceContextHolder.getDbType();
		sql = PageBean.doPage(dbType, sql, entity, getEntityCount(entity, param));
		LogUtil.info("sql语句:"+sql);
		List<Object> list = Lists.newLinkedList();
		for (ColumnData c : columns) {
			try {
				list.add(ClassReflectUtil.getFieldValue(entity, c.getColumnName()));
			} catch (Exception e) {
				LogUtil.error("ClassReflectUtil反射java实体的属性值失败", e);
			}
		}
		List<T> res = jdbcTemplate.query(sql, new MyBeanPropertyRowMapper<T>(entity, columnDatas), list.toArray());
		return res;
	}
	
	@SuppressWarnings("unchecked")
	public <T> Page<T> findListByPage(Page<T> page,T entity, Object... param)
			throws Exception {
		String tableName = (String) NameUtils.getUnderlineName(entity);
		List<ColumnData> columnDatas = jwebReadTable.getColumnDatas(tableName,ResourceBundle.getBundle("jweb/jweb_database").getString("database_name"),jdbcTemplate,getDBType().toString());
		Map<String, Object> paramMap = CodeUtil.getSelectSql(tableName, columnDatas,param);
		String sql = (String) paramMap.get("sql");
		
		List<ColumnData> columns = (List<ColumnData>) paramMap.get("colums");
		String dbType = DataSourceContextHolder.getDbType();
		sql = PageBean.doPage(dbType, sql, page, getEntityCount(entity, param));
		LogUtil.info("sql语句:"+sql);
		List<Object> list = Lists.newLinkedList();
		for (ColumnData c : columns) {
			try {
				list.add(ClassReflectUtil.getFieldValue(entity, c.getColumnName()));
			} catch (Exception e) {
				LogUtil.error("ClassReflectUtil反射java实体的属性值失败", e);
			}
		}
		List<T> res = jdbcTemplate.query(sql, new MyBeanPropertyRowMapper<T>(entity, columnDatas), list.toArray());
		page.setList(res);
		return page;
	}
	
	public <T> Page<T> findListByPage(Page<T> page,T entity, Map<String, Object> param)
			throws Exception {
		String tableName = (String) NameUtils.getUnderlineName(entity);
		List<ColumnData> columnDatas = jwebReadTable.getColumnDatas(tableName,ResourceBundle.getBundle("jweb/jweb_database").getString("database_name"),jdbcTemplate,getDBType().toString());
		Map<String, Object> paramMap = CodeUtil.getSelectSql(tableName, columnDatas,param);
		String sql = (String) paramMap.get("sql");
		
		String dbType = DataSourceContextHolder.getDbType();
		sql = PageBean.doPage(dbType, sql, page, getEntityCount(entity, param));
		LogUtil.info("sql语句:"+sql);
		List<T> res = jdbcTemplate.query(sql, new MyBeanPropertyRowMapper<T>(entity, columnDatas));
		page.setList(res);
		return page;
	}
	
	
	public <T> PageUtil<T> findListByPage(PageUtil<T> page, T entity,
			Map<String, Object> param) throws Exception {
		String tableName = (String) NameUtils.getUnderlineName(entity);
		List<ColumnData> columnDatas = jwebReadTable.getColumnDatas(tableName,ResourceBundle.getBundle("jweb/jweb_database").getString("database_name"),jdbcTemplate,getDBType().toString());
		Map<String, Object> paramMap = CodeUtil.getSelectSql(tableName, columnDatas,param);
		String sql = (String) paramMap.get("sql");
		
		String dbType = DataSourceContextHolder.getDbType();
		sql = PageBean.doPage(dbType, sql, page, getEntityCount(entity, param));
		LogUtil.info("sql语句:"+sql);
		List<T> res = jdbcTemplate.query(sql, new MyBeanPropertyRowMapper<T>(entity, columnDatas));
		page.setList(res);
		return page;
	}
	
	
	public <T> List<T> findAll(Class<T> entity,String methord) {
		String className = ClassReflectUtil.getEntityName(entity);
		String sql = JdbcSqlUtil.getVal(className, methord,"findAll");
		String par = JdbcSqlUtil.getPar(className, methord,"findAll");
		String exclude = JdbcSqlUtil.getExc(className,methord, "findAll");
		String alias = JdbcSqlUtil.getAlia(className,methord, "findAll");
		
		List<Object> list = Lists.newLinkedList();
		if(StringUtil.isNotEmpty(par)){
			String[] param = par.split(",");
			for (String string : param) {
				try {
					list.add(ClassReflectUtil.getFieldValue(entity, string));
				} catch (Exception e) {
					LogUtil.error("ClassReflectUtil反射java实体的属性值失败", e);
				}
			}
		}
		for (Iterator<Object> iterator = list.iterator(); iterator.hasNext();) {
			par +=","+ iterator.next();
			
		}
		sql = (String) CRUDUtil.select(entity,alias, sql,exclude);
		LogUtil.info("sql语句:"+sql+" - param参数:"+par);
		List<T> res = jdbcTemplate.query(sql, new BeanPropertyRowMapper<T>(entity),list.toArray());
		return res;
	}
	
	
	
	public <T> List<T> findAll(Class<T> entity) throws SQLException {
		String tableName = (String) NameUtils.getUnderlineName(entity);
		List<ColumnData> columnDatas = jwebReadTable.getColumnDatas(tableName,ResourceBundle.getBundle("jweb/jweb_database").getString("database_name"),jdbcTemplate,getDBType().toString());
		String columns= CodeUtil.getColumnSplit(columnDatas);
	    String columnFields =  CodeUtil.getColumnFields(columns);
	    String sql = "select "+columnFields+" from "+tableName;
	    LogUtil.info("sql语句:"+sql);
		List<T> res = jdbcTemplate.query(sql, new BeanPropertyRowMapper<T>(entity));
		return res;
	}
	
	public <T> List<T> findAllByPage(T entity) throws Exception {
		String tableName = (String) NameUtils.getUnderlineName(entity);
		List<ColumnData> columnDatas = jwebReadTable.getColumnDatas(tableName,ResourceBundle.getBundle("jweb/jweb_database").getString("database_name"),jdbcTemplate,getDBType().toString());
		String columns= CodeUtil.getColumnSplit(columnDatas);
	    String columnFields =  CodeUtil.getColumnFields(columns);
	    String sql = "select "+columnFields+" from "+tableName;
	    String dbType = DataSourceContextHolder.getDbType();
		sql = PageBean.doPage(dbType, sql, entity, getEntityCount(entity));
	    LogUtil.info("sql语句:"+sql);
		List<T> res = jdbcTemplate.query(sql, new MyBeanPropertyRowMapper<T>(entity, columnDatas));
		return res;
	}
	
	
	public <T> Page<T> findAllByPage(Page<T> page, T entity) throws Exception {
		String tableName = (String) NameUtils.getUnderlineName(entity);
		List<ColumnData> columnDatas = jwebReadTable.getColumnDatas(tableName,ResourceBundle.getBundle("jweb/jweb_database").getString("database_name"),jdbcTemplate,getDBType().toString());
		String columns= CodeUtil.getColumnSplit(columnDatas);
	    String columnFields =  CodeUtil.getColumnFields(columns);
	    String sql = "select "+columnFields+" from "+tableName;
	    String dbType = DataSourceContextHolder.getDbType();
	    sql = PageBean.doPage(dbType, sql, page, getEntityCount(entity));
	    LogUtil.info("sql语句:"+sql);
	    List<T> res = jdbcTemplate.query(sql, new MyBeanPropertyRowMapper<T>(entity, columnDatas));
	    page.setList(res);
		return page;
	}
	
	public <T> PageUtil<T> findAllByPage(PageUtil<T> page, T entity)
			throws Exception {
		String tableName = (String) NameUtils.getUnderlineName(entity);
		List<ColumnData> columnDatas = jwebReadTable.getColumnDatas(tableName,ResourceBundle.getBundle("jweb/jweb_database").getString("database_name"),jdbcTemplate,getDBType().toString());
		String columns= CodeUtil.getColumnSplit(columnDatas);
	    String columnFields =  CodeUtil.getColumnFields(columns);
	    String sql = "select "+columnFields+" from "+tableName;
	    String dbType = DataSourceContextHolder.getDbType();
	    sql = PageBean.doPage(dbType, sql, page, getEntityCount(entity));
	    LogUtil.info("sql语句:"+sql);
	    List<T> res = jdbcTemplate.query(sql, new MyBeanPropertyRowMapper<T>(entity, columnDatas));
	    page.setList(res);
		return page;
	}
	
	public <T> Integer getEntityCount(Class<T> entity, String methord, Serializable id) {
		String className = ClassReflectUtil.getEntityName(entity);
		String sql = JdbcSqlUtil.getVal(className,methord, "getEntityCount");
		String par = JdbcSqlUtil.getPar(className,methord, "getEntityCount");
		String exclude = JdbcSqlUtil.getExc(className,methord, "getEntityCount");
		String alias = JdbcSqlUtil.getAlia(className,methord, "getEntityCount");
		List<Object> list = Lists.newLinkedList();
		list.add(id);
		for (Iterator<Object> iterator = list.iterator(); iterator.hasNext();) {
			par +=","+ iterator.next();
			
		}
		sql = (String) CRUDUtil.select(entity,alias, sql,exclude);
		LogUtil.info("sql语句:"+sql+" - param参数:"+par);
		
		return jdbcTemplate.queryForInt(sql, list.toArray());
	}
	
	public <T> Integer getEntityCount(T entity) throws Exception {
		List<Object> list = Lists.newLinkedList();
		return getEntityCount(entity,list.toArray());
	}
	
	public <T> Integer getEntityCountById(T entity) throws Exception {
		List<Object> list = Lists.newLinkedList();
		list.add("id");
		return getEntityCount(entity,list.toArray());
	}
	
	public <T> Integer getEntityCount(T entity, Map<String, Object> param) throws Exception {
		String tableName = (String) NameUtils.getUnderlineName(entity);
		List<ColumnData> columnDatas = jwebReadTable.getColumnDatas(tableName,ResourceBundle.getBundle("jweb/jweb_database").getString("database_name"),jdbcTemplate,getDBType().toString());
		Map<String, Object> paramMap = CodeUtil.getCountSql(tableName, columnDatas,param);
		String sql = (String) paramMap.get("sql");
		LogUtil.info("sql语句:"+sql);
		return jdbcTemplate.queryForInt(sql);
	}
	
	
	@SuppressWarnings("unchecked")
	public <T> Integer getEntityCount(T entity, Object... param) throws Exception {
		String tableName = (String) NameUtils.getUnderlineName(entity);
		List<ColumnData> columnDatas = jwebReadTable.getColumnDatas(tableName,ResourceBundle.getBundle("jweb/jweb_database").getString("database_name"),jdbcTemplate,getDBType().toString());
		Map<String, Object> paramMap = CodeUtil.getCountSql(tableName, columnDatas,param);
		String sql = (String) paramMap.get("sql");
		columnDatas = (List<ColumnData>) paramMap.get("colums");
		LogUtil.info("sql语句:"+sql);
		List<Object> list = Lists.newLinkedList();
		for (ColumnData c : columnDatas) {
			try {
				list.add(ClassReflectUtil.getFieldValue(entity, c.getColumnName()));
			} catch (Exception e) {
				LogUtil.error("ClassReflectUtil反射java实体的属性值失败", e);
			}
		}
		return jdbcTemplate.queryForInt(sql, list.toArray());
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public <T> T getEntity(Class<T> entity,String methord,Serializable id) {
		String className = ClassReflectUtil.getEntityName(entity);
		String sql = JdbcSqlUtil.getVal(className,methord, "getEntity");
		String par = JdbcSqlUtil.getPar(className,methord, "getEntity");
		String exclude = JdbcSqlUtil.getExc(className,methord, "getEntity");
		String alias = JdbcSqlUtil.getAlia(className,methord, "getEntity");
		List<Object> list = Lists.newLinkedList();
		list.add(id);
		for (Iterator<Object> iterator = list.iterator(); iterator.hasNext();) {
			par +=","+ iterator.next();
			
		}
		sql = (String) CRUDUtil.select(entity,alias, sql,exclude);
		LogUtil.info("sql语句:"+sql+" - param参数:"+par);
		
		return (T) jdbcTemplate.queryForObject(sql,new BeanPropertyRowMapper(entity),list.toArray());
	}
	
	
	/**
	 * 获取数据库类型
	 */
	public <T> String getDBType() throws SQLException {
		return jdbcTemplate.getDataSource().getConnection().getMetaData().getDatabaseProductName();
	}
	
	
	public <T> boolean save(T entity) throws SQLException {
		List<T> entitys = Lists.newArrayList();
		entitys.add(entity);
		if(entitys.size() == batchSave(entitys).length){
			return true;
		}
		return false;
	}
	
	public <T> boolean update(T entity) throws Exception {
		List<T> entitys = Lists.newArrayList();
		entitys.add(entity);
		if(entitys.size() == batchUpdate(entitys).length){
			return true;
		}
		return false;
	}
	
	
	public Integer executeSql(String sql, List<Object> param) {
		return jdbcTemplate.queryForInt(sql, param.toArray());
	}
	
	public List<Map<String, Object>> findForJdbc(String sql, int page, int rows) {
		return null;
	}
	
	public <T> List<T> findObjForJdbc(String sql, int page, int rows, Class<T> clazz) {
		return null;
	}
	
	public List<Map<String, Object>> findForJdbcParam(String sql, int page,
			int rows, Object... objs) {
		return null;
	}
	
	/**
	 * 
	 * @param res
	 * @param entity
	 * @return
	 */
	
	@SuppressWarnings("unchecked")
	private <T> T BeanPropertyRowMapper(List<Map<String, Object>> res,Class<T> entity){
		Object ob=null;
		if(null != res && res.size()>0){
			Map<String, Object> map = res.get(0);
			Set<String> keySet = map.keySet();
			for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext();) {
				String filedname = (String) iterator.next();
				try {
					ob = ClassReflectUtil.setFieldValue(entity, filedname, map.get(filedname)!=null?map.get(filedname).toString():null);
				} catch (Exception e) {
					LogUtil.error("Map对象转换为java实体失败", e);
				}
			}
		}
		return (T) ob;
		
	}
	
	@SuppressWarnings("unchecked")
	private <T> T BeanPropertyRowMapper(List<Map<String, Object>> res,T entity){
		Object ob=null;
		if(null != res && res.size()>0){
			Map<String, Object> map = res.get(0);
			Set<String> keySet = map.keySet();
			for (Iterator<String> iterator = keySet.iterator(); iterator.hasNext();) {
				String filedname = (String) iterator.next();
				try {
					ob = ClassReflectUtil.setFieldValue(entity, filedname, map.get(filedname)!=null?map.get(filedname):null);
				} catch (Exception e) {
					LogUtil.error("Map对象转换为java实体失败", e);
				}
			}
		}
		return (T) ob;
		
	}
}

package org.takinframework.core.common.dao.impl;

import org.takinframework.core.common.dao.CommonDao;

//import java.io.Serializable;
//import java.sql.PreparedStatement;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.jdbc.core.BeanPropertyRowMapper;
//import org.springframework.jdbc.core.JdbcTemplate;
//import org.springframework.jdbc.core.PreparedStatementSetter;
//import org.takinframework.core.annotation.JwebEntityTitle;
//import org.takinframework.core.common.dao.CommonDao;
//import org.takinframework.core.common.model.common.DBTable;
//
///**
// * jdbcTemplate业务实现基类
// * @author twg
// *
// * @param <T>
// */
public abstract class CommonDaoImpl<T> implements CommonDao<T> {
//	@Autowired
//	protected JdbcTemplate jdbcTemplate;
//	/**
//	 * 查询接口
//	 */
//	public List<T> find(String sql, Object[] parameters, Class<T> cl) {
//		List<T> resultList = null;
//		if (parameters != null && parameters.length > 0){
//			resultList = jdbcTemplate.query(sql, parameters, new BeanPropertyRowMapper<T>(cl));
//		}else {
//			resultList = jdbcTemplate.query(sql, new BeanPropertyRowMapper<T>(cl));
//		}
//		return resultList;
//	}
//	/**
//	 * 添加，更新，删除接口
//	 */
//	public int addOrUpdateOrDelete(String sql, final Object[] parameters, Class<T> cl) {
//		int num = 0;
//		if (parameters == null || parameters.length == 0){
//			num = jdbcTemplate.update(sql);
//		}else {
//			num = jdbcTemplate.update(sql, new PreparedStatementSetter() {
//				public void setValues(PreparedStatement ps)
//						throws SQLException {
//					for (int i = 0; i < parameters.length; i++){
//						ps.setObject(i + 1, parameters[i]);
//					}
//				}
//			});
//		}
//		return num;
//	}
//	
//	/**
//	 * 获取所有数据表
//	 * @return
//	 */
//	public List<DBTable> getAllDbTableName() {
//		List<DBTable> resultList = new ArrayList<DBTable>();
//		SessionFactory factory = getSession().getSessionFactory();
//		Map<String, ClassMetadata> metaMap = factory.getAllClassMetadata();
//		for (String key : (Set<String>) metaMap.keySet()) {
//			DBTable dbTable = new DBTable();
//			AbstractEntityPersister classMetadata = (AbstractEntityPersister) metaMap
//					.get(key);
//			dbTable.setTableName(classMetadata.getTableName());
//			dbTable.setEntityName(classMetadata.getEntityName());
//			Class<?> c;
//			try {
//				c = Class.forName(key);
//				JwebEntityTitle t = c.getAnnotation(JwebEntityTitle.class);
//				dbTable.setTableTitle(t != null ? t.name() : "");
//			} catch (ClassNotFoundException e) {
//				e.printStackTrace();
//			}
//			resultList.add(dbTable);
//		}
//		return resultList;
//	}
//	public Integer getAllDbTableSize() {
//		return null;
//	}
//	public <T> Serializable save(T entity) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	public <T> void batchSave(List<T> entitys) {
//		// TODO Auto-generated method stub
//		
//	}
//	public <T> void saveOrUpdate(T entity) {
//		// TODO Auto-generated method stub
//		
//	}
//	public <T> void delete(T entitie) {
//		// TODO Auto-generated method stub
//		
//	}
//	public <T> T get(Class<T> entityName, Serializable id) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	public <T> T findUniqueByProperty(Class<T> entityClass,
//			String propertyName, Object value) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	public <T> List<T> findByProperty(Class<T> entityClass,
//			String propertyName, Object value) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	public <T> List<T> loadAll(Class<T> entityClass) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	public <T> T getEntity(Class entityName, Serializable id) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	public <T> void deleteEntityById(Class entityName, Serializable id) {
//		// TODO Auto-generated method stub
//		
//	}
//	public <T> void deleteAllEntitie(Collection<T> entities) {
//		// TODO Auto-generated method stub
//		
//	}
//	public <T> void updateEntitie(T pojo) {
//		// TODO Auto-generated method stub
//		
//	}
//	public <T> void updateEntityById(Class entityName, Serializable id) {
//		// TODO Auto-generated method stub
//		
//	}
//	public <T> List<T> findByQueryString(String hql) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	public <T> T singleResult(String hql) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	public int updateBySqlString(String sql) {
//		// TODO Auto-generated method stub
//		return 0;
//	}
//	public <T> List<T> findListbySql(String query) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	public <T> List<T> findByPropertyisOrder(Class<T> entityClass,
//			String propertyName, Object value, boolean isAsc) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	public Session getSession() {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	public List findByExample(String entityName, Object exampleEntity) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	public Map<Object, Object> getHashMapbyQuery(String query) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	public Integer executeSql(String sql, List<Object> param) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	public Integer executeSql(String sql, Object... param) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	public Integer executeSql(String sql, Map<String, Object> param) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	public Object executeSqlReturnKey(String sql, Map<String, Object> param) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	public List<Map<String, Object>> findForJdbc(String sql, Object... objs) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	public Map<String, Object> findOneForJdbc(String sql, Object... objs) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	public List<Map<String, Object>> findForJdbc(String sql, int page, int rows) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	public <T> List<T> findObjForJdbc(String sql, int page, int rows,
//			Class<T> clazz) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	public List<Map<String, Object>> findForJdbcParam(String sql, int page,
//			int rows, Object... objs) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	public Long getCountForJdbc(String sql) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	public Long getCountForJdbcParam(String sql, Object[] objs) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	public <T> List<T> findHql(String hql, Object... param) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	public Integer executeHql(String hql) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	public <T> List<T> pageList(DetachedCriteria dc, int firstResult,
//			int maxResult) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	public <T> List<T> findByDetached(DetachedCriteria dc) {
//		// TODO Auto-generated method stub
//		return null;
//	}
//	
//	
}

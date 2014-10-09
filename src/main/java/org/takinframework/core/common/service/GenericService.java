package org.takinframework.core.common.service;

import java.io.Serializable;

import org.takinframework.core.common.dao.impl.JdbcDaoImpl;

/**
 * 基础实现
 * 
 * @author twg
 * 
 */
public abstract class GenericService<T,PK extends Serializable> extends JdbcDaoImpl {
	

//	private BaseMapper<T> mapper;
//
//	public BaseMapper<T> getMapper() {
//		return mapper;
//	}
//
//	public void add(T entity) {
//		getMapper().add(entity);
//	}
//
//	public void delById(PK p) {
//		getMapper().delById(p);
//	}
//
//	public void delByIds(PK[] ids) {
//		if (BeanUtils.isEmpty(ids))
//			return;
//		for (PK p : ids)
//			delById(p);
//	}
//
//	public void update(T entity) {
//		getMapper().update(entity);
//	}
//
//	@SuppressWarnings("unchecked")
//	public T getById(PK id) {
//		return (T) getMapper().getById(id);
//	}
//
//	public List<T> getList(String statatementName, PageBean pb) {
//		List<T> list = getMapper().getList(statatementName, pb);
//		return list;
//	}
//
//	public List<T> getAll() {
//		return getMapper().getAll();
//	}
//
//	public List<T> getAll(QueryFilter queryFilter) {
//		return getMapper().getAll(queryFilter);
//	}
//
//	/**
//	 * 主键查询
//	 */
//	public T selectByPrimaryKey(PK id) throws Exception {
//		return getMapper().selectByPrimaryKey(id);
//	}
//
//	/**
//	 * 主键删除
//	 */
//	public Integer deleteByPrimaryKey(PK id) throws Exception {
//		return getMapper().deleteByPrimaryKey(id);
//	}
//
//	/**
//	 * 插入
//	 */
//	public void insert(T t) throws Exception {
//		ClassReflectUtil.setIdKeyValue(t, "id", UUID.randomUUID().toString());
//		getMapper().insert(t);
//	}
//
//	/**
//	 * 实体删除
//	 */
//	public void delete(T entity) throws Exception {
//		getMapper().delete(entity);
//	}
//
//	/**
//	 * SQL查询
//	 */
//	public List<T> selectBySql(String sql) throws Exception {
//		return getMapper().selectBySql(sql);
//	}
//
//	/**
//	 * SQL更新
//	 */
//	public Integer updateBySql(String sql) throws Exception {
//		return getMapper().updateBySql(sql);
//	}
//
//	/**
//	 * SQL删除
//	 */
//	public Integer deleteBySql(String sql) throws Exception {
//		return getMapper().deleteBySql(sql);
//	}
//
//	/**
//	 * SQL增加
//	 */
//	public Integer insertBySql(String sql) throws Exception {
//		return getMapper().insertBySql(sql);
//	}
//
//	/**
//	 * 查询总行数
//	 */
//	public Integer queryByCount(T model) throws Exception {
//		return getMapper().queryByCount(model);
//	}
//
//	/**
//	 * 模型分页
//	 */
//	public List<T> queryByList(T model) throws Exception {
//		if (((BaseEntity) model).getPageUtil().getPaging()) {
//			try {
//				((BaseEntity) model).getPageUtil().setRowCount(
//						queryByCount(model));
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return getMapper().queryByList(model);
//	}
//
//	/**
//	 * 查询分页数
//	 */
//	public Integer selectByMapPagingCount(Map<?, ?> map) throws SQLException {
//		return getMapper().selectByMapPagingCount(map);
//	}
//
//	/**
//	 * 查询分页
//	 */
//	public List<T> selectByMapPaging(Map<?, ?> map) throws SQLException {
//		int rowCount = getMapper().selectByMapPagingCount(map);
//		PageUtil pageUtil = (PageUtil) map.get("pageUtil");
//		if (null == pageUtil) {
//			System.out.println("错误!!!  pageUtil 参数为NULL");
//			return null;
//		}
//		pageUtil.setRowCount(rowCount);
//		return getMapper().selectByMapPaging(map);
//	}
//
//	/**
//	 * 实体查询
//	 */
//	public List<T> selectByEntiry(T entity) throws Exception {
//		return getMapper().selectByEntiry(entity);
//	}
//
//	/**
//	 * 查询分页数
//	 */
//	public Integer selectByMapCount(Map<?, ?> map) throws Exception {
//		return getMapper().selectByMapCount(map);
//	}
//
//	/**
//	 * Map查询
//	 */
//	public List<T> selectByMap(Map<?, ?> map) throws Exception {
//		return getMapper().selectByMap(map);
//	}
//
//	/**
//	 * 图表
//	 */
//	public List<Map<?, ?>> charts(Map<?, ?> map) throws SQLException {
//		return getMapper().charts(map);
//	}
//
//	/**
//	 * 递归查询
//	 */
//	public List<T> selectByChild(T model) throws SQLException {
//		if (((BaseEntity) model).getPageUtil().getPaging()) {
//			try {
//				((BaseEntity) model).getPageUtil().setRowCount(
//						queryByCount(model));
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		return getMapper().selectByChild(model);
//	}
//
//	/**
//	 * 主键删除
//	 */
//	public Integer deleteByPrimaryKeys(Object... keys) throws Exception {
//		int i = 0;
//		for (Object key : keys) {
//			i += getMapper().deleteByPrimaryKey(key);
//		}
//		return i;
//	}
//
//	public T queryById(T entity) throws Exception {
//		return getMapper().queryById(entity);
//	}
//
//
//	public void updateBySelective(T entity) throws Exception {
//		getMapper().updateBySelective(entity);
//	}

}

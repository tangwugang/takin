package org.takinframework.core.common.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.annotations.Param;
import org.takinframework.core.common.exception.BusinessException;

/**
 * 定义在这里是公用的 由Mapper配置文件实现
 * mybatis方式
 * @author twg
 * @param <T>
 */
public abstract interface BaseMapper<T,PK extends Serializable> {
//
//	public abstract void add(T paramE);
//
//	public abstract int delById(PK paramPK);
//
//	public abstract int update(T paramE);
//
//	public abstract Object getById(PK paramPK);
//
//	public abstract List<T> getList(String paramString, Object paramObject);
//
//	public abstract List<T> getList(String paramString, Object paramObject,
//			PageBean<T> paramPageBean);
//
//	public abstract List<T> getAll();
//
//	public abstract List<T> getAll(QueryFilter paramQueryFilter);
//
//	public abstract T getUnique(String paramString, Object paramObject);
	
	public T checkEntityExits(T entity);

	/***************** CRUD操作 ********************/
	public void insert(T entity) throws BusinessException;
	public T selectById(PK id) throws BusinessException;
	public void deleteById(PK id)throws BusinessException;
	public void delete(T entity)throws BusinessException;
	/********************sql************************/
	public List<T> selectBySql(@Param(value = "sql") String sql)throws BusinessException;
	public void updateBySql(@Param(value = "sql") String sql)throws BusinessException;
	public void deleteBySql(@Param(value = "sql") String sql)throws BusinessException;
	public void insertBySql(@Param(value = "sql") String sql)throws BusinessException;


	/*********************** 按更新条件更新 **************************/
	public void updateBySelective(T entity) throws BusinessException;

	/*********************** 分页查询操作 ************************/
	/************************ 总数 **************************/
	public Integer queryByCount(T entity) throws BusinessException;

	public List<T> queryByList(T entity) throws BusinessException;
	public List<T> queryAll();

	public Integer queryByMapCount(Map<?, ?> map)throws BusinessException;

	public List<T> queryByMapList(Map<?, ?> map)throws BusinessException;

	/*********************** 查询不分页 *************************/
	public Integer selectByCount(T entity) throws BusinessException;
	public List<T> selectByList(T entity) throws BusinessException;
	public List<T> selectAll();
	public Integer selectByMapCount(Map<?, ?> map) throws BusinessException;

	public List<T> selectByMapList(Map<?, ?> map) throws BusinessException;

	/************************** 图表 **************************/
	public List<Map<?, ?>> charts(Map<?, ?> map) throws BusinessException;

	/************************** 递归查询 ***********************/
	public List<T> selectByChild(T entity) throws BusinessException;
	
	/*************************webservice 调用******************************/
	public T getById(PK id) throws BusinessException;

}

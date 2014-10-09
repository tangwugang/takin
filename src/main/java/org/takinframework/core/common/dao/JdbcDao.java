package org.takinframework.core.common.dao;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;
import org.takinframework.core.common.exception.BusinessException;
import org.takinframework.core.util.Page;
import org.takinframework.core.util.PageUtil;

public interface JdbcDao {
	
	/**
	 * 获取数据库类型
	 * @return
	 * @throws SQLException 
	 */
	public <T> String getDBType() throws SQLException;
	
	/**
	 * 判断实体是否存在
	 * @param entity 实体
	 * @param methord 对应sqlconfig.xml中的methord
	 * @return
	 */
	public <T> T checkEntityExits(Class<T> entity,String methord)throws SQLException;
	/**
	 * 判断实体是否存在
	 * @param entity 实体
	 * @return
	 * @throws SQLException
	 * @throws Exception 
	 */
	public <T> T checkEntityExits(T entity)throws Exception;
	/**
	 * 判断实体是否存在
	 * @param entity 实体
	 * @param param 属性名
	 * @return
	 * @throws SQLException
	 */
	public <T> T checkEntityExits(T entity,Object...param)throws Exception;
	/**
	 * 根据id获取entity
	 * @param entity
	 * @return
	 * @throws BusinessException
	 */
	public <T> T getEntity(Class<T> entity,String methord,Serializable id)throws SQLException;
	
	/**
	 * 根据id获取entity数
	 * @param entity
	 * @return
	 * @throws BusinessException
	 */
	public <T> Integer getEntityCount(Class<T> entity,String methord,Serializable id);
	/**
	 * 获取entity数
	 * @param entity 实体
	 * @return
	 * @throws Exception 
	 */
	public <T> Integer getEntityCount(T entity) throws Exception;
	/**
	 * 根据属性名获取entity数
	 * @param entity 实体
	 * @param param 属性名
	 * @return
	 */
	public <T> Integer getEntityCount(T entity,Object...param)throws Exception;
	
	/**
	 * 根据id获取entity数
	 * @param entity 实体
	 * @return
	 * @throws Exception 
	 */
	public <T> Integer getEntityCountById(T entity) throws Exception;
	/**
	 * 根据id获取entity列表
	 * @param entity
	 * @param id
	 * @return
	 */
	public <T> List<T> findList(Class<T> entity,String methord,Serializable id);
	/**
	 * 根据参数获取entity列表
	 * @param entity
	 * @param param
	 * @return
	 */
	public <T> List<T> findList(Class<T> entity,String methord,Object... param);
	/**
	 * 根据id获取entity列表
	 * @param entity
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findList(T entity)throws Exception;
	/**
	 * 根据属性名获取entity列表
	 * @param entity
	 * @param param
	 * @return
	 */
	public <T> List<T> findList(T entity,Object... param)throws Exception;
	/**
	 * 根据属性名获取entity列表
	 * @param entity
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public <T> List<T> findList(T entity,Map<String, Object> param)throws Exception;
	
	/**
	 * 根据属性名获取entity列表
	 * @param entity
	 * @param param
	 * @return
	 */
	public <T> List<T> findListByPage(T entity,Object... param)throws Exception;
	/**
	 * 根据属性名获取entity列表,带分页功能
	 * @param page
	 * @param entity
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public <T> Page<T> findListByPage(Page<T> page,T entity,Object... param)throws Exception;
	
	
	/**
	 * 根据自定义属性,获取entity列表,带分页功能
	 * @param page
	 * @param entity
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public <T> Page<T> findListByPage(Page<T> page,T entity,Map<String, Object> param)throws Exception;
	
	
	/**
	 * 根据自定义属性,获取entity列表,带分页功能
	 * @param page
	 * @param entity
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public <T> PageUtil<T> findListByPage(PageUtil<T> page,T entity,Map<String, Object> param)throws Exception;
	
	
	/**
	 * 查询实体数据列表
	 * @param entity 实体
	 * @param methord sqlconfig.xml中的methord
	 * @return
	 */
	public <T> List<T> findAll(Class<T> entity,String methord);
	
	/**
	 * 查询实体数据列表
	 * @param entity 实体
	 * @return
	 * @throws SQLException 
	 */
	public <T> List<T> findAll(Class<T> entity) throws SQLException;
	
	/**
	 * 查询实体数据列表,带分页功能
	 * @param entity 实体
	 * @return
	 * @throws SQLException 
	 * @throws Exception 
	 */
	public <T> List<T> findAllByPage(T entity) throws Exception;
	
	/**
	 * 查询实体数据列表,带分页功能
	 * @param page 分页组件
	 * @param entity 实体
	 * @return
	 * @throws Exception
	 */
	public <T> Page<T> findAllByPage(Page<T> page,T entity) throws Exception;
	
	/**
	 * 查询实体数据列表,带分页功能
	 * @param page 分页组件
	 * @param entity 实体
	 * @return
	 * @throws Exception
	 */
	public <T> PageUtil<T> findAllByPage(PageUtil<T> page,T entity) throws Exception;
	
	/**
	 * 执行SQL
	 * @return 
	 */
	public Integer executeSql(String sql, Object... param);
	
	
	/**
	 * 删除实体,按id
	 * @param entity
	 */
	public <T> void delete(T entity);
	
	/**
	 * 删除实体
	 * @param entity
	 * @param parameters 参数列表
	 */
	public <T> void delete(T entity,Object...parameters);
	/**
	 * 删除实体
	 * @param entity
	 * @param parameters
	 */
	public <T> boolean delete(Class<T> entity,String methord,Object...parameters);
	
	/**
	 * 保存实体
	 * @param entity
	 * @return 
	 * @throws SQLException 
	 */
	public <T> boolean save(T entity) throws SQLException;
	
	
	/**
	 * 查询接口
	 * @param sql
	 * @param parameters
	 * @param cl
	 * @return
	 */
	public <T> List<T> find(String sql, Object[] parameters, Class<T> cl);
	/**
	 * 添加，更新，删除接口
	 * @param sql
	 * @param parameters
	 * @param cl
	 * @return
	 */
	public <T> int addOrUpdateOrDelete(String sql,Object[] parameters, Class<T> cl);
	
	/**
	 * 批量保存实体集合
	 * @param entitys
	 * @return 
	 * @throws SQLException
	 */
	public <T> int[] batchSave(List<T> entitys) throws SQLException;
	/**
	 * 批量更新实体集合
	 * @param entitys
	 * @return
	 * @throws SQLException
	 */
	public <T> int[] batchUpdate(List<T> entitys) throws Exception;
	
	/**
	 * 删除实体集合
	 * 
	 * @param <T>
	 * @param entities
	 */
	public <T> void delete(Collection<T> entities);

	/**
	 * 更新指定的实体
	 * 
	 * @param <T>
	 * @param entity
	 * @throws SQLException 
	 * @throws Exception 
	 */
	public <T> boolean update(T entity) throws Exception;

	/**
	 * 执行SQL
	 */
	public Integer executeSql(String sql, List<Object> param);
	/**
	 * 通过JDBC查找对象集合
	 * @param sql
	 * @return
	 */
	public List<Map<String, Object>> findForJdbc(String sql);

	/**
	 * 通过JDBC查找对象集合,带分页 使用指定的检索标准检索数据并分页返回数据
	 */
	public List<Map<String, Object>> findForJdbc(String sql, int page, int rows);

	/**
	 * 通过JDBC查找对象集合,带分页 使用指定的检索标准检索数据并分页返回数据
	 */
	public <T> List<T> findObjForJdbc(String sql, int page, int rows,
			Class<T> clazz);

	/**
	 * 使用指定的检索标准检索数据并分页返回数据-采用预处理方式
	 * 
	 * @param criteria
	 * @param firstResult
	 * @param maxResults
	 * @return
	 * @throws DataAccessException
	 */
	public List<Map<String, Object>> findForJdbcParam(String sql, int page,
			int rows, Object... objs);


}

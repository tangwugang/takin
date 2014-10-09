package org.takinframework.core.common.service;

import java.io.Serializable;
import java.net.SocketException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.takinframework.core.common.exception.BusinessException;
import org.takinframework.core.util.Page;
import org.takinframework.core.util.PageUtil;


public interface BaseService {
	
	/**
	 * 判断实体是否存在
	 * @param entity 实体
	 * @param methord 对应sqlconfig.xml中的methord
	 * @return
	 * @throws SQLException 
	 */
	public <T> T checkEntityExits(Class<T> entity,String methord) throws SQLException;
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
	 * @param param 参数
	 * @return
	 * @throws SQLException
	 */
	public <T> T checkEntityExits(T entity,Object...param)throws Exception;
	/**
	 * 根据id获取entity
	 * @param entity
	 * @return
	 * @throws SQLException 
	 * @throws BusinessException
	 */
	public <T> T getEntity(Class<T> entity,String methord,Serializable id) throws SQLException;
	
	/**
	 * 根据id获取entity数
	 * @param entity
	 * @return
	 * @throws SQLException 
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
	 * @param entity
	 * @param param
	 * @return
	 * @throws Exception
	 */
	public <T> Integer getEntityCountById(T entity)throws Exception;
	
	/**
	 * 根据自定义SQL查询
	 */
	public <T> List<Map<String, Object>> find(String sql);
	
	
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
	
	public <T> PageUtil<T> findListByPage(PageUtil<T> page,T entity,Map<String, Object> param)throws Exception;
	
	/**
	 * 查询实体数据列表
	 * 
	 * @param <T>
	 * @param entity
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
	 */
	public void executeSql(String sql, Object... param);
	
	
	/**
	 * 删除实体,按id
	 * @param <T>
	 * 
	 * @param <T>
	 * @param entitie
	 * @throws SQLException 
	 */
	public <T> void delete(T entity);
	/**
	 * 删除实体
	 * @param entity
	 * @param params 参数
	 */
	public <T> void delete(T entity,Object...params );
	
	/**
	 * 删除实体
	 * @param entity
	 * @param parameters
	 */
	public <T> boolean delete(Class<T> entity,String methord,Object...parameters);
	
	/**
	 * 删除实体集合
	 * @param entitie
	 * @throws SQLException 
	 */
	public <T> void deleteAllEntitie(Collection<T> entities);
	/**
	 *  保存实体
	 * @param entity
	 * @return 
	 * @throws SQLException 
	 */
	public <T> boolean save(T entity) throws SQLException;
	/**
	 * 批量保存实体
	 * @param entitys
	 * @throws SQLException
	 */
	public <T> void batchSave(List<T> entitys) throws SQLException;
	
	
	/**
	 * 日志添加
	 * @param LogContent 内容
	 * @param operatetype 类型
	 * @param loglevel 级别
	 * @throws SocketException 
	 */
	public void addLog(String LogContent,Short operatetype, Short loglevel);
	
	/**
	 * 日志添加
	 * @param LogContent 内容
	 * @param operatetype 类型
	 * @param loglevel 级别
	 * @param e  异常信息
	 */
	public void addLog(String LogContent,Short operatetype, Short loglevel,Exception e);
	
	
	/**
	 * 根据类型分组编码和名称获取TypeGroup,如果为空则创建一个
	 * @param typecode
	 * @param typename
	 * @return
	 */
	public <T> T getTypeGroup(String typegroupcode,String typgroupename);
	/**
	 * 根据用户ID 和 菜单Id 获取 具有操作权限的按钮Codes
	 * @param roleId
	 * @param functionId
	 * @return
	 */
	public  <T> Set<T> getOperationCodesByUserIdAndFunctionId(String userId,String functionId);
	/**
	 * 根据角色ID 和 菜单Id 获取 具有操作权限的按钮Codes
	 * @param <T>
	 * @param roleId
	 * @param functionId
	 * @return
	 */
	public  <T> Set<T> getOperationCodesByRoleIdAndFunctionId(String roleId,String functionId);
	/**
	 * 根据编码获取字典组
	 * 
	 * @param typegroupCode
	 * @return
	 */
	public <T> T getTypeGroupByCode(Class<T> entity);
	/**
	 * 对数据字典进行缓存
	 */
	public void initAllTypeGroups();
	
	/**
	 * 刷新字典缓存
	 * @param <T>
	 * @param type
	 * @return 
	 */
	public  <T> void refleshTypesCach(Class<T> entity);
	/**
	 * 刷新字典分组缓存
	 */
	public void refleshTypeGroupCach();


	/**
	 * 更新
	 * @param entity
	 * @throws Exception
	 */
	public <T> boolean update(T entity) throws Exception;
	/**
	 * 获取数据库类型MySQL、Oracle等
	 * @return
	 * @throws SQLException
	 */
	public <T> String getDBType() throws SQLException;
	
}

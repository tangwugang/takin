package org.takinframework.codegenerator.service;

/**
 * 代码生成service
 * @author twg
 *
 */
public interface CodeGenerateService {
	/**
	 * 生成代码(生成全部)
	 * @param bussiPackage 包名(org.takinframework.web)
	 * @param tableName 数据库表名(sys_user)
	 * @param entityPackage 模块名(system)
	 * @param ftl_description 实体描述(系统用户信息表)
	 */
	public void generate(String bussiPackage, String tableName, String entityPackage, String ftl_description)throws Exception;
	/**
	 * 生成代码(模板可选)
	 * @param bussiPackage 包名(org.takinframework.web)
	 * @param tableName 数据库表名(sys_user)
	 * @param entityPackage 模块名(system)
	 * @param ftl_description 实体描述(系统用户信息表)
	 * @param templates {"entityTemplate","mapperTemplate",
	 * "TempMapper","serviceITemplate","serviceImplTemplate","controllerTemplate"}
	 */
	public void generate(String bussiPackage, String tableName, String entityPackage, String ftl_description,String[] templates)throws Exception;
	
	/**
	 * 获取数据库类型MySQL、ORACLE
	 * @return
	 * @throws Exception
	 */
	public String dbType()throws Exception;
	/**
	 * 创建数据库表测试用的
	 * @throws Exception
	 */
	public void createTable()throws Exception;
}

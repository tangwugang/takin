package org.takinframework.core.common.persistence.interceptor;

import java.io.Serializable;
import java.util.Properties;

import org.apache.ibatis.plugin.Interceptor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.takinframework.core.common.hibernate.dialect.DB2Dialect;
import org.takinframework.core.common.hibernate.dialect.DerbyDialect;
import org.takinframework.core.common.hibernate.dialect.Dialect;
import org.takinframework.core.common.hibernate.dialect.H2Dialect;
import org.takinframework.core.common.hibernate.dialect.HSQLDialect;
import org.takinframework.core.common.hibernate.dialect.MySQLDialect;
import org.takinframework.core.common.hibernate.dialect.OracleDialect;
import org.takinframework.core.common.hibernate.dialect.PostgreSQLDialect;
import org.takinframework.core.common.hibernate.dialect.SQLServer2005Dialect;
import org.takinframework.core.common.hibernate.dialect.SybaseDialect;
import org.takinframework.core.util.ClassReflectUtil;
import org.takinframework.core.util.Page;
import org.takinframework.core.util.ResourceUtil;
/**
 * MyBatis 分页拦截器基类
 * @author twg
 *
 */
public abstract class BaseInterceptor implements Interceptor,Serializable {
	private static final long serialVersionUID = 1L;
	protected Logger logger = LoggerFactory.getLogger(BaseInterceptor.class);
	protected Dialect DIALECT;
	protected static final String PAGE = "page";
    protected static final String DELEGATE = "delegate";
    protected static final String MAPPED_STATEMENT = "mappedStatement";
    
    /**
     * 对参数进行转换和检查
     * @param parameterObject 参数对象
     * @param page            分页对象
     * @return 分页对象
     * @throws NoSuchFieldException 无法找到参数
     */
    @SuppressWarnings("unchecked")
	protected static Page<Object> convertParameter(Object parameterObject, Page<Object> page) {
    	try{
            if (parameterObject instanceof Page) {
                return (Page<Object>) parameterObject;
            } else if(parameterObject instanceof String ||
            		parameterObject instanceof Integer ||
            		parameterObject instanceof Double ||
            		parameterObject instanceof Float){
            	return null;
            }else {
                return (Page<Object>)ClassReflectUtil.getFieldValue(parameterObject, PAGE);
            }
    	}catch (Exception e) {
			return null;
		}
    }

    /**
     * 设置属性，支持自定义方言类和制定数据库的方式
     * <code>dialectClass</code>,自定义方言类。可以不配置这项
     * <ode>dbms</ode> 数据库类型，插件支持的数据库
     * <code>sqlPattern</code> 需要拦截的SQL ID
     * @param p 属性
     */
    protected void initProperties(Properties p) {
    	Dialect dialect = null;
        String dbType = ResourceUtil.getConfig("jdbc.dbType");
        if ("db2".equals(dbType)){
        	dialect = new DB2Dialect();
        }else if("derby".equals(dbType)){
        	dialect = new DerbyDialect();
        }else if("h2".equals(dbType)){
        	dialect = new H2Dialect();
        }else if("hsql".equals(dbType)){
        	dialect = new HSQLDialect();
        }else if("mysql".equals(dbType)){
        	dialect = new MySQLDialect();
        }else if("oracle".equals(dbType)){
        	dialect = new OracleDialect();
        }else if("postgre".equals(dbType)){
        	dialect = new PostgreSQLDialect();
        }else if("mssql".equals(dbType) || "sqlserver".equals(dbType)){
        	dialect = new SQLServer2005Dialect();
        }else if("sybase".equals(dbType)){
        	dialect = new SybaseDialect();
        }
        if (dialect == null) {
            throw new RuntimeException("mybatis dialect error.");
        }
        DIALECT = dialect;
//        _SQL_PATTERN = p.getProperty("sqlPattern");
//        _SQL_PATTERN = Global.getConfig("mybatis.pagePattern");
//        if (StringUtils.isEmpty(_SQL_PATTERN)) {
//            throw new RuntimeException("sqlPattern property is not found!");
//        }
    }

}

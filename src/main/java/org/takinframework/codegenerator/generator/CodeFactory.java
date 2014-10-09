package org.takinframework.codegenerator.generator;

import org.springframework.jdbc.core.JdbcTemplate;
/**
 * 代码生成工具工厂获取源
 * @author twg
 *
 */
public abstract class CodeFactory {
	protected static JdbcTemplate jdbcTemplate;
	protected static String dbType;
	
	public static JdbcTemplate getJdbcTemplate() {
		return jdbcTemplate;
	}
	public static void setJdbcTemplate(JdbcTemplate jdbcTemplate) {
		CodeFactory.jdbcTemplate = jdbcTemplate;
	}
	public static String getDbType() {
		return dbType;
	}
	public static void setDbType(String dbType) {
		CodeFactory.dbType = dbType;
	}
	
	
}

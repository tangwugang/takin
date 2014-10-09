package org.takinframework.core.common.service.impl;

import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.takinframework.core.common.dao.JdbcDao;

public abstract class BaseServiceImpl<T,PK extends Serializable > {
	@Autowired
	public JdbcDao jdbcDao;
	@Autowired
	public JdbcTemplate jdbcTemplate;
	
}

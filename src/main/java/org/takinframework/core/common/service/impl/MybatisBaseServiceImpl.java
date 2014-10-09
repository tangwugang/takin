package org.takinframework.core.common.service.impl;



import java.io.Serializable;

import org.springframework.beans.factory.annotation.Autowired;
import org.takinframework.core.common.dao.MybatisBaseDAO;
import org.takinframework.core.common.service.MybatisBaseService;

public class MybatisBaseServiceImpl<T, PK extends Serializable> implements MybatisBaseService<T, String> {
	
	@Autowired
	protected MybatisBaseDAO<T, String> mybatisBaseDAO;

	
}

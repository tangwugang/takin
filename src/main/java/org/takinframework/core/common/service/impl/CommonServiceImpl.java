package org.takinframework.core.common.service.impl;

import java.io.Serializable;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.takinframework.core.common.dao.impl.CommonDaoImpl;
import org.takinframework.core.common.model.common.DBTable;
import org.takinframework.core.common.service.CommonService;

@Service("commonService")
@Transactional
public abstract class CommonServiceImpl<T> extends CommonDaoImpl<T>implements CommonService<T> {

	@SuppressWarnings("rawtypes")
	public List<DBTable> getAllDbTableName() {
		return null;
	}

	public Integer getAllDbTableSize() {
		return null;
	}

	public Serializable save(Object entity) {
		return null;
	}

	public void saveOrUpdate(Object entity) {
		
	}

	public void delete(Object entity) {
		
	}

	@SuppressWarnings("hiding")
	public <T> void batchSave(List<T> entitys) {
		
	}

	@SuppressWarnings("hiding")
	public <T> T get(Class<T> class1, Serializable id) {
		return null;
	}

	@SuppressWarnings("hiding")
	public <T> T getEntity(Class<T> entityName, Serializable id) {
		return null;
	}

	@SuppressWarnings("hiding")
	public <T> T findUniqueByProperty(Class<T> entityClass, String propertyName,
			Object value) {
		return null;
	}

	

//	public List pageList(DetachedCriteria dc, int firstResult, int maxResult) {
//		return null;
//	}
//
//	public List findByDetached(DetachedCriteria dc) {
//		return null;
//	}

}

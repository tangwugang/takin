package org.takinframework.core.common.dao.impl;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import org.apache.ibatis.session.RowBounds;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.support.SqlSessionDaoSupport;
import org.takinframework.core.common.dao.MybatisBaseDAO;
import org.takinframework.core.page.PageBean;

import com.google.common.collect.Maps;
/**
 * sqlSessionTemplate方式保持数据，使用可以继承BaseDaoImpl
 * @author twg
 *
 * @param <T>
 * @param <PK>
 */
public class MybatisBaseDaoImpl<T,PK extends Serializable > extends SqlSessionDaoSupport implements MybatisBaseDAO<T, PK> {
	// mapper.xml中的namespace  
    private String namespace;  
    // sqlmap.xml定义文件中对应的sqlid  
    public static final String SQLID_INSERT = "insert";  
    public static final String SQLID_INSERT_BATCH = "insertBatch";  
    public static final String SQLID_UPDATE = "update";  
    public static final String SQLID_UPDATE_PARAM = "updateParam";  
    public static final String SQLID_UPDATE_BATCH = "updateBatch";  
    public static final String SQLID_DELETE = "delete";  
    public static final String SQLID_DELETE_PARAM = "deleteParam";  
    public static final String SQLID_DELETE_BATCH = "deleteBatch";  
    public static final String SQLID_TRUNCATE = "truncate";  
    public static final String SQLID_SELECT = "select";  
    public static final String SQLID_SELECT_PK = "selectPk";  
    public static final String SQLID_SELECT_PARAM = "selectParam";  
    public static final String SQLID_SELECT_FK = "selectFk";  
    public static final String SQLID_COUNT = "count";  
    public static final String SQLID_COUNT_PARAM = "countParam";  

    //@Resource(name = "sqlSessionTemplate")
    public void setSuperSqlSessionTemplate(SqlSessionTemplate sqlSessionTemplate) {  
        super.setSqlSessionTemplate(sqlSessionTemplate);  
    }  
  
    public String getNamespace() {  
        return namespace;  
    }  
  
    public void setNamespace(String namespace) {  
        this.namespace = namespace;  
    }  
  
    public void insert(T entity) {  
        try {  
            getSqlSession().insert(namespace + "." + SQLID_INSERT,  
                    entity);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    public void update(T entity) {  
        try {  
            getSqlSession().update(namespace + "." + SQLID_UPDATE,  
                    entity);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    public void updateParam(Map param) {  
        try {  
            getSqlSession().update(namespace + "." + SQLID_UPDATE_PARAM,  
                    param);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    public void delete(PK primaryKey) {  
        try {  
            getSqlSession().delete(namespace + "." + SQLID_DELETE,  
                    primaryKey);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    public void deleteParam(Map param) {  
        try {  
            getSqlSession().delete(namespace + "." + SQLID_DELETE_PARAM,  
                    param);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
//    public int truncate() {  
//        int rows = 0;  
//        try {  
//            rows = getSqlSession().delete(namespace + "." + SQLID_TRUNCATE);  
//        } catch (Exception e) {  
//            e.printStackTrace();  
//        }  
//        return rows;  
//    }  
  
    public int count() {  
        int result = 0;  
        try {  
            result = getSqlSession().selectOne(namespace + "." + SQLID_COUNT);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return result;  
    }  
  
    public int count(Object param) {  
        int result = 0;  
        try {  
            result = getSqlSession().selectOne(namespace + "." + SQLID_COUNT_PARAM,param);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
        return result;  
    }  
  
    public T get(PK primaryKey) {  
        try {  
            return getSqlSession().selectOne(namespace + "." + SQLID_SELECT_PK,primaryKey);  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  
  
    public List<T> select() {  
        try {  
            return getSqlSession().selectList(namespace + "." + SQLID_SELECT);  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
          
    }  
  
    public List<T> selectParam(Map param) {  
        try {  
            return getSqlSession().selectList(namespace + "." + SQLID_SELECT_PARAM,param);  
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  
  
    @SuppressWarnings("unchecked")
	public PageBean<T> selectPagination(PageBean<T> pageEntity) {  
        try {  
            int page = pageEntity.getCurrentPage(); //默认为第一页  
            int size = pageEntity.getPageSize(); //默认每页15个  
              
            RowBounds rowBounds = new RowBounds((page-1)*size, size);  
              
            Map<Object, Object> param = pageEntity.getParams();  
            if (param != null) {  
                param.put("orderColumn", pageEntity.getOrderColumn());  
                param.put("orderTurn", pageEntity.getOrderTurn());  
            }else {  
                param = Maps.newConcurrentMap();
                param.put("orderColumn", pageEntity.getOrderColumn());  
                param.put("orderTurn", pageEntity.getOrderTurn());  
            }  
              
            List<T> resultList = getSqlSession().selectList(namespace + "." + SQLID_SELECT_PARAM,param,rowBounds);  
            int totalSize = count(pageEntity.getParams());  
              
            PageBean<T> pagingResult = new PageBean<T>();  
            pagingResult.setCurrentPage(page);  
            pagingResult.setTotalCount(totalSize);  
            pagingResult.setResultList(resultList);  
            return pagingResult;  
              
        } catch (Exception e) {  
            e.printStackTrace();  
            return null;  
        }  
    }  
  
  
    public void insertBatch(List<T> list) {  
        try {  
            getSqlSession().insert(namespace + "." + SQLID_INSERT_BATCH,list);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
   
    public void updateBatch(List<T> list) {  
        try {  
            for (T t : list) {  
                getSqlSession().update(namespace + "." + SQLID_UPDATE, t);  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
  
    public void deleteBatch(List<PK> list) {  
        try {  
            getSqlSession().delete(namespace + "." + SQLID_DELETE_BATCH,list);  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
  
    }  

}

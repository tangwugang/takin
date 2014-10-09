package org.takinframework.core.page;

import java.util.List;
import java.util.Map;

import org.takinframework.core.util.ClassReflectUtil;
import org.takinframework.core.util.Page;
import org.takinframework.core.util.PageUtil;
import org.takinframework.core.util.StringUtil;

import com.google.common.collect.Maps;



public class PageBean<T> {
	private int currentPage = 1;
	private int pageSize = 15;
	private String orderColumn;
	private String orderTurn;
	private int totalCount;
	private List<T> resultList;
	private Map<Object, Object> params = Maps.newConcurrentMap();

	public int getCurrentPage() {
		return currentPage;
	}
	public void setCurrentPage(int currentPage) {
		this.currentPage = currentPage;
	}
	public int getPageSize() {
		return pageSize;
	}
	public void setPageSize(int pageSize) {
		this.pageSize = pageSize;
	}
	public String getOrderColumn() {
		return orderColumn;
	}
	public void setOrderColumn(String orderColumn) {
		this.orderColumn = orderColumn;
	}
	public String getOrderTurn() {
		return orderTurn;
	}
	public void setOrderTurn(String orderTurn) {
		this.orderTurn = orderTurn;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public List<T> getResultList() {
		return resultList;
	}
	public void setResultList(List<T> resultList) {
		this.resultList = resultList;
	}
	public Map<Object, Object> getParams() {
		return params;
	}
	public void setParams(Map<Object, Object> params) {
		this.params = params;
	}


	@SuppressWarnings("rawtypes")
	public static <T> String doPage(String dbType,String sql,T entity,int count){
		try {
			Page page = (Page) ClassReflectUtil.getFieldValue(entity, "page");
			page.setCount(count);
			String order = page.getOrderBy();
			if(StringUtil.isNotEmpty(order)){
				sql+=" order by "+order;
			}
			if ("db2".equals(dbType)){
	        }else if("derby".equals(dbType)){
	        }else if("h2".equals(dbType)){
	        }else if("hsql".equals(dbType)){
	        }else if("mysql".equals(dbType)){
	        	sql+=" limit "+page.getFirstResult()+","+page.getPageSize();
	        }else if("oracle".equals(dbType)){
	        	sql = "select * from (select a.*,rownum rn from ("+sql+") a where rownum<= "+page.getLastResult()+") where rn>"+page.getFirstResult();
	        }else if("postgre".equals(dbType)){
	        }else if("mssql".equals(dbType) || "sqlserver".equals(dbType)){
	        }else if("sybase".equals(dbType)){
	        }
		} catch (Exception e) {
		}
		return sql;
	}
	
	
	public static <T> String doPage(String dbType,String sql,Page<T> page,int count){
		try {
			page.setCount(count);
			String order = page.getOrderBy();
			if(StringUtil.isNotEmpty(order)){
				sql+=" order by "+order;
			}else{
				sql+=" order by createDate";
			}
			if ("db2".equals(dbType)){
	        }else if("derby".equals(dbType)){
	        }else if("h2".equals(dbType)){
	        }else if("hsql".equals(dbType)){
	        }else if("mysql".equals(dbType)){
	        	sql+=" limit "+page.getFirstResult()+","+page.getPageSize();
	        }else if("oracle".equals(dbType)){
	        	sql = "select * from (select a.*,rownum rn from ("+sql+") a where rownum<= "+page.getLastResult()+") where rn>"+page.getFirstResult();
	        }else if("postgre".equals(dbType)){
	        }else if("mssql".equals(dbType) || "sqlserver".equals(dbType)){
	        }else if("sybase".equals(dbType)){
	        }
		} catch (Exception e) {
		}
		return sql;
	}
	
	
	public static <T> String doPage(String dbType,String sql,PageUtil<T> page,int count){
		try {
			page.setCount(count);
			String order = page.getOrderBy();
			if(StringUtil.isNotEmpty(order)){
				sql+=" order by "+order;
			}else{
				sql+=" order by createDate";
			}
			if ("db2".equals(dbType)){
	        }else if("derby".equals(dbType)){
	        }else if("h2".equals(dbType)){
	        }else if("hsql".equals(dbType)){
	        }else if("mysql".equals(dbType)){
	        	sql+=" limit "+page.getFirstResult()+","+page.getPageSize();
	        }else if("oracle".equals(dbType)){
	        	sql = "select * from (select a.*,rownum rn from ("+sql+") a where rownum<= "+page.getLastResult()+") where rn>"+page.getFirstResult();
	        }else if("postgre".equals(dbType)){
	        }else if("mssql".equals(dbType) || "sqlserver".equals(dbType)){
	        }else if("sybase".equals(dbType)){
	        }
		} catch (Exception e) {
		}
		return sql;
	}

}

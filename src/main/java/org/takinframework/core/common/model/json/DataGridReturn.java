package org.takinframework.core.common.model.json;

import java.util.List;

/**
 * 后台向前台返回JSON，用于easyui的datagrid
 * 
 * @author 
 * @param <T>
 * 
 */
public class DataGridReturn<T> {

	public DataGridReturn(Integer total, List<T> rows) {
		this.total = total;
		this.rows = rows;
	}

	private Integer total;// 总记录数
	private List<T> rows;// 每行记录
	private List<T> footer;

	public Integer getTotal() {
		return total;
	}

	public void setTotal(Integer total) {
		this.total = total;
	}

	public List<T> getRows() {
		return rows;
	}

	public void setRows(List<T> rows) {
		this.rows = rows;
	}

	public List<T> getFooter() {
		return footer;
	}

	public void setFooter(List<T> footer) {
		this.footer = footer;
	}

}

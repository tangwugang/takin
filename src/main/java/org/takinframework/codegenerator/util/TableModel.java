package org.takinframework.codegenerator.util;

import java.util.List;
import com.google.common.collect.Lists;

/**
 * 数据库表模型
 * @author twg
 *
 */
public class TableModel {
	private String name = "";// table名
	private String comment = "";// table备注
	private List<ColumnData> columnDatas = Lists.newArrayList();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public List<ColumnData> getColumnDatas() {
		return columnDatas;
	}

	public void setColumnDatas(List<ColumnData> columnDatas) {
		this.columnDatas = columnDatas;
	}
	
	public void addColumnData(ColumnData columnData){
		this.columnDatas.add(columnData);
	}

	public List<ColumnData> getPrimayKey() {
		List<ColumnData> pks = Lists.newArrayList();
		for (ColumnData column : this.columnDatas) {
			if (column.isPk()) {
				pks.add(column);
			}
		}
		return pks;
	}

}

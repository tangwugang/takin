package org.takinframework.spring.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.takinframework.codegenerator.util.ColumnData;
import org.takinframework.core.util.ClassReflectUtil;

/**
 * PreparedStatement(???)赋值
 * @author twg
 *
 * @param <T>
 */

public class MyBatchPreparedStatementSetter<T> implements BatchPreparedStatementSetter {
	/**
	 * 实体集
	 */
	private List<T> entitys;
	/**
	 * 表字段集
	 */
	private List<ColumnData> columnDatas;
	
	
	public MyBatchPreparedStatementSetter(List<T> entitys,List<ColumnData> columnDatas){
		this.entitys = entitys;
		this.columnDatas = columnDatas;
		
	}

	public void setValues(PreparedStatement ps, int i) throws SQLException {
		for (int j = 0; j < columnDatas.size(); j++) {
			String columnName = columnDatas.get(j).getColumnName();
			try {
				ps.setObject(j+1, ClassReflectUtil.getFieldValue(entitys.get(i), columnName));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
	}

	public int getBatchSize() {
		return entitys.size();
	}

}

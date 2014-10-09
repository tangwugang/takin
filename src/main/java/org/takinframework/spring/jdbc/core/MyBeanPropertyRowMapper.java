package org.takinframework.spring.jdbc.core;

import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.BeanUtils;
import org.springframework.jdbc.core.RowMapper;
import org.takinframework.codegenerator.util.ColumnData;
import org.takinframework.core.util.ClassReflectUtil;
import org.takinframework.core.util.DateUtils;
import org.takinframework.core.util.LogUtil;

/**
 * ResultSet结果列表映射到实体
 * @author twg
 *
 * @param <T>
 */
public class MyBeanPropertyRowMapper<T> implements RowMapper<T> {
	private List<ColumnData> columnDatas;
	private Class<T> cls;
	
	public MyBeanPropertyRowMapper(){}
	
	@SuppressWarnings("unchecked")
	public MyBeanPropertyRowMapper(T entity,List<ColumnData> columnDatas){
		this.columnDatas = columnDatas;
		this.cls = (Class<T>) entity.getClass();
	}

	public T mapRow(ResultSet rs, int rowNum) throws SQLException {
		T mappedObject = BeanUtils.instantiate(cls);
		ResultSetMetaData rsmd = rs.getMetaData();
		int columnCount = rsmd.getColumnCount();
		for (int index = 1; index <= columnCount; index++) {
			try {
				getColumnValue(rs, index, columnDatas,mappedObject);
			} catch (Exception e) {
				LogUtil.error("ResultSet结果映射到实体失败", e);
			}
		}
		return (T) mappedObject;
	}
	
	@SuppressWarnings("unchecked")
	protected Object getColumnValue(ResultSet rs, int index, List<ColumnData> columnDatas,T bw) throws Exception {
		Object value = null;
		ColumnData c = columnDatas.get(index-1);
		if (c.getDataType().toLowerCase().equals("string")) {
			value = rs.getString(index);
		}
		else if (c.getDataType().toLowerCase().equals("boolean")) {
			value = rs.getBoolean(index);
		}
		else if (c.getDataType().toLowerCase().equals("byte")) {
			value = rs.getByte(index);
		}
		else if (c.getDataType().toLowerCase().contains("short")) {
			value = rs.getShort(index);
		}
		else if (c.getDataType().toLowerCase().contains("int")) {
			value = rs.getInt(index);
		}
		else if (c.getDataType().toLowerCase().equals("long")) {
			value = rs.getLong(index);
		}
		else if (c.getDataType().toLowerCase().equals("float")) {
			value = rs.getFloat(index);
		}
		else if (c.getDataType().toLowerCase().equals("double")) {
			value = rs.getDouble(index);
		}
		else if (c.getDataType().toLowerCase().equals("byte[]")) {
			value = rs.getBytes(index);
		}
		else if (c.getDataType().toLowerCase().equals("date")) {
			value = DateUtils.parseDateTimeStamp(rs.getString(index));
		}else{
			value = rs.getBytes(index);
		}
		
		try {
			bw = (T) ClassReflectUtil.setFieldValue(bw, c.getColumnName(), value);
		} catch (Exception e) {
			LogUtil.error("对象转换为java实体失败", e);
		}
		
		return bw;
	}
	
	
	public Object getResultSetValue(ResultSet rs, int index) throws SQLException {
		Object obj = rs.getObject(index);
		String className = null;
		if (obj != null) {
			className = obj.getClass().getName();
		}
		if (obj instanceof Blob) {
			obj = rs.getBytes(index);
		}
		else if (obj instanceof Clob) {
			obj = rs.getString(index);
		}
		else if (className != null &&
				("oracle.sql.TIMESTAMP".equals(className) ||
				"oracle.sql.TIMESTAMPTZ".equals(className))) {
			obj = rs.getTimestamp(index);
		}
		else if (className != null && className.startsWith("oracle.sql.DATE")) {
			String metaDataClassName = rs.getMetaData().getColumnClassName(index);
			if ("java.sql.Timestamp".equals(metaDataClassName) ||
					"oracle.sql.TIMESTAMP".equals(metaDataClassName)) {
				obj = rs.getTimestamp(index);
			}
			else {
				obj = rs.getDate(index);
			}
		}
		else if (obj != null && obj instanceof java.sql.Date) {
			if ("java.sql.Timestamp".equals(rs.getMetaData().getColumnClassName(index))) {
				obj = rs.getTimestamp(index);
			}
		}
		return obj;
		
	}

	

}

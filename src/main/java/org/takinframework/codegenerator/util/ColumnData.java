package org.takinframework.codegenerator.util;

import org.takinframework.core.util.StringUtil;

/**
 * 表字段类
 * @author twg
 *
 */
public class ColumnData {

	private String columnName;//字段名
	private String dataType;//字段类型
	private String columnComment;//字段描述
	private boolean isPk = false;//是否主键
	private boolean isFk = false;//是否外键
	private boolean isNull = true;//是否为空
	private Integer columnLength;//字段长度
	private Integer precisions;//
	private Integer scale;//
	private String columnKey;//外键值PRI、MUL
	private String defaultValue;
	
	public String getColumnName() {
		return columnName;
	}
	public void setColumnName(String columnName) {
		this.columnName = columnName;
	}
	public String getDataType() {
		return dataType;
	}
	public void setDataType(String dataType) {
		this.dataType = dataType;
	}
	public String getColumnComment() { 
		if (StringUtil.isNotEmpty(this.columnComment)) {
	      this.columnComment = this.columnComment.replace("'", "''");
	    }
	    return this.columnComment;
	}
	
	public void setColumnComment(String columnComment) {
		this.columnComment = columnComment;
	}
	public boolean isPk() {
		return isPk;
	}
	public void setPk(boolean isPk) {
		this.isPk = isPk;
	}
	public boolean isFk() {
		return isFk;
	}
	public void setFk(boolean isFk) {
		this.isFk = isFk;
	}
	public boolean isNull() {
		return isNull;
	}
	public void setNull(boolean isNull) {
		this.isNull = isNull;
	}
	public Integer getColumnLength() {
		return columnLength;
	}
	public void setColumnLength(Integer columnLength) {
		this.columnLength = columnLength;
	}
	public Integer getPrecisions() {
		return precisions;
	}
	public void setPrecisions(Integer precisions) {
		this.precisions = precisions;
	}
	public Integer getScale() {
		return scale;
	}
	public void setScale(Integer scale) {
		this.scale = scale;
	}
	public String getColumnKey() {
		return columnKey;
	}
	public void setColumnKey(String columnKey) {
		this.columnKey = columnKey;
	}
	public String getDefaultValue() {
		return defaultValue;
	}
	public void setDefaultValue(String defaultValue) {
		this.defaultValue = defaultValue;
	} 
	
	
}

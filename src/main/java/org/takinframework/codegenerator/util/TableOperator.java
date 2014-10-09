package org.takinframework.codegenerator.util;

import java.util.List;

import org.takinframework.core.util.StringUtil;
/**
 * 数据库表生成工具类
 * @author twg
 *
 */
public class TableOperator {
	/**
	 * 创建数据库表
	 * @param model
	 * @return
	 */
	public static String createTable(TableModel model) {
		List<ColumnData> columnList = model.getColumnDatas();
		StringBuffer sb = new StringBuffer();
		String pkColumn = null;
		sb.append("CREATE TABLE " + model.getName() + " (\n");
		for (int i = 0; i < columnList.size(); i++) {
			ColumnData cm = (ColumnData) columnList.get(i);
			sb.append(cm.getColumnName()).append(" ");
			sb.append(CodeType.setMySqlType(cm.getDataType(),
					cm.getColumnLength(), cm.getPrecisions(), cm.getScale()));
			sb.append(" ");
			//是否可以为空默认可以为空
			if (!cm.isNull()) {
				sb.append(" NOT NULL ");
			}
			String defaultValue = cm.getDefaultValue();

			if (StringUtil.isNotEmpty(defaultValue)) {
				sb.append(" default " + defaultValue);
			}

			if (cm.isPk()) {
				if (pkColumn == null)
					pkColumn = cm.getColumnName();
				else {
					pkColumn = pkColumn + "," + cm.getColumnName();
				}
			}

			if ((cm.getColumnComment() != null)
					&& (cm.getColumnComment().length() > 0)) {
				sb.append(" COMMENT '" + cm.getColumnComment() + "'");
			}
			sb.append(",\n");
		}

		if (pkColumn != null) {
			sb.append(" PRIMARY KEY (" + pkColumn + ")");
		}

		sb.append("\n)");
		if ((model.getComment() != null) && (model.getComment().length() > 0)) {
			sb.append(" COMMENT='" + model.getComment() + "'");
		}

		sb.append(";");
		return sb.toString();
	}
	
	

}

package org.takinframework.codegenerator.generator;

import org.takinframework.codegenerator.util.TableOperator;
import org.takinframework.codegenerator.util.TableModel;

/**
 * 实体生成数据库表
 * @author twg
 *
 */
public class TableGenerator extends CodeFactory {
	public static void createTable(TableModel model){
		jdbcTemplate.execute(TableOperator.createTable(model));
	}

}

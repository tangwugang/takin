package org.takinframework.codegenerator.service.impl;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.takinframework.codegenerator.generator.CodeGenerator;
import org.takinframework.codegenerator.service.CodeGenerateService;
import org.takinframework.core.common.service.impl.BaseServiceImpl;

/**
 * 代码生成service实现类
 * @author twg
 *
 */
@Service("codeGenerateService")
@Transactional
public class CodeGenerateServiceImpl extends BaseServiceImpl implements CodeGenerateService {

	public void generate(String bussiPackage, String tableName, String entityPackage, String ftl_description) throws Exception {
		CodeGenerator.setDbType(dbType());
		CodeGenerator.setJdbcTemplate(jdbcTemplate);
		CodeGenerator.generate(bussiPackage, tableName, entityPackage, ftl_description);
	}
	public String dbType() throws Exception {
		return jdbcDao.getDBType();
	}
	public void createTable() throws Exception {
//		java.util.List<ColumnData>columnDataList  = Lists.newArrayList();
//		for (int i = 0; i < 12; i++) {
//			ColumnData columnData = new ColumnData();
//			columnData.setColumnName("id");
//			
//		}
//		
//		ColumnData columnData = new ColumnData();
//		columnData.setColumnName("userName");
//		columnData.setColumnName("realName");
//		columnData.setColumnName("userKey");
//		columnData.setColumnName("password");
//		columnData.setColumnName("activitiSync");
//		columnData.setColumnName("status");
//		columnData.setColumnName("signature");
//		columnData.setColumnName("signatureFile");
//		columnData.setColumnName("mobilePhone");
//		columnData.setColumnName("officePhone");
//		columnData.setColumnName("email");
//		
//		
//		TableModel model = new TableModel();
//		model.setName("sys_user");
//		model.addColumnData(columnData);
//		TableGenerator.createTable(model);
		
	}
	
	public void generate(String bussiPackage, String tableName,
			String entityPackage, String ftl_description, String[] templates)
			throws Exception {
		CodeGenerator.setDbType(dbType());
		CodeGenerator.setJdbcTemplate(jdbcTemplate);
		CodeGenerator.generate(bussiPackage, tableName, entityPackage, ftl_description,templates);
	}
	

}

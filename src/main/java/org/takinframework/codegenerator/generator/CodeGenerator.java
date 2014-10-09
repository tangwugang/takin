package org.takinframework.codegenerator.generator;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import java.util.ResourceBundle;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.jdbc.core.JdbcTemplate;
import org.takinframework.codegenerator.database.jwebReadTable;
import org.takinframework.codegenerator.util.CodeUtil;
import org.takinframework.codegenerator.util.DbType;
import org.takinframework.core.util.DateUtils;
import org.takinframework.core.util.FreeMarkers;
import org.takinframework.core.util.LogUtil;
import com.google.common.collect.Maps;
import freemarker.template.Configuration;
import freemarker.template.Template;

/**
 * 代码生成工具执行类
 * @author twg
 *
 */
public class CodeGenerator extends CodeFactory {
	
	// ========== ↓↓↓↓↓↓ 执行前请修改参数，谨慎执行。↓↓↓↓↓↓ ====================

		// 主要提供基本功能模块代码生成。
		// 目录生成结构：{packageName}/{moduleName}/{dao,entity,service,web}/{subModuleName}/{className}
		
		/**
		 * 生成代码(生成全部)
		 * @param bussiPackage 包名(org.takinframework.web)
		 * @param tableName 数据库表名(sys_user)
		 * @param entityPackage 模块名(system)
		 * @param ftl_description 实体描述(系统用户信息表)
		 */
		public static void generate(String bussiPackage,String tableName,
				String entityPackage,String ftl_description){
			try {
				generate(bussiPackage, tableName, entityPackage, ftl_description, true,jdbcTemplate,dbType,null);
			} catch (IOException e) {
				LogUtil.error("代码生成失败", e);
			}
		}
		
		/**
		 * 生成代码(模板可选)
		 * @param bussiPackage 包名(org.takinframework.web)
		 * @param tableName 数据库表名(sys_user)
		 * @param entityPackage 模块名(system)
		 * @param ftl_description 实体描述(系统用户信息表)
		 * @param templates {"entityTemplate","mapperTemplate",
		 * "TempMapper","serviceITemplate","serviceImplTemplate","controllerTemplate"}
		 */
		public static void generate(String bussiPackage,String tableName,
				String entityPackage,String ftl_description,String[] templates){
			try {
				generate(bussiPackage, tableName, entityPackage, ftl_description, true,jdbcTemplate,dbType,templates);
			} catch (IOException e) {
				LogUtil.error("代码生成失败", e);
			}
		}
		
		
		private static void generate(String bussiPackage,String tableName,
				String entityPackage,String ftl_description,boolean isEnable,
				JdbcTemplate jdbcTemplate,String dbType,String[] templates) throws IOException{
			// ========== ↑↑↑↑↑↑ 执行前请修改参数，谨慎执行。↑↑↑↑↑↑ ====================
			String dbName = ResourceBundle.getBundle("jweb/jweb_database").getString("database_name");//数据库名
			String entityName = CodeUtil.getTablesNameToClassName(tableName);// 类名，例：TSUser
			if (!isEnable){
				LogUtil.error("请启用代码生成工具，设置参数：isEnable = true");
				return;
			}
			
			if (StringUtils.isBlank(entityPackage) || StringUtils.isBlank(entityPackage) 
					|| StringUtils.isBlank(entityName) || StringUtils.isBlank(ftl_description)){
				LogUtil.error("参数设置错误：包名、模块名、类名、功能名不能为空。");
				return;
			}
			// 获取文件分隔符
			String separator = File.separator;
			
			// 获取工程路径
			File projectPath = new DefaultResourceLoader().getResource("").getFile();
			String source_root_package = ResourceBundle.getBundle("jweb/jweb_config").getString("source_root_package").replace(".", "/");
			String templatepath = ResourceBundle.getBundle("jweb/jweb_config").getString("templatepath").replace(".", "/");
			String webroot_package = ResourceBundle.getBundle("jweb/jweb_config").getString("webroot_package").replace(".", "/");
			String mapperpath = ResourceBundle.getBundle("jweb/jweb_config").getString("mapperpath").replace(".", "/");
			while(!new File(projectPath.getPath()+separator+source_root_package).exists()){
				projectPath = projectPath.getParentFile();
			}
			LogUtil.info("Project Path: {%s}", projectPath);
			
			String templatePath = projectPath+separator+templatepath;
			// 模板文件路径
			String tplPath = StringUtils.replace(templatePath, "/", separator);
			LogUtil.info("Template Path: {%s}", tplPath);
			
			// Java文件路径
			String javaPath = StringUtils.replaceEach(projectPath+separator+source_root_package, 
					new String[]{"/", "."}, new String[]{separator, separator});
			LogUtil.info("Java Path: {%s}", javaPath);
			//mybatis mapper.xml文件路径
			String mapperPath = StringUtils.replace(projectPath+separator+mapperpath, "/", separator);
			LogUtil.info("Mapper Path: {%s}", mapperPath);
			
			// 视图文件路径
			String viewPath = StringUtils.replace(projectPath+separator+webroot_package, "/", separator);
			LogUtil.info("View Path: {%s}", viewPath);
			
			// 代码模板配置
			Configuration cfg = new Configuration();
			cfg.setDirectoryForTemplateLoading(new File(tplPath));
		
			// 定义模板变量
			Map<String, Object> model = Maps.newHashMap();
			model.put("bussiPackage", StringUtils.lowerCase(bussiPackage));
			model.put("entityPackage", StringUtils.lowerCase(entityPackage));
			model.put("entityName", entityName);
			model.put("ftl_create_time", DateUtils.formatDate());
			model.put("ftl_description", ftl_description);
			model.put("tableName", tableName);
			model.put("dbType", dbType);
			
		
			/*******************************生成sql语句**********************************/
			try{
				Map<String,Object> sqlMap=jwebReadTable.getAutoCreateSql(tableName,dbName,jdbcTemplate,dbType);
				model.put("columnDatas",jwebReadTable.getColumnDatas(tableName,dbName,jdbcTemplate,dbType)); //生成bean
				model.put("SQL",sqlMap);
			}catch(Exception e){
				e.printStackTrace();
				return;
			}
			
			/******************************生成bean字段*********************************/
			try{
				model.put("feilds",jwebReadTable.getBeanFeilds(tableName,dbName,jdbcTemplate,dbType)); //生成bean
			}catch(Exception e){
				e.printStackTrace();
			}
			
			if(null != templates && templates.length>0){
				for (String temp : templates) {
					
					// 生成 Entity
					Template template = cfg.getTemplate("entityTemplate.ftl");
					String content = FreeMarkers.renderTemplate(template, model);
					String filePath = javaPath+separator+StringUtils.replace(model.get("bussiPackage").toString(), ".", separator)+separator+model.get("entityPackage")+separator+"entity"+separator+model.get("entityName")+".java";
					if(temp.equals("entityTemplate")){
						writeFile(content, filePath);
						LogUtil.info("Entity: {%s}", filePath);
					}
						
					// 生成 Dao
					template = cfg.getTemplate("mapperTemplate.ftl");
					content = FreeMarkers.renderTemplate(template, model);
					filePath = javaPath+separator+StringUtils.replace(model.get("bussiPackage").toString(), ".", separator)+separator+model.get("entityPackage")+separator+"mapper"+separator+model.get("entityName")+"Mapper.java";
					if(temp.equals("mapperTemplate")){
						writeFile(content, filePath);
						LogUtil.info("Dao: {%s}", filePath);
					}
					
					
					//生成Mapper.xml
					template = cfg.getTemplate("TempMapper.xml");
					content = FreeMarkers.renderTemplate(template, model);
					filePath = mapperPath+separator+model.get("entityPackage")+separator+model.get("entityName")+"Mapper.xml";
					if(temp.equals("TempMapper")){
						writeFile(content, filePath);
						LogUtil.info("Mapper.xml: {%s}", filePath);
					}
					
					
					// 生成 Service
					template = cfg.getTemplate("serviceITemplate.ftl");
					content = FreeMarkers.renderTemplate(template, model);
					filePath = javaPath+separator+StringUtils.replace(model.get("bussiPackage").toString(), ".", separator)+separator+model.get("entityPackage")+separator+"service"+separator+model.get("entityName")+"Service.java";
					if(temp.equals("serviceITemplate")){
						writeFile(content, filePath);
						LogUtil.info("Service: {%s}", filePath);
					}
					
					
					// 生成 ServiceImpl
					template = cfg.getTemplate("serviceImplTemplate.ftl");
					content = FreeMarkers.renderTemplate(template, model);
					filePath = javaPath+separator+StringUtils.replace(model.get("bussiPackage").toString(), ".", separator)+separator+model.get("entityPackage")+separator+"service"+separator+"impl"+separator+model.get("entityName")+"ServiceImpl.java";
					if(temp.equals("serviceImplTemplate")){
						writeFile(content, filePath);
						LogUtil.info("ServiceImpl: {%s}", filePath);
					}
					
					
					// 生成 Controller
					template = cfg.getTemplate("controllerTemplate.ftl");
					content = FreeMarkers.renderTemplate(template, model);
					filePath = javaPath+separator+StringUtils.replace(model.get("bussiPackage").toString(), ".", separator)+separator+model.get("entityPackage")+separator+"controller"+separator+model.get("entityName")+"Controller.java";
					if(temp.equals("controllerTemplate")){
						writeFile(content, filePath);
						LogUtil.info("Controller: {%s}", filePath);
					}
					
					
					// 生成 ViewForm
				//				template = cfg.getTemplate("viewForm.ftl");
				//				content = FreeMarkers.renderTemplate(template, model);
				//				filePath = viewPath+separator+StringUtils.substringAfterLast(model.get("packageName"),".")
				//						+separator+model.get("moduleName")+separator+StringUtils.lowerCase(subModuleName)
				//						+separator+model.get("className")+"Form.jsp";
				//				writeFile(content, filePath);
				//				logger.info("ViewForm: {}", filePath);
					
					// 生成 ViewList
				//				template = cfg.getTemplate("viewList.ftl");
				//				content = FreeMarkers.renderTemplate(template, model);
				//				filePath = viewPath+separator+StringUtils.substringAfterLast(model.get("packageName"),".")
				//						+separator+model.get("moduleName")+separator+StringUtils.lowerCase(subModuleName)
				//						+separator+model.get("className")+"List.jsp";
				//				writeFile(content, filePath);
				//				logger.info("ViewList: {}", filePath);
					
					LogUtil.info("Generate Success.");
					
				}
				
			}else{
				
				// 生成 Entity
				Template template = cfg.getTemplate("entityTemplate.ftl");
				String content = FreeMarkers.renderTemplate(template, model);
				String filePath = javaPath+separator+StringUtils.replace(model.get("bussiPackage").toString(), ".", separator)+separator+model.get("entityPackage")+separator+"entity"+separator+model.get("entityName")+".java";
				writeFile(content, filePath);
				LogUtil.info("Entity: {%s}", filePath);
					
				// 生成 Dao
				template = cfg.getTemplate("mapperTemplate.ftl");
				content = FreeMarkers.renderTemplate(template, model);
				filePath = javaPath+separator+StringUtils.replace(model.get("bussiPackage").toString(), ".", separator)+separator+model.get("entityPackage")+separator+"mapper"+separator+model.get("entityName")+"Mapper.java";
				writeFile(content, filePath);
				LogUtil.info("Dao: {%s}", filePath);
				
				//生成Mapper.xml
				template = cfg.getTemplate("TempMapper.xml");
				content = FreeMarkers.renderTemplate(template, model);
				filePath = mapperPath+separator+model.get("entityPackage")+separator+model.get("entityName")+"Mapper.xml";
				writeFile(content, filePath);
				LogUtil.info("Mapper.xml: {%s}", filePath);
				
				// 生成 Service
				template = cfg.getTemplate("serviceITemplate.ftl");
				content = FreeMarkers.renderTemplate(template, model);
				filePath = javaPath+separator+StringUtils.replace(model.get("bussiPackage").toString(), ".", separator)+separator+model.get("entityPackage")+separator+"service"+separator+model.get("entityName")+"Service.java";
				writeFile(content, filePath);
				LogUtil.info("Service: {%s}", filePath);
				
				// 生成 ServiceImpl
				template = cfg.getTemplate("serviceImplTemplate.ftl");
				content = FreeMarkers.renderTemplate(template, model);
				filePath = javaPath+separator+StringUtils.replace(model.get("bussiPackage").toString(), ".", separator)+separator+model.get("entityPackage")+separator+"service"+separator+"impl"+separator+model.get("entityName")+"ServiceImpl.java";
				writeFile(content, filePath);
				LogUtil.info("ServiceImpl: {%s}", filePath);
				
				// 生成 Controller
				template = cfg.getTemplate("controllerTemplate.ftl");
				content = FreeMarkers.renderTemplate(template, model);
				filePath = javaPath+separator+StringUtils.replace(model.get("bussiPackage").toString(), ".", separator)+separator+model.get("entityPackage")+separator+"controller"+separator+model.get("entityName")+"Controller.java";
				writeFile(content, filePath);
				LogUtil.info("Controller: {%s}", filePath);
				
				// 生成 ViewForm
			//				template = cfg.getTemplate("viewForm.ftl");
			//				content = FreeMarkers.renderTemplate(template, model);
			//				filePath = viewPath+separator+StringUtils.substringAfterLast(model.get("packageName"),".")
			//						+separator+model.get("moduleName")+separator+StringUtils.lowerCase(subModuleName)
			//						+separator+model.get("className")+"Form.jsp";
			//				writeFile(content, filePath);
			//				logger.info("ViewForm: {}", filePath);
				
				// 生成 ViewList
			//				template = cfg.getTemplate("viewList.ftl");
			//				content = FreeMarkers.renderTemplate(template, model);
			//				filePath = viewPath+separator+StringUtils.substringAfterLast(model.get("packageName"),".")
			//						+separator+model.get("moduleName")+separator+StringUtils.lowerCase(subModuleName)
			//						+separator+model.get("className")+"List.jsp";
			//				writeFile(content, filePath);
			//				logger.info("ViewList: {}", filePath);
				
				LogUtil.info("Generate Success.");
				
			}
			
			
		}
		
		/**
		 * 创建单个文件
		 * @param descFileName 文件名，包含路径
		 * @return 如果创建成功，则返回true，否则返回false
		 */
		 private static boolean createFile(String descFileName) {
			File file = new File(descFileName);
			if (descFileName.endsWith(File.separator)) {
				LogUtil.debug(descFileName + " 为目录，不能创建目录!");
				return false;
			}
			if (!file.getParentFile().exists()) {
				// 如果文件所在的目录不存在，则创建目录
				if (!file.getParentFile().mkdirs()) {
					LogUtil.debug("创建文件所在的目录失败!");
					return false;
				}
			}
		
			// 创建文件
			try {
					file.delete();
					LogUtil.info("--------成功删除文件---------"+descFileName);
				if (file.createNewFile()) {
					LogUtil.info(descFileName + " 文件创建成功!");
					return true;
				} else {
					LogUtil.info(descFileName + " 文件创建失败!");
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				LogUtil.debug(descFileName + " 文件创建失败!");
				return false;
			}
		
		}
		
		/**
		 * 将内容写入文件
		 * @param content
		 * @param filePath
		 */
		 private static void writeFile(String content, String filePath) {
			try {
				if (createFile(filePath)){
					FileWriter fileWriter = new FileWriter(filePath, true);
					BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
					bufferedWriter.write(content);
					bufferedWriter.close();
					fileWriter.close();
				}else{
					LogUtil.info("生成失败，文件已存在！");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		public static void main(String[] arg){
			String dbType="MySQL";
			System.err.println(dbType.equals(DbType.MySQL.toString()));
		}

}

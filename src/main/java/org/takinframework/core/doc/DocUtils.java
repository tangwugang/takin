package org.takinframework.core.doc;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.takinframework.core.util.Encodes;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * word文档工具类
 * @author twg
 * @version 2013-12-25
 *
 */
public class DocUtils {
	
	private static Configuration configuration = null;
	private static final String PATH = "/doc";
	static{
		configuration = new Configuration();  
	    configuration.setClassForTemplateLoading(DocUtils.class, PATH);
	}
	
	public DocUtils(){
		
	}
	
	public DocUtils(String filePath){
		configuration = new Configuration();  
	    configuration.setClassForTemplateLoading(this.getClass(), filePath);
	}
	
	public static Template getTemplate(String file) throws IOException{
		return configuration.getTemplate(file);
	}
	
	public static Writer getWriter(){
		File file = new File("D:/temp/");
		if(!file.exists()){
			file.mkdirs();
	    }
		
		//输出文档路径及名称  
	    File outFile = new File("D:/temp/outFile.vm");
	    Writer out = null;
	    try {
	    out = new BufferedWriter(new OutputStreamWriter(
	    new FileOutputStream(outFile),"UTF-8"));
	    } catch (Exception e1) {
	    e1.printStackTrace();
	    }
	    return out;
		
	}
	
	/**
	 * 
	 * @param filePath 文件路径
	 * @param fileName 文件名
	 * @return
	 */
	public Writer getWriter(String fileName){
		File file = new File("C:/temp/");
		if(!file.exists()){
			file.mkdirs();
	    }
		
		//输出文档路径及名称  
	    File outFile = new File("C:/temp/"+fileName);
	    Writer out = null;
	    try {
	    out = new BufferedWriter(new OutputStreamWriter(
	    new FileOutputStream(outFile),"UTF-8"));
	    } catch (Exception e1) {
	    e1.printStackTrace();
	    }
	    return out;
		
	}
	
	/**
	 * 
	 * @param filePath 文件路径
	 * @param fileName 文件名
	 * @return
	 */
	public Writer getWriter(String filePath,String fileName){
		File file = new File(filePath);
		if(!file.exists()){
			file.mkdirs();
	    }
		
		//输出文档路径及名称  
	    File outFile = new File(filePath+File.separator+fileName);
	    Writer out = null;
	    try {
	    out = new BufferedWriter(new OutputStreamWriter(
	    new FileOutputStream(outFile),"gbk"));
	    } catch (Exception e1) {
	    e1.printStackTrace();
	    }
	    return out;
		
	}
	
	public static void createDoc(Template template,Map<String, Object> dataMap,Writer out) {
		//要填入模本的数据文件  
		
	    try {
			template.process(dataMap, out);
		} catch (TemplateException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			try {
				out.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}  
	      
	}
	
	/**  
	   * 注意dataMap里存放的数据Key值要与模板中的参数相对应  
	   * @param dataMap  
	   */ 
	public static Map<String, Object> getData(){
		Map<String, Object> dataMap = Maps.newHashMap();
		List<String> list = Lists.newArrayList();
		list.add("0");
		list.add("5");
		list.add("2");
		list.add("8");
		dataMap.put("year", "2013");
		dataMap.put("month", "12");
		dataMap.put("day", "25");
		dataMap.put("context", "√");
		dataMap.put("list", list);
		return dataMap;
	}
	
	/**
	   * 输出到客户端
	   * @param response 
	   * @param fileName 输出文件名
	   */
	  public static void write(HttpServletResponse response,String fileName){
		  response.reset();
	      response.setContentType("application/octet-stream; charset=utf-8");
	      response.setHeader("Content-Disposition", "attachment; filename="+Encodes.urlEncode(fileName));
		  
	  }
	
	public static final void main (String arg[]) throws IOException{
		Template template = DocUtils.getTemplate("test.vm");
		template.setEncoding("UTF-8");
		Writer out = DocUtils.getWriter();
		DocUtils.createDoc(template, getData(), out);
		System.out.println(System.getProperty("java.io.tmpdir"));
		
	}
	

}

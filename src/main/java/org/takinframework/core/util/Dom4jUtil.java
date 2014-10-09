package org.takinframework.core.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.URL;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.SAXValidator;
import org.dom4j.io.XMLWriter;
import org.dom4j.util.XMLErrorHandler;
import org.xml.sax.SAXException;

public class Dom4jUtil {
	private static final Log logger = LogFactory.getLog(Dom4jUtil.class);

	public static Document loadXml(String s) {
		Document document = null;
		try {
			document = DocumentHelper.parseText(s);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return document;
	}

	public static Document load(String filename, String encode) {
		Document document = null;
		try {
			SAXReader saxReader = new SAXReader();
			saxReader.setEncoding(encode);
			document = saxReader.read(new File(filename));
		} catch (Exception localException) {
		}
		return document;
	}

	public static Document loadXml(String xml, String encode)
			throws UnsupportedEncodingException {
		ByteArrayInputStream inputStream = new ByteArrayInputStream(
				xml.getBytes(encode));
		return loadXml(inputStream, encode);
	}

	public static Document loadXml(InputStream is) {
		return loadXml(is, "utf-8");
	}

	public static Document loadXml(InputStream is, String charset) {
		Document document = null;
		try {
			SAXReader reader = new SAXReader();
			reader.setEncoding(charset);
			document = reader.read(is);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return document;
	}

	public static void write(Document document, String fileName)
			throws IOException {
		String xml = document.asXML();
		FileUtils.writeFile(fileName, xml);
	}

	public static void write(String str, String fileName) throws IOException,
			DocumentException {
		Document document = DocumentHelper.parseText(str);
		write(document, fileName);
	}

	public Document load(URL url) throws DocumentException {
		SAXReader reader = new SAXReader();
		Document document = reader.read(url);
		return document;
	}

	public static Document load(String filename) {
		Document document = null;
		try {
			SAXReader reader = new SAXReader();
			document = reader.read(new File(filename));
			document.normalize();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return document;
	}

	@SuppressWarnings("rawtypes")
	public static String transFormXsl(String xml, String xsl,
			Map<String, String> map) throws Exception {
		StringReader xmlReader = new StringReader(xml);
		StringReader xslReader = new StringReader(xsl);

		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer(new StreamSource(
				xslReader));
		if (map != null) {
			Iterator it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry obj = (Map.Entry) it.next();
				transformer.setParameter((String) obj.getKey(), obj.getValue());
			}
		}
		StreamSource xmlSource = new StreamSource(xmlReader);

		StringWriter writer = new StringWriter();
		Result result = new StreamResult(writer);
		transformer.transform(xmlSource, result);

		return writer.toString();
	}

	public static String transXmlByXslt(String xml, String xslPath,
			Map<String, String> map) throws Exception {
		Document document = loadXml(xml);
		document.setXMLEncoding("UTF-8");

		Document result = styleDocument(document, xslPath, map);

		return docToString(result);
	}

	public static String transFileXmlByXslt(String xmlPath, String xslPath,
			Map<String, String> map) throws Exception {
		Document document = load(xmlPath);
		document.setXMLEncoding("UTF-8");

		Document result = styleDocument(document, xslPath, map);

		return docToString(result);
	}

	public static String docToString(Document document) {
		String s = "";
		try {
			ByteArrayOutputStream out = new ByteArrayOutputStream();
			OutputFormat format = new OutputFormat("  ", true, "UTF-8");
			XMLWriter writer = new XMLWriter(out, format);
			writer.write(document);
			s = out.toString("UTF-8");
		} catch (Exception ex) {
			logger.error("docToString error:" + ex.getMessage());
		}
		return s;
	}

	public static String docToPrettyString(Document document) {
		String s = "";
		try {
			Writer writer = new StringWriter();
			OutputFormat format = OutputFormat.createPrettyPrint();
			format.setSuppressDeclaration(true);
			XMLWriter xmlWriter = new XMLWriter(writer, format);
			xmlWriter.write(document);
			s = writer.toString();
		} catch (Exception ex) {
			logger.error("docToString error:" + ex.getMessage());
		}
		return s;
	}

	@SuppressWarnings("rawtypes")
	public static Document styleDocument(Document document, String stylesheet,
			Map<String, String> map) throws Exception {
		TransformerFactory factory = TransformerFactory.newInstance();
		Transformer transformer = factory.newTransformer(new StreamSource(
				stylesheet));

		if (map != null) {
			Iterator it = map.entrySet().iterator();
			while (it.hasNext()) {
				Map.Entry obj = (Map.Entry) it.next();
				transformer.setParameter((String) obj.getKey(), obj.getValue());
			}
		}

		DocumentSource source = new DocumentSource(document);
		DocumentResult result = new DocumentResult();
		transformer.transform(source, result);

		Document transformedDoc = result.getDocument();
		return transformedDoc;
	}

	@SuppressWarnings("unused")
	public static String validXmlBySchema(String xml, String schema) {
		String result = "";
		try {
			XMLErrorHandler errorHandler = new XMLErrorHandler();

			SAXParserFactory factory = SAXParserFactory.newInstance();

			factory.setValidating(true);

			factory.setNamespaceAware(true);

			SAXParser parser = factory.newSAXParser();

			SAXReader xmlReader = new SAXReader();

			Document xmlDocument = xmlReader.read(new File(xml));

			parser.setProperty(
					"http://java.sun.com/xml/jaxp/properties/schemaLanguage",
					"http://www.w3.org/2001/XMLSchema");
			parser.setProperty(
					"http://java.sun.com/xml/jaxp/properties/schemaSource",
					"file:" + schema);

			SAXValidator validator = new SAXValidator(parser.getXMLReader());

			validator.setErrorHandler(errorHandler);

			validator.validate(xmlDocument);
			XMLWriter writer = new XMLWriter(OutputFormat.createPrettyPrint());

			if (errorHandler.getErrors().hasContent()) {
				result = "<result success='0'>XML文件通过XSD文件校验失败,请检查xml是否符合指定格式!</result>";
			} else
				result = "<result success='1'>XML文件通过XSD文件校验成功!</result>";
		} catch (Exception ex) {
			result = "<result success='0'>XML文件通过XSD文件校验失败:" + ex.getMessage()
					+ "</result>";
		}
		return result;
	}

	public static boolean validByXsd(String xsdPath, InputStream xmlData) {
		SchemaFactory schemaFactory = SchemaFactory
				.newInstance("http://www.w3.org/2001/XMLSchema");

		File schemaFile = new File(xsdPath);

		Schema schema = null;
		try {
			schema = schemaFactory.newSchema(schemaFile);
		} catch (SAXException e) {
			e.printStackTrace();
		}

		Validator validator = schema.newValidator();

		Source source = new StreamSource(xmlData);
		try {
			validator.validate(source);
		} catch (Exception ex) {
			logger.info(ex.getMessage());
			return false;
		}
		return true;
	}

	public static String getString(Element element, String attrName) {
		return getString(element, attrName, Boolean.valueOf(false));
	}

	public static String getString(Element element, String attrName,
			Boolean fuzzy) {
		if (BeanUtils.isEmpty(element))
			return null;
		String val = element.attributeValue(attrName);
		if (StringUtil.isEmpty(val))
			return null;
		if (fuzzy.booleanValue()) {
			val = "%" + val + "%";
		}
		return val;
	}

	public static void addAttribute(Element element, String attrName, Object val) {
		addAttribute(element, attrName, val, "yyyy-MM-dd HH:mm:ss");
	}

	public static void addAttribute(Element element, String attrName,
			Object val, String format) {
		if (BeanUtils.isEmpty(val))
			return;
		if ((val instanceof Date)) {
			String dateStr = TimeUtil.getDateTimeString((Date) val, format);
			element.addAttribute(attrName, dateStr);
		} else {
			element.addAttribute(attrName, val.toString());
		}
	}
	
	public static Document readXml(String path){
		//用来载入xml文件的
	    SAXReader reader = new SAXReader(); 
	    try {
			Document document = reader.read(path);
			return document;
			//获取root(根)节点
//			Element root=document.getRootElement();
//			//获取单个节点，如果有多个，只会获取到第一个
//			Element show=(Element) root.selectSingleNode("/students/student[@id='1000']");
//			System.err.println(show.attributeValue("value"));
		} catch (DocumentException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	@SuppressWarnings("unused")
	public static void main(String [] args){
		 //创建了一个xml文档
        Document doc=DocumentHelper.createDocument();
        //添加注释
        doc.addComment("这里是注释");
        //创建了一个名为students节点，因为是第一个创建，所以是根节点,再通过doc创建一个则会报错。
        Element root=doc.addElement("students");
        //在root节点下创建一个名为student的节点
        Element stuEle=root.addElement("student");
        //给student节点添加属性
        stuEle.addAttribute("id", "1000");
        //给student节点添加一个子节点
        Element nameEle=stuEle.addElement("name");
        //设置子节点的文本
        nameEle.setText("张三");
        //用于格式化xml内容和设置头部标签
        OutputFormat format = OutputFormat.createPrettyPrint();
        //设置xml文档的编码为gbk
//        format.setEncoding("uft-8");
//        Writer out;
//        try {
//                //创建一个输出流对象
////                out= new FileWriter("src/qunhao.xml");
////                //创建一个dom4j创建xml的对象
////                XMLWriter writer = new XMLWriter(out, format);
////                //调用write方法将doc文档写到指定路径
////			    writer.write(doc);
////			    writer.close();
//			    System.out.print("生成XML文件成功");
//			    readXml();
//	        } catch (IOException e) {
//	                System.out.print("生成XML文件失败");
//	                e.printStackTrace();
//	        }
        
		
	}

}

package org.takinframework.core.util;

import java.beans.BeanInfo;
import java.beans.IntrospectionException;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;

import org.apache.commons.lang3.math.NumberUtils;
import org.takinframework.core.common.exception.BusinessException;
import org.takinframework.core.model.BaseEntity;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * 反射工具类
 * @author twg
 *
 */
public class ClassReflectUtil {
	
	private static Map<Object, Object> filedMap = Collections.synchronizedMap(Maps.newHashMap());
	@SuppressWarnings("rawtypes")
	private static final Map<Class, BeanInfo> classCache = Collections.synchronizedMap(new WeakHashMap<Class, BeanInfo>());
	
	
	/**
	 * 根据类名反射创建对象
	 * @param name 类名
	 * @return 对象
	 * @throws Exception
	 */
	public static Object getInstance(String name) throws Exception {
		Class<?> cls = Class.forName(name);
		return cls.newInstance();
	}
	
	public static Object getInstance(Class<?> cls) throws Exception {
		return cls.newInstance();
	}
	
	/**
	 * 反射方法，打印对象的属性，方法，构造器属性
	 * @param obj 被反射对象
	 */
	public static void reflect(Object obj) {
		Class<?> cls = obj.getClass();
		System.out.println("************构  造  器************");
		Constructor<?>[] constructors = cls.getConstructors();
		for (Constructor<?> constructor : constructors) {
			System.out.println("构造器名称:" + constructor.getName() + "\t"+ "    "
					+ "构造器参数类型:"
					+ Arrays.toString(constructor.getParameterTypes()));
		}
		System.out.println("************属     性************");
		Field[] fields = cls.getDeclaredFields();
		// cls.getFields() 该方法只能访问共有的属性
		// cls.getDeclaredFields()  可以访问私有属性
		for (Field field : fields) {
			System.out.println("属性名称:" + field.getName() + "\t"+ "属性类型:"
					+ field.getType().getSimpleName()+"\t");
		}
		System.out.println("************方   法************");
		Method[] methods = cls.getMethods();
		for (Method method : methods) {
			System.out.println("方法名:" + method.getName() + "\t" + "方法返回类型："
					+ method.getReturnType() + "\t"+ "方法参数类型:"
					+ Arrays.toString(method.getParameterTypes()));
		}
	}
	
	/**
	 * 反射获取属性
	 * @param obj
	 * @return
	 */
	public static List<String> getFields(Object obj){
		List<String> files = Lists.newArrayList();
		Class<?> cls = obj.getClass();
		Field[] fields = null;
		if(null != cls.getSuperclass()){
			cls = cls.getSuperclass();
			fields = cls.getFields();
			for (Field field : fields) {
				files.add(field.getName());
			}
		}
		
		cls = obj.getClass();
		fields = cls.getDeclaredFields();
		for (Field field : fields) {
			files.add(field.getName());
		}
		return files;
	}
	
	/**
	 * 反射获取属性
	 * @param obj
	 * @return
	 */
	public static List<String> getFields(Class<?> cls){
		List<String> files = Lists.newArrayList();
		Field[] fields = null;
		fields = cls.getDeclaredFields();
		for (Field field : fields) {
			files.add(field.getName());
		}
		if(null != cls.getSuperclass()){
			cls = cls.getSuperclass();
			fields = cls.getDeclaredFields();
			for (Field field : fields) {
				files.add(field.getName());
			}
		}
		return files;
	}
	
	/**
	 * 返回实体类名
	 * @param obj
	 * @return
	 */
	public static String getEntityName(Object obj){
		Class<?> cls = obj.getClass();
		return cls.getName().substring(cls.getName().lastIndexOf(".")+1);
	}
	
	/**
	 * 返回实体类名
	 * @param obj
	 * @return
	 */
	public static String getEntityName(Class<?> cls){
		return cls.getName().substring(cls.getName().lastIndexOf(".")+1);
		
	}
	
	@SuppressWarnings("unchecked")
	public static <T> BeanInfo getBeanInfo(Class<T> clazz) {
		BeanInfo beanInfo = null;
        try {
            if (classCache.get(clazz) == null) {
                beanInfo = Introspector.getBeanInfo(clazz);
                classCache.put(clazz, beanInfo);
                // Immediately remove class from Introspector cache, to allow for proper
                // garbage collection on class loader shutdown - we cache it here anyway,
                // in a GC-friendly manner. In contrast to CachedIntrospectionResults,
                // Introspector does not use WeakReferences as values of its WeakHashMap!
                Class<T> classToFlush = clazz;
                do {
                    Introspector.flushFromCaches(classToFlush);
                    classToFlush = (Class<T>) classToFlush.getSuperclass();
                } while (classToFlush != null);
            } else {
                beanInfo = classCache.get(clazz);
            }
        } catch (IntrospectionException e) {
            LogUtil.error("获取BeanInfo失败", e);
        }
        return beanInfo;
    }
	
	
	
	/**
	 * 
	 * @param obj 访问对象
	 * @param filedname  对象的属性
	 * @return 返回对象的属性值
	 * @throws Exception
	 */
	public static Object getFieldValue(Object obj,String filedname) throws Exception{
		//反射出类型
		Class<?> cls = obj.getClass();
		Field field = null;
		//反射出类型字段
		 try {
			 try {
				 field = cls.getDeclaredField(filedname);
			} catch (Exception e) {
				cls = cls.getSuperclass();
				field = cls.getDeclaredField(filedname);
			}
		   } catch (Exception e) {
			   e.printStackTrace();
			   System.out.println("没有这个字段："+filedname);
		   }
		//获取属性时，压制Java对访问修饰符的检查 
		field.setAccessible(true);
		//在对象obj上读取field属性的值
		Object val = field.get(obj);
//		if(field.getType().getName().toLowerCase().contains("date") &&
//				null == val){
//			field.set(obj, new Date());		
//		}
		if(field.getType().getSimpleName().toLowerCase().equals("string") &&
				filedname.equals("id") && null == val){
			field.set(obj, UUIDGenerator.generate());
		}
		Object v = field.get(obj);
		field.setAccessible(false);
		return v;
	}
	
	/**
	 * 
	 * @param obj 访问对象
	 * @param filedname  对象的属性
	 * @return 返回对象的属性值
	 * @throws Exception
	 */
	public static Object setIdKeyValue(Object obj,String filedname,String value) throws Exception{
		Field field = setFiledCache(obj,filedname);
		if(field==null){
			return null;
		}
		//获取属性时，压制Java对访问修饰符的检查 
		field.setAccessible(true);
		//---------------------------------------------------
		//针对表主键为字符类型进行赋值UUID,如果为int类型采用自增方式
		if(!field.getType().getSimpleName().contains("Integer")){
			field.set(obj, value);		
		}
		//---------------------------------------------------
		//在对象obj上读取field属性的值
		Object val = field.get(obj);
		field.setAccessible(false);
		return val;
	}
	
	
	
	/**
	 * 
	 * @param obj 访问对象
	 * @param filedname  对象的属性
	 * @return 返回对象的属性值
	 * @throws Exception
	 */
	public static Object setFieldValue(Object obj,String filedname,String value) throws Exception{
		//反射出类型
		Class<?> cls = obj.getClass();
		Field field = null;
		//反射出类型字段
		 try {
			 try {
				 field = cls.getDeclaredField(filedname);
			} catch (Exception e) {
				cls = cls.getSuperclass();
				field = cls.getDeclaredField(filedname);
			}
		   } catch (Exception e) {
			   e.printStackTrace();
			   System.out.println("没有这个字段："+filedname);
		   }
		if(field==null){
			return null;
		}
		//获取属性时，压制Java对访问修饰符的检查 
		field.setAccessible(true);
		if(field.getType().getSimpleName().toLowerCase().equals("date")){
			field.set(obj, DateUtils.parseDateTimeStamp(value));		
		}else if(field.getType().getSimpleName().toLowerCase().equals("short")){
			field.set(obj, NumberUtils.toShort(value));
		}else if(field.getType().getSimpleName().toLowerCase().equals("int")){
			field.set(obj, NumberUtils.toInt(value));
		}else if(field.getType().getSimpleName().toLowerCase().equals("integer")){
			field.set(obj, NumberUtils.createInteger(value));
		}else if(field.getType().getSimpleName().toLowerCase().equals("double")){
			field.set(obj, NumberUtils.createDouble(value));
		}else if(field.getType().getSimpleName().toLowerCase().equals("float")){
			field.set(obj, NumberUtils.createFloat(value));
		}else if(field.getType().getSimpleName().toLowerCase().equals("long")){
			field.set(obj, NumberUtils.createLong(value));
		}else {
			field.set(obj, value);
		}
		
//		
//		else if(field.getType().getName().toLowerCase().contains("integer")){
//			field.set(obj, NumberUtils.createInteger(value));
//		}else if(field.getType().getName().toLowerCase().contains("byte")){
//			field.set(obj, NumberUtils.toByte(value));
//		}else if(field.getType().getName().toLowerCase().contains("short")){
//			field.set(obj, value);
//		}else if(field.getType().getName().toLowerCase().contains("long")){
//			field.set(obj, NumberUtils.createLong(value));
//		}else if(field.getType().getName().toLowerCase().contains("biginteger")){
//			field.set(obj, NumberUtils.createBigInteger(value));
//		}else if(field.getType().getName().toLowerCase().contains("float")){
//			field.set(obj, NumberUtils.createFloat(value));
//		}else if(field.getType().getName().toLowerCase().contains("double")){
//			field.set(obj, NumberUtils.createDouble(value));
//		}else if(field.getType().getName().toLowerCase().contains("bigdecimal")){
//			field.set(obj, NumberUtils.createBigDecimal(value));
//		}
//		//在对象obj上读取field属性的值
//		Object val = field.get(obj);
//		field.setAccessible(false);
		return obj;
	}
	
	
	/**
	 * 
	 * @param obj 访问对象
	 * @param filedname  对象的属性
	 * @return 返回对象的属性值
	 * @throws Exception
	 */
	public static Object setFieldValue(Object obj,String filedname,Object value) throws Exception{
		//反射出类型
		Class<?> cls = obj.getClass();
		Field field = null;
		//反射出类型字段
		 try {
			 try {
				 field = cls.getDeclaredField(filedname);
			} catch (Exception e) {
				cls = cls.getSuperclass();
				field = cls.getDeclaredField(filedname);
			}
		   } catch (Exception e) {
			   e.printStackTrace();
			   System.out.println("没有这个字段："+filedname);
		   }
		if(field==null){
			return null;
		}
		//获取属性时，压制Java对访问修饰符的检查 
		field.setAccessible(true);
		
		
		if(null != value && field.getType().getSimpleName().toLowerCase().equals("short")){
			field.set(obj, NumberUtils.toShort(String.valueOf(value)));
		}else if(null != value && field.getType().getSimpleName().toLowerCase().equals("int")){
			field.set(obj, NumberUtils.toInt(String.valueOf(value)));
		}else if(null != value && field.getType().getSimpleName().toLowerCase().equals("integer")){
			field.set(obj, NumberUtils.createInteger(String.valueOf(value)));
		}else if(null != value && field.getType().getSimpleName().toLowerCase().equals("double")){
			field.set(obj, NumberUtils.createDouble(String.valueOf(value)));
		}else if(null != value && field.getType().getSimpleName().toLowerCase().equals("float")){
			field.set(obj, NumberUtils.createFloat(String.valueOf(value)));
		}else if(null != value && field.getType().getSimpleName().toLowerCase().equals("long")){
			field.set(obj, NumberUtils.createLong(String.valueOf(value)));
		}else{
			field.set(obj, value);
		}
		
		return obj;
	}
	
	/**
	 * 反射调用对象的方法
	 * @param obj 对象 
	 * @param methodName  方法名称 
	 * @param paramType 参数类型    new Class[]{int.class,double.class}
	 * @param params 参数值     new Object[]{2,3.5}
	 * @return
	 * @throws Exception 
	 */
	public static Object readObjMethod(Object obj,String methodName,Class<?>[] paramTypes,Object[] params) throws  Exception{
		//发现类型
		Class<?> cls = obj.getClass();
		//发现方法
		Method method = cls.getDeclaredMethod(methodName, paramTypes);
		//访问方法时,压制Java对访问修饰符的检查
		method.setAccessible(true);
		Object val = method.invoke(obj, params);
		return val;
	}
	
	@SuppressWarnings("unused")
	private static String underscoreName(String name) {
		StringBuilder result = new StringBuilder();
		if (name != null && name.length() > 0) {
			result.append(name.substring(0, 1).toLowerCase());
			for (int i = 1; i < name.length(); i++) {
				String s = name.substring(i, i + 1);
				if (s.equals(s.toUpperCase())) {
					result.append("_");
					result.append(s.toLowerCase());
				}
				else {
					result.append(s);
				}
			}
		}
		return result.toString();
	}
	
	/**
	 * 存放反射属性缓存，提高性能
	 * @param obj
	 * @param filedname
	 * @return
	 */
	private static Field setFiledCache(Object obj,String filedname){
		Field field = null;
		//添加filedMap性能提升一倍
		if(null != filedMap.get(filedname)){
			field = (Field) filedMap.get(filedname);
		}else{
			//反射出类型
			Class<?> cls = obj.getClass();
			//反射出类型字段
			 try {
				 try {
					 field = cls.getDeclaredField(filedname);
				} catch (Exception e) {
					cls = cls.getSuperclass();
					field = cls.getDeclaredField(filedname);
				}
			   } catch (Exception e) {
				   e.printStackTrace();
				   System.out.println("没有这个字段："+filedname);
			   }
			 filedMap.put(filedname, field);
		}
		return field;
	}
	
	/**
	 * 通过PropertyDescriptor类获取属性值
	 * @param cls 
	 * @param filedname 
	 * @return
	 */
	public static Object getFieldValueByPropertyDescriptor(Object obj,String filedname){
		Object value = null;
		try {
			Class<?> cls = obj.getClass();
			Field field = null;
			//反射出类型字段
			 try {
				 try {
					 field = cls.getDeclaredField(filedname);
				} catch (Exception e) {
					cls = cls.getSuperclass();
					field = cls.getDeclaredField(filedname);
				}
			   } catch (Exception e) {
				   e.printStackTrace();
				   System.out.println("没有这个字段："+filedname);
			   }
			
			//获取属性时，压制Java对访问修饰符的检查 
			field.setAccessible(true);
			PropertyDescriptor pd = new PropertyDescriptor(filedname, cls);//获取 cls 类型中的 filedname 的属性描述器  
			Method rm = pd.getReadMethod();
			Method wm = pd.getWriteMethod();
			value = rm.invoke(obj);
			if(null != field){
				if(field.getType().getName().toLowerCase().contains("date") &&
						null == value){
					wm.invoke(obj, new Date());
				}
				if(field.getType().getName().toLowerCase().contains("string") &&
						filedname.equals("id") && null == value){
					wm.invoke(obj, UUIDGenerator.generate());
				}
				value = rm.invoke(obj);
			}
			
		}  catch (Exception e) {
			LogUtil.error("获取 cls 类型中的 filedname 的属性描述器失败", e);
		}
		
		return value;
		
	}
	
	public static <T> PropertyDescriptor getPropertyDescriptor(Class<T> clazz, String propertyName) {  
        StringBuffer sb = new StringBuffer();//构建一个可变字符串用来构建方法名称  
        Method setMethod = null;  
        Method getMethod = null;  
        PropertyDescriptor pd = null;  
        try {  
            Field f = clazz.getDeclaredField(propertyName);//根据字段名来获取字段  
            if (f!= null) {  
                //构建方法的后缀  
               String methodEnd = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);  
               sb.append("set" + methodEnd);//构建set方法  
               setMethod = clazz.getDeclaredMethod(sb.toString(), new Class[]{ f.getType() });  
               sb.delete(0, sb.length());//清空整个可变字符串  
               sb.append("get" + methodEnd);//构建get方法  
               //构建get 方法  
               getMethod = clazz.getDeclaredMethod(sb.toString(), new Class[]{ });  
               //构建一个属性描述器 把对应属性 propertyName 的 get 和 set 方法保存到属性描述器中  
               pd = new PropertyDescriptor(propertyName, getMethod, setMethod);  
            }  
        } catch (Exception ex) {  
                ex.printStackTrace();  
        }  
      
        return pd;  
    }  
	
	@SuppressWarnings("unused")
	private static Object getReadMethodValue(Method readMethod, Object entity) {
        if (readMethod == null) {
            return null;
        }
        try {
            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                readMethod.setAccessible(true);
            }
            return readMethod.invoke(entity);
        } catch (Exception e) {
            LogUtil.error("获取属性值失败", e);
        }
		return null;
    }
	
	public static void main(String[] args) {
		long a=System.currentTimeMillis();
		//获取属性信息
//        BeanInfo beanInfo = ClassReflectUtil.getBeanInfo(p.getClass());
//        PropertyDescriptor[] pds = beanInfo.getPropertyDescriptors();
//        for (PropertyDescriptor pd : pds) {
//            Object value = getReadMethodValue(pd.getReadMethod(), p);
//            System.out.println("ClassReflectUtil.main()"+value+" name "+pd.getName());
//        }
//		 for(int i=0;i<5000;i++){
////			 getFieldValueByPropertyDescriptor(p,"id");
//			 try {
//				getFieldValue(p, "id");
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
		
		long b=System.currentTimeMillis();
	    System.out.println(b-a+" ======== ");
//		try {
////			reflect(p);
//			List<String> fileds = getFields(p);
//			StringBuffer s = new StringBuffer();
//			s.append("select ");
//			for (int i = 0; i < fileds.size(); i++) {
//				s.append(fileds.get(i));
//				if (i < fileds.size() - 1) {
//					s.append(",");
//				}
//			}
//			s.append(" from ");
//			
//			ClassReflectUtil.reflect("org.takinframework.web.system.entity.SysDepart");
////			getTablesNameFromEntity(p);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		List<String> f = ClassReflectUtil.getFields(SysFunction.class);
//		StringBuilder b = new StringBuilder();
//		for (String field : f) {
//			b.append(field);
//			b.append(",");
//		}
//		String c = b.toString();
//		String p[] = "createBy,createName,createDate,updateDate,page".split(",");
//		for (String string : p) {
//			String d = string+",";
//			if(c.contains(d)){
//				c = c.replace(d, "");
//			}
//		}
		//CodeUtil.getTablesNameFromClassName(SysFunction.class.getSimpleName()
		
		
	}
}

@SuppressWarnings("serial")
class person extends BaseEntity{
	private String name;
	private int age;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getAge() {
		return age;
	}

	public void setAge(int age) {
		this.age = age;
	}
	
	
}

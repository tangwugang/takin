package org.takinframework.core.common.model.json;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 
 * @author twg
 *
 */
@SuppressWarnings("rawtypes")
public class DataContext {
	
	private Object obj;//对象
	
	private List objList1 = Lists.newArrayList();
	private List objList2 = Lists.newArrayList();
	public Object getObj() {
		return obj;
	}
	public void setObj(Object obj) {
		this.obj = obj;
	}
	public List getObjList1() {
		return objList1;
	}
	public void setObjList1(List objList1) {
		this.objList1 = objList1;
	}
	public List getObjList2() {
		return objList2;
	}
	public void setObjList2(List objList2) {
		this.objList2 = objList2;
	}
	
	
	
	

}

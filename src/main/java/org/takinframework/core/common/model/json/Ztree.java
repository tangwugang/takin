package org.takinframework.core.common.model.json;

import java.util.List;
import com.google.common.collect.Lists;

/**
 * ztree返回的JSON格式
 * @author twg
 *
 */
public class Ztree {
	private String id;
	private String pId;
	private String name;
	private boolean checked = false;
	private List obj = Lists.newArrayList();//其他信息
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getpId() {
		return pId;
	}
	public void setpId(String pId) {
		this.pId = pId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public boolean isChecked() {
		return checked;
	}
	public void setChecked(boolean checked) {
		this.checked = checked;
	}
	public List getObj() {
		return obj;
	}
	public void setObj(List obj) {
		this.obj = obj;
	}
	
	
	
	
	

}

package org.takinframework.core.model;
import java.io.Serializable;
import java.util.Date;

import org.takinframework.core.util.Page;
import org.takinframework.core.util.UUIDGenerator;

/**
 * 实体模型基类
 * @author twg
 *
 */
@SuppressWarnings("serial")
public class BaseEntity implements Serializable {
	public String id;//主键
	public String createBy;//创建者
	public String createName;//创建者名
	public Date createDate;//创建时间
	public Date updateDate;//更新时间
	public String updateBy;//更新者
	public String updateName;//更新者名
	@SuppressWarnings("rawtypes")
	private Page page=new Page();
	
	public BaseEntity(){
	}
	
	
	public BaseEntity(String updateBy,String updateName,String createBy,String createName,Date updateDate,Date createDate ){
		this.updateBy = updateBy;
		this.updateName = updateName;
		this.id= UUIDGenerator.generate();
		this.updateDate = updateDate;
		this.createBy = createBy;
		this.createName = createName;
		this.createDate = createDate;
	}
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}
	public String getCreateBy() {
		return createBy;
	}
	
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}

	public String getCreateName() {
		return createName;
	}

	public void setCreateName(String createName) {
		this.createName = createName;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public Date getUpdateDate() {
		return updateDate;
	}

	public void setUpdateDate(Date updateDate) {
		this.updateDate = updateDate;
	}

	public String getUpdateBy() {
		return updateBy;
	}

	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}

	public String getUpdateName() {
		return updateName;
	}

	public void setUpdateName(String updateName) {
		this.updateName = updateName;
	}

	@SuppressWarnings("rawtypes")
	public Page getPage() {
		return page;
	}

	@SuppressWarnings("rawtypes")
	public void setPage(Page page) {
		this.page = page;
	}


	/**
	  * 状态枚举
	  *
	  */
	 public static enum STATE {
		 	ONLINE(3, "在线"), OFFLINE(2,"离线"),DISABLE(0,"禁用"),ENABLED(1,"可用");
			public int key;
			public String value;
			private STATE(int key, String value) {
				this.key = key;
				this.value = value;
			}
			public static STATE get(int key) {
				STATE[] values = STATE.values();
				for (STATE object : values) {
					if (object.key == key) {
						return object;
					}
				}
				return null;
			}
		}
	 	
	 	/**
	 	 * 删除枚举
	 	 * @author lu
	 	 *
	 	 */
	 	public static enum DELETED {
			NO(0, "未删除"), YES(1,"已删除");
			public int key;
			public String value;
			private DELETED(int key, String value) {
				this.key = key;
				this.value = value;
			}
			public static DELETED get(int key) {
				DELETED[] values = DELETED.values();
				for (DELETED object : values) {
					if (object.key == key) {
						return object;
					}
				}
				return null;
			}
		}

}

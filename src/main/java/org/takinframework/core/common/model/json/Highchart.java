package org.takinframework.core.common.model.json;
import java.util.List;
/**
 *  统计报表模型
 * @author twg
 * @param <T>
 *
 */
public class Highchart<T> {
private String name;
private String type;//类型
private List<T> data;//数据
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public List<T> getData() {
	return data;
}
public void setData(List<T> data) {
	this.data = data;
}
public String getType() {
	return type;
}
public void setType(String type) {
	this.type = type;
}

}

package org.takinframework.core.quartz;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * 自定义触发器时间
 * @author twg
 *
 */
public class TriggerTime {
	/**
	 * 时间类型
	 * 1:制定【某个日期-时间点】执行一次
	 * 2:制定每天【每分钟】执行一次
	 * 3:制定每天【小时:分钟】执行一次
	 * 4:制定每周【星期[|]小时:分钟】执行一次
	 * 5:制定每月【几号[|]小时:分钟】执行一次
	 * 6:直接填写quartz表达式如0 15 10 15 * ?
	 */
	private int timeType;
	/**
	 * 时间间隔
	 * 1:2000-12-2 12:00:00
	 * 2:3
	 * 3:13:45
	 * 4：MON,TUE[|]12:00
	 * 5:1,23,25[|]12:00
	 * 6:0 15 10 15 * ?
	 */
	private String timeInterval;
	/**
	 * 优先级默认5
	 */
	private int priority = 5;
	/**
	 * 存放时间的列表如：[{timeType:1,timeInterval:"2014-6-20 13:00:00"},{timeType:1,timeInterval:"2014-6-20 14:00:00"},{timeType:1,timeInterval:"2014-6-20 15:00:00"},{timeType:1,timeInterval:"2014-6-20 16:00:00"}]
	 */
	private List<TriggerTime> triggerTimeList = Lists.newLinkedList();
	
	public int getTimeType() {
		return timeType;
	}
	public void setTimeType(int timeType) {
		this.timeType = timeType;
	}
	public String getTimeInterval() {
		return timeInterval;
	}
	public void setTimeInterval(String timeInterval) {
		this.timeInterval = timeInterval;
	}
	public int getPriority() {
		return priority;
	}
	public void setPriority(int priority) {
		this.priority = priority;
	}
	public List<TriggerTime> getTriggerTimeList() {
		return triggerTimeList;
	}
	public void setTriggerTimeList(List<TriggerTime> triggerTimeList) {
		this.triggerTimeList = triggerTimeList;
	}
	
	
	
	

}

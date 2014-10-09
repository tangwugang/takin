package org.takinframework.core.quartz.listener;

import java.util.Date;

import org.quartz.JobExecutionContext;
import org.quartz.Trigger;
import org.quartz.Trigger.CompletedExecutionInstruction;
import org.quartz.listeners.TriggerListenerSupport;
import org.takinframework.core.util.DateUtils;
import org.takinframework.core.util.LogUtil;

/**
 * 触发器监听器
 * @author twg
 *
 */
public class MyTriggerListener extends TriggerListenerSupport {
	
	private String name;
	
	public MyTriggerListener(String name){
		this.name = name;
	}

	
	public String getName() {
		return name;
	}
	
	
	
	public void triggerFired(Trigger trigger, JobExecutionContext context) {
		System.out.println("MyTriggerListener.triggerFired()===触发器开始工作");
		System.out.println("触发器参数：优先级"+trigger.getPriority()+"===触发器描述："+trigger.getDescription());
		System.out.println("触发器参数：触发开始时间"+trigger.getStartTime());
		LogUtil.info("触发器参数：触发开始时间"+DateUtils.formatDateTime(trigger.getStartTime())+"===触发器描述："+trigger.getDescription());
	}
	
	
	public void triggerMisfired(Trigger trigger) {
		System.out.println("MyTriggerListener.triggerMisfired()===触发器triggerMisfired");
		
	}
	public void triggerComplete(Trigger trigger, JobExecutionContext context, 
			CompletedExecutionInstruction triggerInstructionCode) {
		LogUtil.info("触发器参数：触发结束时间"+DateUtils.formatDateTime(new Date())+"===触发器结束工作");
	};

}

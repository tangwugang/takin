package org.takinframework.core.quartz.listener;


import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.SchedulerException;
import org.quartz.SchedulerMetaData;
import org.quartz.listeners.JobListenerSupport;
import org.takinframework.core.util.LogUtil;


/**
 * 任务监听器
 * @author twg
 *
 */
public class MyJobListener extends JobListenerSupport {
	
	private String name;
	
	public MyJobListener(String name){
		this.name = name;
	}

	
	public String getName() {
		return name;
	}
	
	
	
	public void jobToBeExecuted(JobExecutionContext context) {
		JobDataMap jobDataMap = context.getJobDetail().getJobDataMap();
		String keys[] = jobDataMap.getKeys();
		for (String string : keys) {
			System.out.println("jobDateMap : key =="+string+ "value =="+jobDataMap.get(string));
		}
		try {
			SchedulerMetaData metaData = context.getScheduler().getMetaData();
			System.err.println("------任务监听器开始工作 ------执行了 "+metaData.getNumberOfJobsExecuted()+" 个");
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		
		LogUtil.info("------任务监听器开始工作 ------");
		
	}
	
	
	
	public void jobExecutionVetoed(JobExecutionContext context) {
		super.jobExecutionVetoed(context);
	}
	
	
	public void jobWasExecuted(JobExecutionContext context,
			JobExecutionException jobException) {
		try {
			SchedulerMetaData metaData = context.getScheduler().getMetaData();
			System.err.println("------任务监听器结束工作: ------执行了 "+metaData.getNumberOfJobsExecuted()+" 个");
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		System.err.println("----任务监听器结束工作: "+context.getJobDetail().getDescription());
		LogUtil.info("----任务监听器结束工作: "+context.getJobDetail().getDescription());
	}

}

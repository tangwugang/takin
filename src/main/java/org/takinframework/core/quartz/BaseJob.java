package org.takinframework.core.quartz;

import org.quartz.DisallowConcurrentExecution;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * 定时任务执行器基类
 * @author twg
 *
 */
@DisallowConcurrentExecution
public abstract class BaseJob implements Job {
	
	public abstract void executeJob(JobExecutionContext paramJobExecutionContext)
			throws JobExecutionException;
	
	public void execute(JobExecutionContext context) throws JobExecutionException{
	    executeJob(context);
	}

}

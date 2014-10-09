package org.takinframework.core.quartz;

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.quartz.CalendarIntervalScheduleBuilder;
import org.quartz.CronScheduleBuilder;
import org.quartz.Job;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.ScheduleBuilder;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.StdSchedulerFactory;
import org.quartz.impl.matchers.GroupMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.takinframework.core.quartz.listener.MyJobListener;
import org.takinframework.core.quartz.listener.MyTriggerListener;
import org.takinframework.core.util.DateUtils;
import org.takinframework.core.util.LogUtil;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;



/**
 * 定时任务管理器
 * 
 * @author twg
 * 
 */
public class QuartzManager {
	@Autowired
	private static Scheduler sched;

	private static final String JOB_GROUP_NAME = "group1";
	private static final String TRIGGER_GROUP_NAME = "trigger1";
	private static Map<String, String> mapWeek = Maps.newConcurrentMap();
	
	static{
		if(null == sched){
			try {
				sched = new StdSchedulerFactory().getScheduler();
				LogUtil.info("定时器实例化成功");
			} catch (SchedulerException e) {
				LogUtil.error("定时器实例化失败", e);
			}
		}
		
		mapWeek.put("MON", "星期一");
	    mapWeek.put("TUE", "星期二");
	    mapWeek.put("WED", "星期三");
	    mapWeek.put("THU", "星期四");
	    mapWeek.put("FRI", "星期五");
	    mapWeek.put("SAT", "星期六");
	    mapWeek.put("SUN", "星期日");
	}
	
	/**
	 * 获取周期
	 * @param week 周期
	 * @return
	 */
	private static String getWeek(String week){
	    String[] aryWeek = week.split(",");
	    String str = "";
	    for (int i = 0; i < aryWeek.length; i++) {
	        str += (String)mapWeek.get(aryWeek[i]) + ",";
	    }
	    str = str.substring(0, str.length()-1);
	    return str;
	  }
	/**
	 * 获取那天
	 * @param day
	 * @return
	 */
	private static String getDay(String day){
	    String[] aryDay = day.split(",");
	    int len = aryDay.length;
	    String str = "";
	    for (int i = 0; i < len; i++) {
	      String tmp = aryDay[i];
	      tmp = tmp.equals("L") ? "最后一天" : tmp;
	        str += tmp + ",";
	    }
	    str = str.substring(0,str.length()-1);
	    return str;
	  }
	
	private static void addJobData(JobBuilder jobBuilder,List<TriggerTime> triggerTime){
		if(triggerTime.size()>0){
			JobDataMap jobDataMap = new JobDataMap();
			for (int i = 0; i < triggerTime.size(); i++) {
				jobDataMap.put("jobKey"+i, triggerTime.get(i));
			}
			jobBuilder.usingJobData(jobDataMap);
		}
	}
	
	@SuppressWarnings("rawtypes")
	private static void addJobData(JobBuilder jobBuilder,List<TriggerTime> triggerTime,List obj){
		if(triggerTime.size()>0){
			JobDataMap jobDataMap = new JobDataMap();
			for (int i = 0; i < triggerTime.size(); i++) {
				jobDataMap.put("jobKey"+i, triggerTime.get(i));
			}
			jobDataMap.put("paramObj", obj);
			jobBuilder.usingJobData(jobDataMap);
		}
	}
	
	/**
	 * 添加触发器时间
	 * @param jobName 任务名
	 * @param triggerName 触发器名
	 * @param time 时间设置，参考quartz说明文档
	 * @throws ParseException 
	 * @throws SchedulerException 
	 */
	public static void addTrigger(JobDetail jobDetail,String jobName,String triggerName,TriggerTime time) 
			throws ParseException, SchedulerException{
		TriggerBuilder<Trigger> tr = TriggerBuilder.newTrigger();
		tr.withIdentity(triggerName, TRIGGER_GROUP_NAME);
		setTrigBuilder(time,tr);
		tr.forJob(jobName, JOB_GROUP_NAME);
		sched.addJob(jobDetail, true);
		sched.scheduleJob(tr.build());
	}
	
	public static void addTrigger(JobDetail jobDetail,String jobName,String triggerName,List<TriggerTime> triggerTime) 
			throws ParseException, SchedulerException{
		for (int i = 0; i < triggerTime.size(); i++) {
			TriggerBuilder<Trigger> tr = TriggerBuilder.newTrigger();
			tr.withIdentity(triggerName+i+"_priority"+triggerTime.get(i).getPriority(), TRIGGER_GROUP_NAME);
			setTrigBuilder(triggerTime.get(i),tr);
			tr.withPriority(triggerTime.get(i).getPriority());
			tr.forJob(jobName, JOB_GROUP_NAME);
			sched.addJob(jobDetail, true);
			sched.scheduleJob(tr.build());
		}
		
	}
	
	/**
	 * 添加触发器时间
	 * @param jobName 任务名
	 * @param jobGroupName 任务组名
	 * @param triggerName 触发器名
	 * @param triggerGroupName 触发器组名
	 * @param time 时间设置，参考quartz说明文档
	 * @throws ParseException
	 * @throws SchedulerException
	 */
	public static void addTrigger(JobDetail jobDetail,String jobName,String jobGroupName,String triggerName, 
			String triggerGroupName,TriggerTime time) 
			throws ParseException, SchedulerException{
		TriggerBuilder<Trigger> tr = TriggerBuilder.newTrigger();
		tr.withIdentity(triggerName, triggerGroupName);
		setTrigBuilder(time,tr);
		tr.forJob(jobName, jobGroupName);
		sched.addJob(jobDetail, true);
		sched.scheduleJob(tr.build());
	}
	
	
	/**
	 * 
	 * @param time 触发器时间
	 * @param tb 触发器
	 * @throws ParseException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static void setTrigBuilder(TriggerTime time, TriggerBuilder<Trigger> tr) 
			throws ParseException {
		int timeType = time.getTimeType();
		String timeInterval = time.getTimeInterval();
		switch (timeType) {
		case 1:
			Date dateTime = DateUtils.parseDateTime(timeInterval);
			tr.startAt(dateTime);//触发时间开始
			tr.withDescription("执行一次,执行时间:"+timeInterval);
			break;
		case 2:
			int minute = Integer.parseInt(timeInterval);
			tr.startNow();
			tr.withSchedule(CalendarIntervalScheduleBuilder.calendarIntervalSchedule().withIntervalInMinutes(minute));
			tr.withDescription("每:" + minute + "分钟执行!");
			break;
			
		case 3:
			String[] aryTime = timeInterval.split(":");
	        int hour = Integer.parseInt(aryTime[0]);
	        int m = Integer.parseInt(aryTime[1]);
	        ScheduleBuilder s3 = CronScheduleBuilder.dailyAtHourAndMinute(hour, m);
	        tr.startNow();
	        tr.withSchedule(s3);
	        tr.withDescription("每天：" + hour + ":" + m + "执行!");
			break;
		case 4:
			String[] aryExpression4 = timeInterval.split("[|]");
		    String week = aryExpression4[0];
		    String[] aryTime1 = aryExpression4[1].split(":");
		    String h1 = aryTime1[0];
		    String m1 = aryTime1[1];
		    String cronExperssion4 = "0 " + m1 + " " + h1 + " ? * " + week;
		    ScheduleBuilder s4 = CronScheduleBuilder.cronSchedule(cronExperssion4);
		    tr.startNow();
		    tr.withSchedule(s4);
		    String weekName = getWeek(week);
		    tr.withDescription("每周：" + weekName + "," + h1 + ":" + m1 + "执行!");
			break;
		case 5:
			String[] aryExpression5 = timeInterval.split("[|]");
		    String day = aryExpression5[0];
		    String[] aryTime2 = aryExpression5[1].split(":");
		    String h2 = aryTime2[0];
		    String m2 = aryTime2[1];
		    String cronExperssion1 = "0 " + m2 + " " + h2 + " " + day + " * ?";
		    ScheduleBuilder s5 = CronScheduleBuilder.cronSchedule(cronExperssion1);
		    tr.startNow();
		    tr.withSchedule(s5);
		    String dayName = getDay(day);
		    tr.withDescription("每月:" + dayName + "," + h2 + ":" + m2 + "执行!");
		    break;
		case 6:
			ScheduleBuilder sb6 = CronScheduleBuilder.cronSchedule(timeInterval);
		    tr.startNow();
		    tr.withSchedule(sb6);
		    tr.withDescription("quartz表达式(如0 15 10 15 * ?):" + timeInterval);
		    break;
		default:
			break;
		}
		
	}

	/**
	 * 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名,并执行(sched.start())
	 * @param jobName 任务名
	 * @param job 实现的Job类的实例
	 * @param time 时间设置，参考quartz说明文档
	 * @param description 任务描述
	 * @return
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public static boolean addJob(String jobName, Job job, TriggerTime time,String description)
			throws SchedulerException, ParseException {
		try {
			JobDetail jobDetail = JobBuilder.newJob(job.getClass())// job是我实现的Job类
					.withIdentity(jobName, JOB_GROUP_NAME)// 可以给该JobDetail起一个id，便于之后的检索
					.withDescription(description)
//					.requestRecovery() // 执行中应用发生故障，需要重新执行(默认没有开启“requestRecovery”。当确认业务中允许一次任务执行两次的情况下，可以开启该选项，则任务肯定不会因为应用停止而漏调用，但缺点就是，有可能会重复调用。)
					.storeDurably() // 即使没有Trigger关联时，也不需要删除该JobDetail
					.build();// 任务名，任务组，任务执行类
			
			// 触发器
			addTrigger(jobDetail,jobName, jobName+"_trigger", time);
			sched.addJob(jobDetail, true);
			return true;
		} catch (Exception e) {
			LogUtil.error("添加一个定时任务失败", e);
			
		}finally{
			// 启动
			if (!sched.isShutdown())
				sched.start();
		}
		return false;
	}
	
	/**
	 * 添加一个定时任务，使用默认的任务组名，触发器名，触发器组名,并执行(sched.start())
	 * @param <T>
	 * @param jobName 任务名
	 * @param cls 实现的Job类的class
	 * @param time 时间设置，参考quartz说明文档
	 * @param description 任务描述
	 * @return
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public static  boolean addJob(String jobName, Class<? extends Job> cls, TriggerTime time,String description)
			throws SchedulerException, ParseException {
		try {
			JobDetail jobDetail = JobBuilder.newJob(cls)// job是我实现的Job类
					.withIdentity(jobName, JOB_GROUP_NAME)// 可以给该JobDetail起一个id，便于之后的检索
					.withDescription(description)
//					.requestRecovery() // 执行中应用发生故障，需要重新执行(默认没有开启“requestRecovery”。当确认业务中允许一次任务执行两次的情况下，可以开启该选项，则任务肯定不会因为应用停止而漏调用，但缺点就是，有可能会重复调用。)
					.storeDurably() // 即使没有Trigger关联时，也不需要删除该JobDetail
					.build();// 任务名，任务组，任务执行类
			
			// 触发器
			addTrigger(jobDetail,jobName, jobName+"_trigger", time);
			//创建触发器监听器，记录触发器参数信息
			sched.getListenerManager().addTriggerListener(new MyTriggerListener(jobName+"触发器监听"), GroupMatcher.anyTriggerGroup());
			//创建任务监听器,用来保存日志等
			sched.getListenerManager().addJobListener(new MyJobListener(jobName+"任务监听"), GroupMatcher.anyJobGroup());
			return true;
		} catch (Exception e) {
			LogUtil.error("添加一个定时任务失败", e);
			
		}finally{
			// 启动
			if (!sched.isShutdown())
				sched.start();
		}
		return false;
	}
	
	/**
	 * 添加任务
	 * @param jobName 任务名
	 * @param cls 执行类
	 * @param triggerTime 时间
	 * @param description 任务描述
	 * @return
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public static boolean addJob(String jobName, Class<? extends Job> cls,List<TriggerTime> triggerTime,String description)
			throws SchedulerException, ParseException {
		try {
			JobBuilder jobBuilder = JobBuilder.newJob(cls);
			jobBuilder.withIdentity(jobName, JOB_GROUP_NAME);// 可以给该JobDetail起一个id，便于之后的检索
			jobBuilder.withDescription(description);
			addJobData(jobBuilder, triggerTime);
			jobBuilder.storeDurably();// 即使没有Trigger关联时，也不需要删除该JobDetail
			JobDetail jobDetail = jobBuilder.build();
//			JobDetail jobDetail = jobBuilder.withIdentity(jobName, JOB_GROUP_NAME)// 可以给该JobDetail起一个id，便于之后的检索
//					.withDescription(description)
////					.requestRecovery() // 执行中应用发生故障，需要重新执行(默认没有开启“requestRecovery”。当确认业务中允许一次任务执行两次的情况下，可以开启该选项，则任务肯定不会因为应用停止而漏调用，但缺点就是，有可能会重复调用。)
//					.storeDurably() // 即使没有Trigger关联时，也不需要删除该JobDetail
//					.build();// 任务名，任务组，任务执行类
			if(!isTriggerExists(jobName+"_trigger", triggerTime)){
				// 触发器
				addTrigger(jobDetail,jobName, jobName+"_trigger", triggerTime);
			}
			//创建触发器监听器，记录触发器参数信息
			sched.getListenerManager().addTriggerListener(new MyTriggerListener(jobName+"触发器监听"), GroupMatcher.anyTriggerGroup());
			//创建任务监听器,用来保存日志等
			sched.getListenerManager().addJobListener(new MyJobListener(jobName+"任务监听"), GroupMatcher.anyJobGroup());
			LogUtil.info("添加任务名为【"+jobName+"】成功");
			return true;
		} catch (Exception e) {
			LogUtil.error("添加一个定时任务失败", e);
		}finally{
			// 启动
			if (!sched.isShutdown())
				sched.start();
		}
		return false;
	}
	
	/**
	 * 添加任务
	 * @param jobName 任务名
	 * @param cls 执行类
	 * @param triggerTime 时间
	 * @param description 任务描述
	 * @param obj 持久化的参数
	 * @return
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	@SuppressWarnings("rawtypes")
	public static boolean addJob(String jobName, Class<? extends Job> cls,List<TriggerTime> triggerTime,String description,List obj)
			throws SchedulerException, ParseException {
		try {
			JobBuilder jobBuilder = JobBuilder.newJob(cls);
			jobBuilder.withIdentity(jobName, JOB_GROUP_NAME);// 可以给该JobDetail起一个id，便于之后的检索
			jobBuilder.withDescription(description);
			addJobData(jobBuilder, triggerTime,obj);//给任务添加参数
			jobBuilder.storeDurably();// 即使没有Trigger关联时，也不需要删除该JobDetail
			JobDetail jobDetail = jobBuilder.build();
//			JobDetail jobDetail = jobBuilder.withIdentity(jobName, JOB_GROUP_NAME)// 可以给该JobDetail起一个id，便于之后的检索
//					.withDescription(description)
////					.requestRecovery() // 执行中应用发生故障，需要重新执行(默认没有开启“requestRecovery”。当确认业务中允许一次任务执行两次的情况下，可以开启该选项，则任务肯定不会因为应用停止而漏调用，但缺点就是，有可能会重复调用。)
//					.storeDurably() // 即使没有Trigger关联时，也不需要删除该JobDetail
//					.build();// 任务名，任务组，任务执行类
			if(!isTriggerExists(jobName+"_trigger", triggerTime)){
				// 触发器
				addTrigger(jobDetail,jobName, jobName+"_trigger", triggerTime);
			}
			//创建触发器监听器，记录触发器参数信息
			sched.getListenerManager().addTriggerListener(new MyTriggerListener(jobName+"触发器监听"), GroupMatcher.anyTriggerGroup());
			//创建任务监听器,用来保存日志等
			sched.getListenerManager().addJobListener(new MyJobListener(jobName+"任务监听"), GroupMatcher.anyJobGroup());
			LogUtil.info("添加任务名为【"+jobName+"】成功");
			return true;
		} catch (Exception e) {
			LogUtil.error("添加一个定时任务失败", e);
		}finally{
			// 启动
			if (!sched.isShutdown())
				sched.start();
		}
		return false;
	}
	
	

	/**
	 * 添加一个定时任务,并执行(sched.start())
	 * 
	 * @param jobName
	 *            任务名
	 * @param jobGroupName
	 *            任务组名
	 * @param triggerName
	 *            触发器名
	 * @param triggerGroupName
	 *            触发器组名
	 * @param job
	 *            任务
	 * @param time
	 *            时间设置，参考quartz说明文档
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	public static boolean addJob(String jobName, String jobGroupName,String triggerName, 
			String triggerGroupName, Job job, TriggerTime time,String description)
			throws SchedulerException, ParseException {
		try {
			JobDetail jobDetail = JobBuilder.newJob(job.getClass())// job是我实现的Job类
					.withIdentity(jobName, jobGroupName)// 可以给该JobDetail起一个id，便于之后的检索
					.withDescription(description)
//					.requestRecovery() // 执行中应用发生故障，需要重新执行(默认没有开启“requestRecovery”。当确认业务中允许一次任务执行两次的情况下，可以开启该选项，则任务肯定不会因为应用停止而漏调用，但缺点就是，有可能会重复调用。)
					.storeDurably() // 即使没有Trigger关联时，也不需要删除该JobDetail
					.build();// 任务名，任务组，任务执行类
			// 触发器
			addTrigger(jobDetail,jobName,jobGroupName,triggerName, triggerGroupName, time);
			//创建任务监听器,用来保存日志等
			sched.getListenerManager().addJobListener(new MyJobListener(jobName+"任务监听"), GroupMatcher.anyJobGroup());
			return true;
		} catch (Exception e) {
			LogUtil.error("添加一个定时任务失败", e);
		}finally{
			// 启动
			if (!sched.isShutdown())
				sched.start();
		}
		return false;
		
		
	}
	
	/**
	 * 判断触发器名是否存在(默认触发器组名)
	 * @param triggerName
	 * @return
	 * @throws SchedulerException 
	 */
	@SuppressWarnings("unused")
	private static boolean isTriggerExists(String triggerName,List<TriggerTime> triggerTime ) 
			throws SchedulerException{
		for (int i = 0; i < triggerTime.size(); i++) {
			TriggerKey triggerKey = new TriggerKey(triggerName+i+"_priority"+triggerTime.get(i).getPriority(), TRIGGER_GROUP_NAME);
		    return sched.checkExists(triggerKey);
		}
		return false;
	}
	/**
	 * 判断触发器名是否存在(默认触发器组名)
	 * @param triggerName
	 * @return
	 * @throws SchedulerException 
	 */
	private static boolean isTriggerExists(String triggerName) 
			throws SchedulerException{
		TriggerKey triggerKey = new TriggerKey(triggerName, TRIGGER_GROUP_NAME);
	    return sched.checkExists(triggerKey);
	}
	/**
	 * 判断触发器名是否存在
	 * @param triggerName 触发器名
	 * @param triggerGroupName 触发器组名
	 * @return
	 * @throws SchedulerException
	 */
	private static boolean isTriggerExists(String triggerName,String triggerGroupName) 
			throws SchedulerException{
		TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroupName);
	    return sched.checkExists(triggerKey);
		
	}
	/**
	 * 获取触发器(默认触发器组名)
	 * @param triggerName 触发器名
	 * @return
	 * @throws SchedulerException
	 */
	private static Trigger getTrigger(String triggerName) 
			throws SchedulerException{
		TriggerKey triggerKey = new TriggerKey(triggerName, TRIGGER_GROUP_NAME);
		Trigger trigger = sched.getTrigger(triggerKey);
		return trigger;
	}
	/**
	 * 获取触发器
	 * @param triggerName 触发器名
	 * @param triggerGroupName 触发器组名
	 * @return
	 * @throws SchedulerException
	 */
	private static Trigger getTrigger(String triggerName,String triggerGroupName) 
			throws SchedulerException{
		TriggerKey triggerKey = new TriggerKey(triggerName, triggerGroupName);
		Trigger trigger = sched.getTrigger(triggerKey);
		return trigger;
	}

	/**
	 * 修改一个任务的触发时间(使用默认的任务组名，触发器名，触发器组名)
	 * 
	 * @param jobName
	 * @param time
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public static void modifyJobTime(String jobName, TriggerTime time)
			throws SchedulerException, ParseException {
		if(isTriggerExists(jobName+"_trigger")){
			Trigger trigger = getTrigger(jobName+"_trigger");
			TriggerBuilder<Trigger> tb = (TriggerBuilder<Trigger>) trigger.getTriggerBuilder();
			setTrigBuilder(time, tb);
			
			//重启任务
			sched.rescheduleJob(trigger.getKey(), tb.build());
		}
	}

	/**
	 * 修改一个任务的触发时间
	 * 
	 * @param triggerName
	 * @param triggerGroupName
	 * @param time
	 * @throws SchedulerException
	 * @throws ParseException
	 */
	@SuppressWarnings("unchecked")
	public static void modifyJobTime(String triggerName,
			String triggerGroupName, TriggerTime time) throws SchedulerException,
			ParseException {
		if(isTriggerExists(triggerName,triggerGroupName)){
			Trigger trigger = getTrigger(triggerName,triggerGroupName);
			TriggerBuilder<Trigger> tb = (TriggerBuilder<Trigger>) trigger.getTriggerBuilder();
			setTrigBuilder(time, tb);
			//重启任务
			sched.rescheduleJob(trigger.getKey(), tb.build());
		}
	}
	
	/**
	 * 获取任务列表
	 * @return
	 * @throws SchedulerException
	 */
	public static List<JobDetail> getJobList() 
			throws SchedulerException{
		List<JobDetail> jds = Lists.newArrayList();
		GroupMatcher<JobKey> matcher = GroupMatcher.groupEquals(JOB_GROUP_NAME);
		Set<JobKey> jk = sched.getJobKeys(matcher);
		for (JobKey key : jk) {
			JobDetail jd = sched.getJobDetail(key);
			jds.add(jd);
		}
		return jds;
	}
	/**
	 * 根据任务名获取触发器列表
	 * @param jobName 任务名
	 * @return
	 * @throws SchedulerException
	 */
	@SuppressWarnings("unchecked")
	public static List<Trigger> getTriggersByJob(String jobName) 
			throws SchedulerException{
		return (List<Trigger>) sched.getTriggersOfJob(new JobKey(jobName, JOB_GROUP_NAME));
	}
	
	/**
	 * 根据任务名、任务组名获取触发器列表
	 * @param jobName 任务名
	 * @param jobGroupName 任务组名
	 * @return
	 * @throws SchedulerException
	 */
	@SuppressWarnings("unchecked")
	public static List<Trigger> getTriggersByJob(String jobName,String jobGroupName) 
			throws SchedulerException{
		return (List<Trigger>) sched.getTriggersOfJob(new JobKey(jobName, jobGroupName));
	}

	/**
	 * 移除一个任务(使用默认的任务组名，触发器名，触发器组名)
	 * 
	 * @param jobName
	 * @throws SchedulerException
	 */
	public static void removeJob(String jobName) throws SchedulerException {
		if(isTriggerExists(jobName+"_trigger")){
			Trigger trigger = getTrigger(jobName+"_trigger",TRIGGER_GROUP_NAME);
			sched.pauseTrigger(trigger.getKey());//停止触发器
			sched.unscheduleJob(trigger.getKey());// 移除触发器
			sched.deleteJob(new JobKey(jobName,JOB_GROUP_NAME));// 删除任务
		}else{
			sched.deleteJob(new JobKey(jobName,JOB_GROUP_NAME));// 删除任务
			LogUtil.info("移除任务名为【"+jobName+"】成功");
		}
	}

	/**
	 * 移除一个任务
	 * 
	 * @param jobName
	 * @param jobGroupName
	 * @param triggerName
	 * @param triggerGroupName
	 * @throws SchedulerException
	 */
	public static void removeJob(String jobName, String jobGroupName,
			String triggerName, String triggerGroupName)
			throws SchedulerException {
		if(isTriggerExists(triggerName,triggerGroupName)){
			Trigger trigger = getTrigger(triggerName,triggerGroupName);
			sched.pauseTrigger(trigger.getKey());//停止触发器
			sched.unscheduleJob(trigger.getKey());// 移除触发器
			sched.deleteJob(new JobKey(jobName,jobGroupName));// 删除任务
		}else{
			sched.deleteJob(new JobKey(jobName,triggerGroupName));// 删除任务
		}
	}
	
	/**
	 * 手动执行任务,默认任务组名
	 * @param jobName 任务名
	 * @throws SchedulerException
	 */
	public static void executeJob(String jobName) 
			throws SchedulerException{
		sched.triggerJob(new JobKey(jobName,JOB_GROUP_NAME));
	}
	/**
	 * 手动执行任务
	 * @param jobName 任务名
	 * @param jobGroupName 任务组名
	 * @throws SchedulerException
	 */
	public static void executeJob(String jobName,String jobGroupName) 
			throws SchedulerException{
		sched.triggerJob(new JobKey(jobName,jobGroupName));
	}

}

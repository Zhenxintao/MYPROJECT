package com.bmts.heating.service.task.method.job;

import com.bmts.heating.service.task.method.pojo.MethodName;
import com.bmts.heating.service.task.method.service.MethodClient;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.stereotype.Component;

@Component("excute_day_avg")
@Slf4j
@EnableScheduling//开启定时
public class ExcuteDayAvg implements Job {

	@Autowired
	private MethodClient methodClient;

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		excuteDayAvg();
		log.info("任务被执行");
	}

	public void excuteDayAvg(){
		//调用方法 并把对应的方法名传递进去
		MethodName methodName = new MethodName();
		methodName.setName("excute_day_avg");
		methodClient.executeMethods(methodName);
		log.info("excute_day_avg");
	}

}

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

@Component("excute_day_energy_job_source")
@Slf4j
@EnableScheduling//开启定时
public class ExcuteDayEnergyJobSource implements Job {
	@Autowired
	private MethodClient methodClient;

	@Override
	public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
		excuteDayEnergyJobSource();
		log.info("任务被执行");
	}

	//@Scheduled(cron = "0 3 0 * * ?")
	public void excuteDayEnergyJobSource(){
		//调用方法 并把对应的方法名传递进去
		/*
			excute_day_energy_job_source 每天00点03执行一次
		*/
		MethodName methodName = new MethodName();
		methodName.setName("excute_day_energy_job_source");
		methodClient.executeMethods(methodName);
		log.info("excute_day_energy_job_source");
	}
}

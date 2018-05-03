package com.elitel.quartz;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import java.util.Date;

/**
 * created by guoyanfei on 2018/3/20
 */
public class HelloTestQuartz implements Job {
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDetail detail = jobExecutionContext.getJobDetail();
        String name = detail.getJobDataMap().getString("serviceId");
        System.out.println("任务调度say hello to " + name + " at " + new Date());
    }
}

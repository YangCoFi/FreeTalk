package com.yangcofi.community.quartz;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * @ClassName AlpthaJob
 * @Description TODO
 * @Author YangC
 * @Date 2019/12/9 19:56
 **/
public class AlphaJob implements Job {
    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
//        System.out.println(Thread.currentThread().getName() + ": execute a quartz job.");
    }
}

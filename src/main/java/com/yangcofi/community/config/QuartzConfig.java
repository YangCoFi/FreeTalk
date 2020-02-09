package com.yangcofi.community.config;

import com.yangcofi.community.quartz.AlphaJob;
import com.yangcofi.community.quartz.PostScoreRefreshJob;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/**
 * @ClassName QuartzConfig
 * @Description TODO
 * @Author YangC
 * @Date 2019/12/9 20:06
 **/
//配置 -> 数据库 -> 调用
@Configuration
public class QuartzConfig {

    //FactoryBean可简化bean的实例化过程：
    //1.通过FactoryBean封装Bean的实例化过程
    //2.FactoryBean装配到Spring容器里
    //3.将FactoryBean注入给其他的Bean
    //4.该Bean得到的是FactoryBean所管理的对象实例


    //配置JobDetail
    @Bean// Configure JobDetail @Bean public JobDetailFactoryBean alphaJobDetail () {JobDetailFactoryBean factoryBean = new JobDetailFactoryBean (); factoryBean.setJobClass (AlphaJob.class); factoryBean.setName ("alphaJob"); factoryBean.setGroup ("alphaJobGroup"); // multiple Tasks can belong to a group of factoryBean.setDurability (true); // task persists factoryBean.setRequestsRecovery (true); // task sets a recoverable return factoryBean;}v
    public JobDetailFactoryBean alphaJobDetail(){
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(AlphaJob.class);
        factoryBean.setName("alphaJob");
        factoryBean.setGroup("alphaJobGroup");       //多个任务可以同属于一组
        factoryBean.setDurability(true);        //任务持久的保存
        factoryBean.setRequestsRecovery(true);  //任务设置可恢复的
        return factoryBean;
    }

    //配置Trigger,两种方式(SimpleTriggerFactoryBean,CronTriggerFactoryBean)
    @Bean
    public SimpleTriggerFactoryBean alphaTrigger(JobDetail alphaJobDetail){     //这里和alphaJobDetail()保持一致名字 因为实例多了的话 会优先按照名字注入
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(alphaJobDetail);
        factoryBean.setName("alphaTrigger");
        factoryBean.setGroup("alphaTriggerGroup");
        factoryBean.setRepeatInterval(3000);
        factoryBean.setJobDataMap(new JobDataMap());    //底层需要存储Job的一些状态 默认用JobDataMap这个对象
        return factoryBean;
    }

    //刷新帖子分数任务
    @Bean
    public JobDetailFactoryBean postScoreRefreshJobDetail(){     //这里和alphaJobDetail()保持一致名字 因为实例多了的话 会优先按照名字注入
        JobDetailFactoryBean factoryBean = new JobDetailFactoryBean();
        factoryBean.setJobClass(PostScoreRefreshJob.class);
        factoryBean.setName("postScoreRefreshJob");
        factoryBean.setGroup("communityJobGroup");
        factoryBean.setDurability(true);
        factoryBean.setRequestsRecovery(true);    //底层需要存储Job的一些状态 默认用JobDataMap这个对象
        return factoryBean;
    }

    @Bean
    public SimpleTriggerFactoryBean postScoreRefreshTrigger(JobDetail postScoreRefreshJobDetail){     //这里和alphaJobDetail()保持一致名字 因为实例多了的话 会优先按照名字注入
        SimpleTriggerFactoryBean factoryBean = new SimpleTriggerFactoryBean();
        factoryBean.setJobDetail(postScoreRefreshJobDetail);
        factoryBean.setName("postScoreRefreshTrigger");
        factoryBean.setGroup("communityTriggerGroup");
        factoryBean.setRepeatInterval(1000 * 60 * 5);
        factoryBean.setJobDataMap(new JobDataMap());    //底层需要存储Job的一些状态 默认用JobDataMap这个对象
        return factoryBean;
    }
}

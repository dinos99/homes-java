package homes.config;

import java.util.HashMap;
import java.util.Map;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import homes.batch.job.ARC000Job;

@Configuration
public class QuartzSchedulerConfig {
	/*
	@Bean
	JobDetail BJT001JobDetail() {
		return JobBuilder.newJob(BJT001Job.class).withIdentity("BJT001Job").storeDurably().build();
	}
	
	@Bean
	JobDetail BLD002JobDetail() {
		return JobBuilder.newJob(BLD002Job.class).withIdentity("BLD002Job").storeDurably().build();
	}

	@Bean
	JobDetail BLD003JobDetail() {
		return JobBuilder.newJob(BLD003Job.class).withIdentity("BLD003Job").storeDurably().build();
	}

	@Bean
	Trigger BJT001JobTrigger( JobDetail BJT001JobDetail ) {
		return TriggerBuilder.newTrigger()
				.forJob(BJT001JobDetail).withIdentity("BJT001JobTrigger")
				.withSchedule(CronScheduleBuilder.cronSchedule("0 22 19 * * ?")) 
				.build();
	}
	
	@Bean
	Trigger BLD002JobTrigger( JobDetail BLD002JobDetail ) {
		return TriggerBuilder.newTrigger()
				.forJob(BLD002JobDetail).withIdentity("BLD002JobTrigger")
				.withSchedule(CronScheduleBuilder.cronSchedule("0 * /4 1-3 11-13 * ?")) 
				.build();
	}
	
	@Bean
	Trigger BLD003JobTrigger( JobDetail BLD003JobDetail ) {
		return TriggerBuilder.newTrigger()
				.forJob(BLD003JobDetail).withIdentity("BLD003JobTrigger")
				.withSchedule(CronScheduleBuilder.cronSchedule("0 * / 4 20-9 15-20 * ?")) 
				.build();
	}
	*/
	@Bean
	JobDetail ARC000JobDetail() { 
		Map<String, Object> jbmap = new HashMap<String, Object>() ;
		jbmap.put("userno" , "900900901") ;
		jbmap.put("batchYn", "Y") ;
		JobDataMap dataMap = new JobDataMap(jbmap) ;
		return JobBuilder.newJob(ARC000Job.class)
				         .withIdentity("ARC000Job")
				         .usingJobData(dataMap)
				         .storeDurably()
				         .build();
	}
	
	@Bean
	Trigger BLD003JobTrigger( JobDetail ARC000JobDetail ) {
		return TriggerBuilder.newTrigger()
				.forJob(ARC000JobDetail)
				.withIdentity("ARC000JobDetail")
                .startNow() // 애플리케이션 시작 시 즉시 작업 실행
				.withSchedule(CronScheduleBuilder.cronSchedule("0 */10 22-23 * * ?")) 
				.build();
	}
}

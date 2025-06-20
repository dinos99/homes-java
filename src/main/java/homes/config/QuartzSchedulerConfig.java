package homes.config;

import org.springframework.context.annotation.Configuration;

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
}

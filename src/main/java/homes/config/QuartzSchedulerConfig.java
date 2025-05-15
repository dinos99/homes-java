package homes.config;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import homes.batch.job.BJT001Job;

@Configuration
public class QuartzSchedulerConfig {
	@Bean
	JobDetail BJT001obDetail() {
		return JobBuilder.newJob(BJT001Job.class)
				.withIdentity("BJT001")
				.storeDurably()
				.build();
	}
	
	@Bean
	Trigger BJT001Trigger( JobDetail jobDetail ) {
		return TriggerBuilder.newTrigger()
				.forJob(jobDetail)
				.withIdentity("BJT001")
				.withSchedule(CronScheduleBuilder.cronSchedule("0 10 13 * * ?")) // 매일 0시 50분 분에시작 
				.build();
	}

}

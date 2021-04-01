package com.sftp.client.learnkotlin.scheduler

import com.sftp.client.learnkotlin.Start
import com.sftp.client.learnkotlin.file.Load
import com.sftp.client.learnkotlin.model.Login
import com.sftp.client.learnkotlin.model.LoginSettings
import org.quartz.*
import org.quartz.impl.StdSchedulerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.scheduling.quartz.SpringBeanJobFactory;
import java.lang.Exception
import org.quartz.CronScheduleBuilder
import java.text.ParseException
import org.quartz.SchedulerException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CustomScheduler {


    fun scheduler(list: Login): Scheduler {
        val stdSchedulerFactory = StdSchedulerFactory()
        stdSchedulerFactory.initialize(ClassPathResource("quartz.properties").inputStream)

        val scheduler: Scheduler = stdSchedulerFactory.scheduler
        scheduler.setJobFactory(SpringBeanJobFactory())

        for(login in list.login){
            val job = JobBuilder.newJob(Start::class.java).build()
            job.jobDataMap["login"] = login
            val trigger = TriggerBuilder.newTrigger().forJob(job).withSchedule(getScheduleBuilder(login.scheduledTime)).build()

            try {
                scheduler.scheduleJob(job, trigger)
            } catch (e: SchedulerException) {
                e.printStackTrace()
            }
        }
        scheduler.start();
        return scheduler;
    }

    private fun getScheduleBuilder(cronExpr: String): CronScheduleBuilder? {
        try {
            return CronScheduleBuilder.cronSchedule(CronExpression(cronExpr))
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        return CronScheduleBuilder.cronSchedule(cronExpr)
    }

}

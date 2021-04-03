package com.sftp.client.learnkotlin.scheduler

import com.sftp.client.learnkotlin.model.Login
import org.quartz.*
import org.quartz.CronScheduleBuilder.cronSchedule
import org.quartz.impl.StdSchedulerFactory
import org.springframework.core.io.ClassPathResource
import org.springframework.scheduling.quartz.SpringBeanJobFactory
import org.springframework.stereotype.Component


@Component
class CustomScheduler {


    fun scheduler(list: Login): Scheduler {

        val stdSchedulerFactory = StdSchedulerFactory()
        stdSchedulerFactory.initialize(ClassPathResource("quartz.properties").inputStream)

        val scheduler: Scheduler = stdSchedulerFactory.scheduler
        scheduler.setJobFactory(SpringBeanJobFactory())

        for(login in list.login){
            val job = JobBuilder.newJob(SftpJob::class.java).build()
            job.jobDataMap["login"] = login
            val trigger = TriggerBuilder.newTrigger().withIdentity(login.id).withSchedule(cronSchedule(login.scheduledTime)).forJob(job).build()

            try {
                scheduler.scheduleJob(job, trigger)
            } catch (e: SchedulerException) {
                e.printStackTrace()
            }
        }
        scheduler.start();
        return scheduler;
    }

}

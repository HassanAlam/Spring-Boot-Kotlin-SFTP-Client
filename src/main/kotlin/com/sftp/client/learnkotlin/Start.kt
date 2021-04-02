package com.sftp.client.learnkotlin

import com.sftp.client.learnkotlin.file.Load
import com.sftp.client.learnkotlin.file.Save
import com.sftp.client.learnkotlin.jsch.Download
import com.sftp.client.learnkotlin.jsch.Upload
import com.sftp.client.learnkotlin.model.LoginSettings
import com.sftp.client.learnkotlin.scheduler.CustomScheduler
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.CommandLineRunner
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*


@Component
class Start : CommandLineRunner{

    private val LOG: Logger = LoggerFactory.getLogger(Start::class.java)

    val list = Load().loadFromJsonFile()
    private val customScheduler: CustomScheduler = CustomScheduler()

    @Scheduled(fixedDelay = 120000) //2min
    fun writeCached() {
        Save().saveToJsonFile(list)
    }

    override fun run(vararg args: String?) {
        println("start")
        customScheduler.scheduler(list)
    }

}
package com.sftp.client.learnkotlin

import com.sftp.client.learnkotlin.file.Save
import com.sftp.client.learnkotlin.file.Cache
import com.sftp.client.learnkotlin.scheduler.CustomScheduler
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.boot.CommandLineRunner
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component


@Component
@EnableScheduling
class Start : CommandLineRunner{

    private val LOG: Logger = LoggerFactory.getLogger(Start::class.java)

    private val customScheduler: CustomScheduler = CustomScheduler()

    @Scheduled(fixedDelay = 120000) //2min
    fun writeCached() {
        LOG.info("Write cached")
        Save().saveToJsonFile(Cache.settingsCache)
    }

    override fun run(vararg args: String?) {
        println("start")
       // customScheduler.scheduler(Cache.settingsCache)
        Save().saveToJsonFile(Cache.settingsCache)
    }

}
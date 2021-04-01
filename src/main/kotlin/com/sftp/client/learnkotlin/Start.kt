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
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.util.*


@Component
class Start : Job {

    private val LOG: Logger = LoggerFactory.getLogger(Start::class.java)

    val list = Load().loadFromJsonFile()
    private val download: Download = Download()
    private val upload: Upload = Upload()
    private val customScheduler: CustomScheduler = CustomScheduler()

    @Scheduled(fixedDelay = 120000) //2min
    fun writeCached() {
        Save().saveToJsonFile(list)
    }

    @Autowired
    fun run() {
        println("start")
        customScheduler.scheduler(list)
    }

    @Override
    override fun execute(context: JobExecutionContext) {
        println("I am scheduled on " + Date(System.currentTimeMillis()))
        System.out.println("MyJob 1" + context.jobDetail.jobDataMap["login"])
        val login = context.jobDetail.jobDataMap["login"] as LoginSettings
        execute(login)
    }

    @Override
    private fun execute(login: LoginSettings) {
        try {
            downloadFiles(login)
            uploadFiles(login)
        }catch (e: Exception){
            LOG.error(e.message)
        }
    }

    fun downloadFiles(loginSettings: LoginSettings){
        if (isDownload(loginSettings))
            download.startDownload(loginSettings)
    }

    fun uploadFiles(loginSettings: LoginSettings){
        if (isUpload(loginSettings))
            upload.startUpload(loginSettings)
    }

    fun isUpload(loginSettings: LoginSettings): Boolean {
        return loginSettings.transactionType == "Upload"
    }

    fun isDownload(loginSettings: LoginSettings): Boolean {
        return loginSettings.transactionType == "Download"
    }

}
package com.sftp.client.learnkotlin.scheduler

import com.sftp.client.learnkotlin.jsch.Download
import com.sftp.client.learnkotlin.jsch.Upload
import com.sftp.client.learnkotlin.model.LoginSettings
import org.quartz.Job
import org.quartz.JobExecutionContext
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

class SftpJob : Job {

    private val download: Download = Download()
    private val upload: Upload = Upload()
    private val LOG: Logger = LoggerFactory.getLogger(SftpJob::class.java)


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
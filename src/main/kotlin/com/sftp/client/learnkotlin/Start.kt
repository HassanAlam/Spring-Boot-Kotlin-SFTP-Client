package com.sftp.client.learnkotlin

import com.sftp.client.learnkotlin.Util.Util
import com.sftp.client.learnkotlin.file.Load
import com.sftp.client.learnkotlin.file.Save
import com.sftp.client.learnkotlin.jsch.Download
import com.sftp.client.learnkotlin.jsch.Upload
import com.sftp.client.learnkotlin.model.Login
import com.sftp.client.learnkotlin.model.LoginSettings
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Service
class Start {

    val list = Load().loadFromJsonFile()
    private val download: Download = Download()
    private val upload: Upload = Upload()

    @PreDestroy
    @Scheduled(fixedRate = 60L * 1000L)
    fun writeCached() {
        Save().saveToJsonFile(list)
    }

    @Autowired
    fun run() {
        println("start")

        for(login in list.login){
            downloadFiles(login)
            uploadFiles(login)
        }
    }

    fun downloadFiles(loginSettings: LoginSettings){
        if (isDownload(loginSettings)) {

            download.startDownload(loginSettings)

        }
    }

    fun uploadFiles(loginSettings: LoginSettings){
        if (isUpload(loginSettings)) {

            upload.startUpload(loginSettings)

        }
    }

    fun isUpload(loginSettings: LoginSettings): Boolean {
        return loginSettings.transactionType == "Upload"
    }

    fun isDownload(loginSettings: LoginSettings): Boolean {
        return loginSettings.transactionType == "Download"
    }

}
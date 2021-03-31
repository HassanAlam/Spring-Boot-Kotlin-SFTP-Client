package com.sftp.client.learnkotlin

import com.sftp.client.learnkotlin.file.Load
import com.sftp.client.learnkotlin.file.Save
import com.sftp.client.learnkotlin.jsch.Download
import com.sftp.client.learnkotlin.jsch.Upload
import com.sftp.client.learnkotlin.model.LoginSettings
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service


@Service
class Start {

    val list = Load().loadFromJsonFile()
    private val download: Download = Download()
    private val upload: Upload = Upload()

    fun writeCached() {
        Save().saveToJsonFile(list)
    }

    @Scheduled(fixedDelay = 120000) //2min
    fun run() {
        println("start")

        for(login in list.login){
            downloadFiles(login)
            uploadFiles(login)
        }
        writeCached()
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
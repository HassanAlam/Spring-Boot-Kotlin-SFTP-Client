package com.sftp.client.learnkotlin

import com.sftp.client.learnkotlin.file.Load
import com.sftp.client.learnkotlin.file.Save
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

    @PreDestroy
    @Scheduled(fixedRate = 60L * 1000L)
    fun writeCached() {
        Save().saveToJsonFile(list)
    }


    @Autowired
    fun run() {
        println("start")


    }
}
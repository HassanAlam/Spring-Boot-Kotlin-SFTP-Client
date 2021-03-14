package com.sftp.client.learnkotlin

import com.sftp.client.learnkotlin.file.Save
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service
import javax.annotation.PostConstruct
import javax.annotation.PreDestroy

@Service
class Start {

/*
    @PostConstruct
    fun init() {
        // load the data from file
    }

    @PreDestroy
    @Scheduled(fixedRate = 60L * 1000L)
    fun writeCached() {
        // update data to file
    }
*/

    @Autowired
    fun run() {

        println("start")

        Save().saveToJsonFile()

    }
}
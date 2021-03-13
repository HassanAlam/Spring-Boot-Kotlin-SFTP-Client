package com.sftp.client.learnkotlin

import com.sftp.client.learnkotlin.file.Save
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class Start {

    @Autowired
    fun run() {

        println("start")

        Save().saveToJsonFile()

    }
}
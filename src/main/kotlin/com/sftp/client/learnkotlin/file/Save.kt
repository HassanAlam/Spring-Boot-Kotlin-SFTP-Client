package com.sftp.client.learnkotlin.file

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.sftp.client.learnkotlin.model.Login
import java.io.File

class Save {

    private val fileLocation = "settings/settings.json"
    val objectMapper = jacksonObjectMapper()


    fun saveToJsonFile(login: Login){
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(File(fileLocation), login)
    }

}
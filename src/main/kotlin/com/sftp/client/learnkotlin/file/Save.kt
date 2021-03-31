package com.sftp.client.learnkotlin.file

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.sftp.client.learnkotlin.model.Login
import com.sftp.client.learnkotlin.model.LoginDetails
import com.sftp.client.learnkotlin.model.LoginSettings
import java.io.File
import java.io.FileWriter

class Save {

    private val fileLocation = "settings/settings.json"
    val objectMapper = jacksonObjectMapper()


    fun saveToJsonFile(login: Login){
        objectMapper.writerWithDefaultPrettyPrinter().writeValue(File(fileLocation), login)
    }

}
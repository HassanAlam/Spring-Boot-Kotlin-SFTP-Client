package com.sftp.client.learnkotlin.file

import com.fasterxml.jackson.module.kotlin.*
import com.sftp.client.learnkotlin.model.Login
import java.io.*

class Load {

    private val fileLocation = "settings/settings.json"
    val objectMapper = jacksonObjectMapper()

    fun loadFromJsonFile(): Login{
        var login: Login =  objectMapper.readValue<Login>(File(fileLocation))
        return login
    }

}
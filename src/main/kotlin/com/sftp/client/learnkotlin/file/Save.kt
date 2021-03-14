package com.sftp.client.learnkotlin.file

import com.fasterxml.jackson.databind.ObjectMapper
import com.sftp.client.learnkotlin.model.Login
import com.sftp.client.learnkotlin.model.LoginDetails
import com.sftp.client.learnkotlin.model.LoginSettings
import java.io.FileWriter


class Save {

    private val fileLocation = "settings/settings.json"
    val objectMapper = ObjectMapper()
    val list = Login(mutableListOf<LoginSettings>())


    fun saveToJsonFile(){

        val settings = LoginSettings(
            LoginDetails("","","","","",""),
            "",
            "","",
            "","",
            2,"",
            "","",
            "","",
            ""
        )

        list.login.add(settings)

        val fw = FileWriter(fileLocation, false)
            fw.write(objectMapper.writeValueAsString(list) + "\n")
            fw.close()
    }

}
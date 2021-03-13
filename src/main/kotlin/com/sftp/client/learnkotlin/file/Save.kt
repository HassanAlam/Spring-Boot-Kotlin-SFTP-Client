package com.sftp.client.learnkotlin.file

import com.fasterxml.jackson.databind.ObjectMapper
import com.sftp.client.learnkotlin.model.Settings
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import java.awt.SystemColor.text
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardOpenOption

class Save {

    private val fileLocation = "settings/settings.json"
    val objectMapper = ObjectMapper()


    fun saveToJsonFile(){

        val settings = Settings("test",
        "","",
            "","",
            "","",
            "","",
            "","",
            2,"",
            "","",
            "","",
            ""
        )
            val fw = FileWriter(fileLocation, true)
            fw.write(objectMapper.writeValueAsString(settings) + "\n")
            fw.close()
    }

}
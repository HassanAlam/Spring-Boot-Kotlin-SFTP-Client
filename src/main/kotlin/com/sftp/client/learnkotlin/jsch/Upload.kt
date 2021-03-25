package com.sftp.client.learnkotlin.jsch

import com.sftp.client.learnkotlin.model.LoginSettings
import java.io.File

class Upload {

    fun uploadFile(loginSettings: LoginSettings) {
        TODO("Not yet implemented")
    }

    fun uploadFiles(loginSettings: LoginSettings) {
        TODO("Not yet implemented")
    }

    fun deleteFile(delete: String, files: Array<File>, i: Int) {
        if (delete == "1") {
            files[i].delete()
        }
    }

    fun isForce(force: String): Boolean {
        return force == "1"
    }


}
package com.sftp.client.learnkotlin.jsch

import com.jcraft.jsch.Channel
import com.jcraft.jsch.ChannelSftp
import com.sftp.client.learnkotlin.Util.Utils
import com.sftp.client.learnkotlin.model.LoginSettings
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File

class Upload {
    private val LOG: Logger = LoggerFactory.getLogger(Upload::class.java)
    var jschConfig = JschConfig()

    fun startUpload(loginSettings: LoginSettings) {
        val folder: File = File(loginSettings.localDirectoryPath)
        val files = folder.listFiles()

        for (file in files) {
            if (file.isFile) {

                uploadMatchingFile(loginSettings, file)
                uploadAllFiles(loginSettings, file)

            }
        }
    }

    fun uploadMatchingFile(loginSettings: LoginSettings, file: File) {
        if(fileNameIsFilledOut(loginSettings)) {
            val channelSftp: ChannelSftp = setupJschConnection(loginSettings) as ChannelSftp
            if (file.name == loginSettings.fileName) {

                channelSftp.connect()
                LOG.info("Open SFTP Connection")

                //get minutes since last modification on file
                val minutesSinceLastMod = getMinutesSinceLastMod(file.lastModified())

                //compare lastUploadedFileModDate vs lastModified
                if (Utils.convertToLong(loginSettings.fileModDateMillisTime) < file.lastModified() || isForce(loginSettings.forceFile)) {
                    if (minutesSinceLastMod >= loginSettings.minimumFileAgeMinutes) {

                        processFile(loginSettings, file, channelSftp)

                    }
                }
            }
            channelSftp.exit()
            channelSftp.disconnect()
        }
    }

    fun uploadAllFiles(loginSettings: LoginSettings, file: File) {
        if(!fileNameIsFilledOut(loginSettings)) {
            val channelSftp: ChannelSftp = setupJschConnection(loginSettings) as ChannelSftp

            channelSftp.connect()
            LOG.info("Open SFTP Connection")

            //get minutes since last modification on file
            val minutesSinceLastMod = getMinutesSinceLastMod(file.lastModified())

            //compare lastUploadedFileModDate vs lastModified
            if (Utils.convertToLong(loginSettings.fileModDateMillisTime) < file.lastModified() || isForce(loginSettings.forceFile)) {
                if (minutesSinceLastMod >= loginSettings.minimumFileAgeMinutes) {

                    processFile(loginSettings, file, channelSftp)

                }
            }

            channelSftp.exit()
            channelSftp.disconnect()
        }
    }

    private fun fileNameIsFilledOut(loginSettings: LoginSettings) =
            !Utils.isNullOrEmpty(loginSettings.fileName)


    private fun setupJschConnection(loginSettings: LoginSettings): Channel? {
        return jschConfig.setupJschConnection(loginSettings.loginDetails)
    }

    private fun processFile(loginSettings: LoginSettings, file: File, channelSftp: ChannelSftp) {
        val localFile: String = loginSettings.localDirectoryPath + file.name
        val remoteDir: String = loginSettings.remoteDirectoryPath
        channelSftp.put(localFile, remoteDir + file.name)
        LOG.info("{} Uploaded to SFTP", file.name)

        archiveFile(loginSettings,file)
        deleteFile(loginSettings,file)
        //log

    }

    fun getMinutesSinceLastMod(l: Long): Long {
        return (System.currentTimeMillis() - l) / 60000
    }


    fun archiveFile(loginSettings: LoginSettings,  file: File) {
        if (loginSettings.archiveSource == "1") {
            Utils.archiveFilesLocal(loginSettings.localDirectoryPath, loginSettings.localDirectoryPath + file.name, loginSettings.localDirectoryPath + "/Archive/" + file.name)
        }
    }

    fun deleteFile(loginSettings: LoginSettings,  file: File) {
        if (loginSettings.deleteSource == "1") {
            file.delete()
        }
    }

    fun isForce(force: String): Boolean {
        return force == "1"
    }

}
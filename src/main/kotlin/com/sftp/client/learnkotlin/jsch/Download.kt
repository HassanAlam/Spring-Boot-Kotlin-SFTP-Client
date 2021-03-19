package com.sftp.client.learnkotlin.jsch

import com.jcraft.jsch.Channel
import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.ChannelSftp.LsEntry
import com.sftp.client.learnkotlin.Util.Util
import com.sftp.client.learnkotlin.model.LoginSettings
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Download {

    private val LOG: Logger = LoggerFactory.getLogger(Download::class.java)
    var jschConfig = JschConfig()

    fun downloadFile(loginSettings: LoginSettings) {
        val channelSftp: ChannelSftp = setupJschConnection(loginSettings) as ChannelSftp
        try{
            channelSftp.connect()
            LOG.info("Open SFTP Connection");
            val list: Vector<LsEntry> = channelSftp.ls(loginSettings.remoteDirectoryPath) as Vector<LsEntry>
            if (!list.isEmpty()) {
               loginSettings.fileName == list.get(0).longname
                val minutesSinceLastMod: Long = getMinutesSinceLastMod(list[0])
                if (compareLastFileDate(loginSettings.fileModDateMillisTime, list.get(0)) || isForce(loginSettings.forceFile))  {
                    if (minutesSinceLastMod >= loginSettings.minimumFileAgeMinutes) {
                        LOG.info("Downloading file");

                        downloadFile(loginSettings,channelSftp)
                        processFile(loginSettings,channelSftp)
                        TODO("Not done yet")
                        logResult()
                    }
                }
            }
        }finally {
            LOG.info("Close connection");
            channelSftp.exit();
            channelSftp.disconnect();
        }
    }

    private fun logResult() {
        TODO("Not yet implemented")
    }

    private fun processFile(loginSettings: LoginSettings, channelSftp: ChannelSftp) {
        unzipFile()
        archiveFile()
        deleteFile(loginSettings, channelSftp)
        addDateToFilname(loginSettings)
    }

    private fun addDateToFilname(loginSettings: LoginSettings) {
        val myDateObj = LocalDateTime.now()
        val myFormatObj = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm")
        val formattedDate = myDateObj.format(myFormatObj)

        if (loginSettings.addDateToEndOfFilename == "1") {
            TODO("Not yet implemented")
        }

    }

    private fun deleteFile(loginSettings: LoginSettings, channelSftp: ChannelSftp) {
        if (loginSettings.deleteSource == "1") {
            channelSftp.rm(loginSettings.remoteDirectoryPath + loginSettings.fileName)
        }
    }

    private fun archiveFile() {
        TODO("Not yet implemented")
    }

    private fun unzipFile() {
        TODO("Not yet implemented")
    }

    private fun downloadFile(loginSettings: LoginSettings, channelSftp: ChannelSftp) {
        channelSftp.get(loginSettings.remoteDirectoryPath + loginSettings.fileName, loginSettings.localDirectoryPath + "~" + loginSettings.fileName + ".DOWNLOADING");
        renameTmpNameToOrigAfterDownload(loginSettings)
    }

    private fun renameTmpNameToOrigAfterDownload(loginSettings: LoginSettings) {
        Util.renameFile(loginSettings.localDirectoryPath + "~" + loginSettings.fileName + ".DOWNLOADING", loginSettings.localDirectoryPath + loginSettings.fileName)
    }

    private fun setupJschConnection(loginSettings: LoginSettings): Channel? {
        return jschConfig.setupJschConnection(loginSettings.loginDetails)
    }

    fun compareLastFileDate(lastUploadedFileModMillisDate: String, entry: ChannelSftp.LsEntry): Boolean {
        return Util.convertToLong(lastUploadedFileModMillisDate) < getEntryMillisMod(entry)
    }
    fun getEntryMillisMod(lsEntry: ChannelSftp.LsEntry): Long {
        return lsEntry.attrs.mTime * 1000L
    }

    fun isForce(force: String): Boolean {
        return force == "1"
    }

    fun downloadFiles(loginSettings: LoginSettings) {
        TODO("Not yet implemented")
    }

    private fun getMinutesSinceLastMod(lsEntry: ChannelSftp.LsEntry?): Long {
        TODO("Not done yet")
    }

}
package com.sftp.client.learnkotlin.jsch

import com.jcraft.jsch.Channel
import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.ChannelSftp.LsEntry
import com.sftp.client.learnkotlin.Util.Utils
import com.sftp.client.learnkotlin.model.LoginSettings
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class Download {

    private val LOG: Logger = LoggerFactory.getLogger(Download::class.java)
    var jschConfig = JschConfig()

    fun startDownload(loginSettings: LoginSettings) {
        val channelSftp: ChannelSftp = setupJschConnection(loginSettings) as ChannelSftp
        try{
            channelSftp.connect()
            LOG.info("Open SFTP Connection");
            val list: Vector<LsEntry> = channelSftp.ls(loginSettings.remoteDirectoryPath) as Vector<LsEntry>
            if (!list.isEmpty()) {

                downloadMatchingFile(loginSettings,list,channelSftp)
                downloadAllFiles(loginSettings,list,channelSftp)

            }
        }finally {
            LOG.info("Close connection");
            channelSftp.exit();
            channelSftp.disconnect();
        }
    }

    private fun downloadMatchingFile(loginSettings: LoginSettings, list: Vector<LsEntry>, channelSftp: ChannelSftp) {
        if(fileNameIsFilledOut(loginSettings)){
            val minutesSinceLastMod: Long = getMinutesSinceLastMod(list[0])
            if (compareLastFileDate(loginSettings.fileModDateMillisTime, list.get(0)) || isForce(loginSettings.forceFile)) {
                if (minutesSinceLastMod >= loginSettings.minimumFileAgeMinutes) {
                    LOG.info("Downloading file");
                    processFile(loginSettings, channelSftp)
                    logResult()
                }
            }
        }
        channelSftp.get(loginSettings.remoteDirectoryPath + loginSettings.fileName, loginSettings.localDirectoryPath + "~" + loginSettings.fileName + ".DOWNLOADING");
        renameTmpNameToOrigAfterDownload(loginSettings)
    }

    fun downloadAllFiles(loginSettings: LoginSettings, list: Vector<LsEntry>, channelSftp: ChannelSftp) {
        if(!fileNameIsFilledOut(loginSettings)) {
            for (entry in list) {
                //skip download if its an dir
                if (!entry.attrs.isDir) {
                    val minutesSinceLastMod = getMinutesSinceLastMod(entry)

                    //compare lastDownloadDate vs lastModified
                    if (compareLastFileDate(
                            loginSettings.fileModDateMillisTime,
                            entry
                        ) || isForce(loginSettings.forceFile)
                    ) {
                        if (minutesSinceLastMod >= loginSettings.minimumFileAgeMinutes) {
                            LOG.info("Downloading files")
                            processFile(loginSettings, channelSftp)
                            logResult()
                        }
                    }
                }
            }
        }
    }


    private fun fileNameIsFilledOut(loginSettings: LoginSettings) =
        !Utils.isNullOrEmpty(loginSettings.fileName)


    private fun logResult() {
        TODO("Not yet implemented")
    }

    private fun processFile(loginSettings: LoginSettings, channelSftp: ChannelSftp) {
        downloadFileWithTmpName(loginSettings, channelSftp)
        renameTmpNameToOrigAfterDownload(loginSettings)
        unzipFile(loginSettings)
        archiveFile(loginSettings)
        deleteFile(loginSettings, channelSftp)
        addDateToFilname(loginSettings)
    }

    private fun downloadFileWithTmpName(loginSettings: LoginSettings, channelSftp: ChannelSftp) {
        channelSftp.get(loginSettings.remoteDirectoryPath + loginSettings.fileName, loginSettings.localDirectoryPath + "~" + loginSettings.fileName + ".DOWNLOADING")
    }

    private fun addDateToFilname(loginSettings: LoginSettings) {
        val myDateObj = LocalDateTime.now()
        val myFormatObj = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm")
        val formattedDate = myDateObj.format(myFormatObj)

        if (loginSettings.addDateToEndOfFilename == "1") {
            Utils.renameFile(
                loginSettings.localDirectoryPath + loginSettings.fileName,
                    Utils.getFileWithoutExtension(
                    loginSettings.localDirectoryPath + loginSettings.fileName
                ).toString() + "_" + formattedDate + Utils.getFileExtension(loginSettings.fileName)
            )
        }
    }

    private fun deleteFile(loginSettings: LoginSettings, channelSftp: ChannelSftp) {
        if (loginSettings.deleteSource == "1") {
            channelSftp.rm(loginSettings.remoteDirectoryPath + loginSettings.fileName)
        }
    }

    private fun archiveFile(loginSettings: LoginSettings) {
        if (loginSettings.archiveSource == "1") {
            Utils.archiveFilesSFTP(loginSettings)
        }    }

    private fun unzipFile(loginSettings: LoginSettings) {
        if (loginSettings.unzipFile == "1") {
            Utils.unzip(loginSettings.localDirectoryPath + loginSettings.fileName, loginSettings.localDirectoryPath)
            Utils.archiveFilesLocal(
                loginSettings.localDirectoryPath,
                loginSettings.localDirectoryPath + loginSettings.fileName,
                loginSettings.localDirectoryPath + "/Archive/" + loginSettings.fileName
            )
        }
    }

    private fun renameTmpNameToOrigAfterDownload(loginSettings: LoginSettings) {
        Utils.renameFile(loginSettings.localDirectoryPath + "~" + loginSettings.fileName + ".DOWNLOADING", loginSettings.localDirectoryPath + loginSettings.fileName)
    }

    private fun setupJschConnection(loginSettings: LoginSettings): Channel? {
        return jschConfig.setupJschConnection(loginSettings.loginDetails)
    }

    fun compareLastFileDate(lastUploadedFileModMillisDate: String, entry: ChannelSftp.LsEntry): Boolean {
        return Utils.convertToLong(lastUploadedFileModMillisDate) < getEntryMillisMod(entry)
    }
    fun getEntryMillisMod(lsEntry: ChannelSftp.LsEntry): Long {
        return lsEntry.attrs.mTime * 1000L
    }

    fun isForce(force: String): Boolean {
        return force == "1"
    }

    private fun getMinutesSinceLastMod(lsEntry: ChannelSftp.LsEntry): Long {
        return (System.currentTimeMillis() - getEntryMillisMod(lsEntry)) / 60000
    }


}
package com.sftp.client.learnkotlin.jsch

import com.jcraft.jsch.Channel
import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.ChannelSftp.LsEntry
import com.jcraft.jsch.SftpException
import com.sftp.client.learnkotlin.Start
import com.sftp.client.learnkotlin.Util.Utils
import com.sftp.client.learnkotlin.file.Cache
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
            if (compareLastFileDate(loginSettings.fileModDateMillisTime, list[0]) || loginSettings.forceFile) {
                if (minutesSinceLastMod >= loginSettings.minimumFileAgeMinutes) {
                    LOG.info("Downloading file");
                    processFile(loginSettings, loginSettings.fileName,  channelSftp)
                    logResult(getEntryMillisMod(list[0]), loginSettings)
                }
            }
        }
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
                        ) || loginSettings.forceFile
                    ) {
                        if (minutesSinceLastMod >= loginSettings.minimumFileAgeMinutes) {
                            LOG.info("Downloading files")
                            processFile(loginSettings,entry.filename, channelSftp)
                            logResult(getEntryMillisMod(list[0]), loginSettings)
                        }
                    }
                }
            }
        }
    }


    private fun fileNameIsFilledOut(loginSettings: LoginSettings) =
        !Utils.isNullOrEmpty(loginSettings.fileName)

    private fun logResult(entryMillisMod: Long, loginSettings: LoginSettings) {
        val start = Start()
        for(login in Cache.settingsCache.login) {
            if(login.id == loginSettings.id){
                login.fileModDateTime = Utils.convertMillistoDate(entryMillisMod).toString()
                login.fileModDateMillisTime = entryMillisMod.toString()
                start.writeCached()
            }
        }
    }

    private fun processFile(loginSettings: LoginSettings,fileName: String, channelSftp: ChannelSftp) {
        downloadFileWithTmpName(loginSettings,fileName, channelSftp)
        renameTmpNameToOrigAfterDownload(loginSettings,fileName)
        unzipFile(loginSettings,fileName)
        archiveFile(loginSettings,fileName)
        deleteFile(loginSettings,fileName, channelSftp)
        addDateToFilname(loginSettings,fileName)
    }

    @Throws(SftpException::class)
    private fun downloadFileWithTmpName(loginSettings: LoginSettings, fileName: String, channelSftp: ChannelSftp) {
    channelSftp[loginSettings.remoteDirectoryPath + fileName, loginSettings.localDirectoryPath + "~" + fileName + ".DOWNLOADING"]
    }

    private fun addDateToFilname(loginSettings: LoginSettings, fileName: String) {
        val myDateObj = LocalDateTime.now()
        val myFormatObj = DateTimeFormatter.ofPattern("yyyyMMdd_HHmm")
        val formattedDate = myDateObj.format(myFormatObj)

        if (loginSettings.addDateToEndOfFilename) {
            Utils.renameFile(
                loginSettings.localDirectoryPath + fileName,
                    Utils.getFileWithoutExtension(
                    loginSettings.localDirectoryPath + fileName
                ) + "_" + formattedDate + Utils.getFileExtension(fileName)
            )
        }
    }

    private fun deleteFile(loginSettings: LoginSettings,fileName: String, channelSftp: ChannelSftp) {
        if (loginSettings.deleteSource) {
            channelSftp.rm(loginSettings.remoteDirectoryPath + fileName)
        }
    }

    private fun archiveFile(loginSettings: LoginSettings, fileName: String) {
        if (loginSettings.archiveSource) {
            Utils.archiveFilesSFTP(loginSettings)
        }    }

    private fun unzipFile(loginSettings: LoginSettings, fileName: String) {
        if (loginSettings.unzipFile) {
            Utils.unzip(loginSettings.localDirectoryPath + fileName, loginSettings.localDirectoryPath)
            Utils.archiveFilesLocal(
                loginSettings.localDirectoryPath,
                loginSettings.localDirectoryPath + fileName,
                loginSettings.localDirectoryPath + "/Archive/" + fileName
            )
        }
    }

    private fun renameTmpNameToOrigAfterDownload(loginSettings: LoginSettings, fileName: String) {
        Utils.renameFile(loginSettings.localDirectoryPath + "~" + fileName + ".DOWNLOADING", loginSettings.localDirectoryPath + fileName)
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

    private fun getMinutesSinceLastMod(lsEntry: ChannelSftp.LsEntry): Long {
        return (System.currentTimeMillis() - getEntryMillisMod(lsEntry)) / 60000
    }


}
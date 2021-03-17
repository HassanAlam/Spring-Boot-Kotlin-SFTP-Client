package com.sftp.client.learnkotlin.jsch

import com.jcraft.jsch.Channel
import com.jcraft.jsch.ChannelSftp
import com.jcraft.jsch.ChannelSftp.LsEntry
import com.sftp.client.learnkotlin.Util.Util
import com.sftp.client.learnkotlin.model.LoginSettings
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.util.*

class Download {

    private val LOG: Logger = LoggerFactory.getLogger(Download::class.java)


    var jschConfig = JschConfig()


    fun downloadFile(loginSettings: LoginSettings) {

        //Open SFTP Connetion
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

                        TODO("Not done yet")


                    }
                }
            }
        }finally {
            LOG.info("Close connection");
            channelSftp.exit();
            channelSftp.disconnect();
        }

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
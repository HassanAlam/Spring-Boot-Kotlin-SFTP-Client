package com.sftp.client.learnkotlin.Util

import com.sftp.client.learnkotlin.model.LoginSettings
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import java.io.File
import java.io.IOException
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.text.SimpleDateFormat
import java.util.*

class Util {
    companion object{

        private val LOG: Logger = LoggerFactory.getLogger(Util.javaClass)


        fun isNullOrEmpty(str: String): Boolean {
            return if (str == null || str.trim { it <= ' ' }.length == 0) true else false
        }

        fun convertToLong(milliseconds: String?): Long {
            return if (milliseconds == null) {
                0
            } else {
                java.lang.Long.valueOf(milliseconds)
            }
        }

        fun unzip(zipFilePath: String, destDirectory: String) {
            val destDir: File = File(destDirectory)
            if (!destDir.exists()) {
                destDir.mkdir()
            }
            TODO("Not yet implemented")
        }

            fun convertMillistoDate(milliseconds: Long): String? {
            val date = Date(milliseconds)
            val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss.sss")
            return sdf.format(date)
        }

        fun renameFile(oldname: String, newname: String): Boolean {
            //File (or directory) with old name
            val file = File(oldname)
            //File (or directory) with new name
            val file2 = File(newname)
            //overwrite file
            if (file2.exists()) file2.delete()
            //Rename file (or directory)
            return file.renameTo(file2)
        }

        fun getFileExtension(file: String): String {
            val lastIndexOf = file.lastIndexOf(".")
            return if (lastIndexOf == -1) {
                "" // empty extension
            } else file.substring(lastIndexOf)
        }

        fun getFileWithoutExtension(fileName: String): String {
            val index = fileName.lastIndexOf('.')
            return if (index == -1) {
                fileName
            } else {
                fileName.substring(0, index)
            }
        }

        fun archiveFilesLocal(localDirectoryPath: String, src: String, dest: String) {
            File("$localDirectoryPath/Archive/").mkdir()
            var result: Path? = null
            try {
                result = Files.move(Paths.get(src), Paths.get(dest), StandardCopyOption.REPLACE_EXISTING)
            } catch (e: IOException) {
                LOG.info("Exception while moving file: {}", e.message)
            }
            if (result != null) {
                LOG.info("File moved successfully.")
            } else {
                LOG.info("File movement failed.")
            }
        }

        fun archiveFilesSFTP(loginSettings: LoginSettings) {
            TODO("Not yet implemented")
        }

    }
}
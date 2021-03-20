package com.sftp.client.learnkotlin.Util

import java.io.File
import java.text.SimpleDateFormat
import java.util.*

class Util {
    companion object{
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

    }
}
package com.sftp.client.learnkotlin.Util

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
    }
}
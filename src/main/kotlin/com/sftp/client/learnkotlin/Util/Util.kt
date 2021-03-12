package com.sftp.client.learnkotlin.Util

class Util {
    companion object{
        fun isNullOrEmpty(str: String): Boolean {
            return if (str == null || str.trim { it <= ' ' }.length == 0) true else false
        }
    }
}
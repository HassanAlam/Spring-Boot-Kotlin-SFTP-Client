package com.sftp.client.learnkotlin.model

class Settings(val loginId: Int, val fileId: Int,
val userName: String, val userPassword: String, val privateKeyPath: String, val privateKeyPassword: String, val host: String,
val port: String, val TransactionType: String, val LocalDirectoryPath: String, val RemoteDirectoryPath: String, val FileName: String,
val DeleteSource: String, val MinimumFileAgeMinutes: Int, val FileModDateTime: String, val FileModDateMillisTime: String, val ArchiveSource: String,
val AddDateToEndOfFilename: String, val UnzipFile: String, val ForceFile: String) {
}
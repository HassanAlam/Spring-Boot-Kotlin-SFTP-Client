package com.sftp.client.learnkotlin.model

class LoginSettings(val loginDetails: LoginDetails, val TransactionType: String, val LocalDirectoryPath: String, val RemoteDirectoryPath: String, val FileName: String,
                    val DeleteSource: String, val MinimumFileAgeMinutes: Int, val FileModDateTime: String, val FileModDateMillisTime: String, val ArchiveSource: String,
                    val AddDateToEndOfFilename: String, val UnzipFile: String, val ForceFile: String) {
}
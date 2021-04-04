package com.sftp.client.learnkotlin.model

class LoginSettings(val loginDetails: LoginDetails, val id: String, val scheduledTime: String, val transactionType: String, val localDirectoryPath: String, val remoteDirectoryPath: String, val fileName: String,
                    val deleteSource: String, val minimumFileAgeMinutes: Int, var fileModDateTime: String, var fileModDateMillisTime: String, val archiveSource: String,
                    val addDateToEndOfFilename: String, val unzipFile: String, val forceFile: String) {
}
package com.sftp.client.learnkotlin.model

class LoginSettings(var loginDetails: LoginDetails = LoginDetails(), var id: String = "", var scheduledTime: String = "", var transactionType: String = "", var localDirectoryPath: String = "", var remoteDirectoryPath: String = "", var fileName: String = "",
                    var deleteSource: Boolean = false, var minimumFileAgeMinutes: Int = 0, var fileModDateTime: String = "", var fileModDateMillisTime: String = "", var archiveSource: Boolean = false,
                    var addDateToEndOfFilename: Boolean = false, var unzipFile: Boolean = false, var forceFile: Boolean = false, var active: Boolean = true) {
}
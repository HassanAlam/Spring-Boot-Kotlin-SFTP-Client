package com.sftp.client.learnkotlin.model

data class LoginDetails(var userName: String = "",var userPassword: String = "",var privateKeyFile: String = "",
                   var privateKeyPassword: String = "",var host: String = "",var port: String = "") {

}
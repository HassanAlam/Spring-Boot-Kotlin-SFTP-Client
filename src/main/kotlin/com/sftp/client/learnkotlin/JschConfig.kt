package com.sftp.client.learnkotlin

import com.jcraft.jsch.*
import com.sftp.client.learnkotlin.Util.Util
import com.sftp.client.learnkotlin.model.LoginDetails
import java.util.*

class JschConfig {

    fun setupJschConnection(loginDetails: LoginDetails): Channel? {
        val jsch = JSch()
        isPrivateKey(loginDetails, jsch)
        val portNumber = isDefaultPort(loginDetails);
        val jschSession = jsch.getSession(loginDetails.userName, loginDetails.host, portNumber)
        removeKnownHostSetting(jschSession)
        isPassword(loginDetails, jschSession)
        jschSession.connect()

        return jschSession.openChannel("sftp")
    }

    private fun isDefaultPort(loginDetails: LoginDetails): Int {
        var portNumber = 22
        if (!Util.isNullOrEmpty(loginDetails.port)) {
            portNumber = loginDetails.port.toInt()
        }
        return portNumber

    }

    fun removeKnownHostSetting(jschSession: Session) {
        val config = Properties()
        config["StrictHostKeyChecking"] = "no"
        config["PreferredAuthentications"] = "publickey,keyboard-interactive,password"
        jschSession.setConfig(config)
    }

    private fun isPrivateKey(loginDetails: LoginDetails, jsch: JSch) {
        if (!Util.isNullOrEmpty(loginDetails.privateKeyFile)) {
            jsch.addIdentity(loginDetails.privateKeyFile, loginDetails.privateKeyPassword)
        }
    }

    fun isPassword(loginDetails: LoginDetails, jschSession: Session) {
        if (!Util.isNullOrEmpty(loginDetails.userPassword)) {
            jschSession.setPassword(loginDetails.userPassword)
        }
    }


}
package com.sftp.client.learnkotlin.jsch

import com.jcraft.jsch.*
import com.sftp.client.learnkotlin.Util.Utils
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
        if (!Utils.isNullOrEmpty(loginDetails.port)) {
            portNumber = loginDetails.port.toInt()
        }
        return portNumber

    }

    private fun removeKnownHostSetting(jschSession: Session) {
        val config = Properties()
        config["StrictHostKeyChecking"] = "no"
        config["PreferredAuthentications"] = "publickey,keyboard-interactive,password"
        jschSession.setConfig(config)
    }

    private fun isPrivateKey(loginDetails: LoginDetails, jsch: JSch) {
        if (!Utils.isNullOrEmpty(loginDetails.privateKeyFile)) {
            jsch.addIdentity(loginDetails.privateKeyFile, loginDetails.privateKeyPassword)
        }
    }

   private fun isPassword(loginDetails: LoginDetails, jschSession: Session) {
        if (!Utils.isNullOrEmpty(loginDetails.userPassword)) {
            jschSession.setPassword(loginDetails.userPassword)
        }
    }


}
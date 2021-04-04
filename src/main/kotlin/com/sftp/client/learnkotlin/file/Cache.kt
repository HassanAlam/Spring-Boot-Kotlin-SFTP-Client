package com.sftp.client.learnkotlin.file

class Cache {
    companion object {
        val settingsCache = Load().loadFromJsonFile()
    }
}
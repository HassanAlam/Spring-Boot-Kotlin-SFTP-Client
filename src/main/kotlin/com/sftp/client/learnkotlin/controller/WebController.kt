package com.sftp.client.learnkotlin.controller

import com.sftp.client.learnkotlin.Start
import com.sftp.client.learnkotlin.file.Cache
import com.sftp.client.learnkotlin.model.LoginDetails
import com.sftp.client.learnkotlin.model.LoginSettings
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.*
import org.springframework.web.servlet.mvc.support.RedirectAttributes
import org.springframework.web.servlet.view.RedirectView


@Controller
class WebController {

    val start = Start()

    @GetMapping("/")
    fun showAll(model: Model): String? {
        model.addAttribute("logins", Cache.settingsCache.login)
        model.addAttribute("loginSettings",LoginSettings())
        return "sftp.html"
    }

    @RequestMapping(path = ["/"], method = [RequestMethod.POST])
    fun createNewSetting(@ModelAttribute loginSettings: LoginSettings): String? {
        println(loginSettings.id)
        println(loginSettings.loginDetails.userName)
        Cache.settingsCache.login.add(loginSettings)
        start.writeCached()
        start.restartScheduler()
        return "redirect:/"
    }

    @RequestMapping(path = ["/{id}"], method = [RequestMethod.GET])
    fun getSetting(model: Model, @PathVariable("id") id: String): String? {
        for(login in Cache.settingsCache.login) {
            if(login.id == id){
                model.addAttribute("loginSettings", login)
                return "edit.html"
            }
        }
        return "redirect:/"
    }

    //delete and update method
    @RequestMapping(path = ["/{id}"], method = [RequestMethod.POST])
    fun updateSetting(redirectAttributes: RedirectAttributes, @PathVariable("id") id: String, @ModelAttribute loginSettings: LoginSettings): RedirectView {

        for(login in Cache.settingsCache.login) {
            if(login.id == id){
                //todo
                login.loginDetails.userName = loginSettings.loginDetails.userName
                login.loginDetails.userPassword = loginSettings.loginDetails.userPassword
                login.loginDetails.privateKeyFile = loginSettings.loginDetails.privateKeyFile
                login.loginDetails.privateKeyPassword = loginSettings.loginDetails.privateKeyPassword
                login.loginDetails.host = loginSettings.loginDetails.host
                login.loginDetails.port = loginSettings.loginDetails.port
                login.scheduledTime = loginSettings.scheduledTime
                login.transactionType = loginSettings.transactionType
                login.localDirectoryPath = loginSettings.localDirectoryPath
                login.remoteDirectoryPath = loginSettings.remoteDirectoryPath
                login.fileName = loginSettings.fileName
                login.deleteSource = loginSettings.deleteSource
                login.minimumFileAgeMinutes = loginSettings.minimumFileAgeMinutes
                login.fileModDateTime = loginSettings.fileModDateTime
                login.fileModDateMillisTime = loginSettings.fileModDateMillisTime
                login.archiveSource = loginSettings.archiveSource
                login.addDateToEndOfFilename = loginSettings.addDateToEndOfFilename
                login.unzipFile = loginSettings.unzipFile
                login.forceFile = loginSettings.forceFile
                login.active = loginSettings.active
            }
        }
        start.writeCached()
        start.restartScheduler()
        val message = "Updated"
        val redirectView = RedirectView("/", true)
        redirectAttributes.addFlashAttribute("userMessage", message)
        return redirectView
    }
}
package com.sftp.client.learnkotlin.controller

import com.sftp.client.learnkotlin.file.Cache
import com.sftp.client.learnkotlin.model.LoginSettings
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import com.sftp.client.learnkotlin.Start
import org.springframework.web.bind.annotation.RequestMethod
import org.springframework.web.bind.annotation.RequestMapping
import com.jcraft.jsch.UserInfo
import org.apache.juli.logging.Log

import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.servlet.view.RedirectView

import org.springframework.web.servlet.mvc.support.RedirectAttributes







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

}
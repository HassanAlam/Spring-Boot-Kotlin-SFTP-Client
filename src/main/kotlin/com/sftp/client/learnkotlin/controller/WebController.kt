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
    fun createUser(@ModelAttribute loginSettings: LoginSettings): String? {
        println(loginSettings.id)
        println(loginSettings.loginDetails.userName)
        Cache.settingsCache.login.add(loginSettings)
        start.writeCached()
        return "redirect:/"
    }

}
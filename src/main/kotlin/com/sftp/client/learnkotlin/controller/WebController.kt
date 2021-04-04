package com.sftp.client.learnkotlin.controller

import com.sftp.client.learnkotlin.file.Cache
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class WebController {

    @GetMapping("/settings")
    fun showAll(model: Model): String? {
        model.addAttribute("logins", Cache.settingsCache.login)
        return "sftp.html"
    }

}
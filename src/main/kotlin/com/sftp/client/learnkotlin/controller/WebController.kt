package com.sftp.client.learnkotlin.controller

import com.sftp.client.learnkotlin.Start
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping


@Controller
class WebController {

    val start = Start()

    @GetMapping("/settings")
    fun showAll(model: Model): String? {
        model.addAttribute("logins", start.list.login)
        return "sftp.html"
    }

    /*
    val loginDetails = LoginDetails("","","","","","")
    val loginSettings = LoginSettings(loginDetails,"2","","","","","","",0,"","","","","","")
    list.login.add(1, loginSettings)
    */
}
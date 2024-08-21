package org.store.clothstar.kakaoLogin.controller

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@Controller
class KakaoLoginPageController {

    @Value("\${kakao.client_id}")
    private lateinit var clientId: String

    @Value("\${kakao.redirect_uri}")
    private lateinit var redirectUri: String

    @GetMapping("/kakaoLogin/page")
    fun loginPage(model: Model): String {
        val location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=$clientId&redirect_uri=$redirectUri"
        model.addAttribute("location", location)

        return "kakaoLogin"
    }
}
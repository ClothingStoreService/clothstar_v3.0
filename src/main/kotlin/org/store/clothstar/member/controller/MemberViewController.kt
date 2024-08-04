package org.store.clothstar.member.controller

import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import org.store.clothstar.common.dto.MessageDTO

@Tag(name = "index", description = "회원가입, 로그인, 로그아웃 기능과 user, seller, admin 페이지로 이동하기 위한 API 입니다.")
@Controller
class MemberViewController {
    @GetMapping("/main")
    fun main(): String {
        return "index"
    }

    @GetMapping("/membersPagingOffset")
    fun membersPagingOffset(): String {
        return "memberOffsetList"
    }

    @GetMapping("/membersPagingSlice")
    fun membersPagingSlice(): String {
        return "memberSliceList"
    }

    @GetMapping("/signup")
    fun signup(): String {
        return "signup"
    }

    @GetMapping("/login")
    fun login(): String {
        return "login"
    }

    @GetMapping("/user")
    @ResponseBody
    fun userPage(): MessageDTO {
        return MessageDTO(HttpStatus.OK.value(), "인증성공")
    }

    @GetMapping("/seller")
    @ResponseBody
    fun sellerPage(): MessageDTO {
        return MessageDTO(HttpStatus.OK.value(), "인증성공")
    }

    @GetMapping("/admin")
    @ResponseBody
    fun adminPage(): MessageDTO {
        return MessageDTO(HttpStatus.OK.value(), "인증성공")
    }
}
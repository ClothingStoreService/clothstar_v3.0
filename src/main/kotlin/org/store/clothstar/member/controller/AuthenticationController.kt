package org.store.clothstar.member.controller

import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController
import org.store.clothstar.common.dto.SaveResponseDTO
import org.store.clothstar.dto.request.CreateMemberRequest
import org.store.clothstar.member.application.MemberServiceApplication

@Tag(name = "Auth", description = "회원가입과 인증에 관한 API 입니다.")
@RestController
class AuthenticationController(
    private val memberServiceApplication: MemberServiceApplication,
) {
    @Operation(summary = "회원가입", description = "회원가입시 회원 정보를 저장한다.")
    @PostMapping("/v1/members")
    fun signup(@Validated @RequestBody createMemberDTO: CreateMemberRequest): ResponseEntity<SaveResponseDTO> {
        val accountId = memberServiceApplication.signUp(createMemberDTO)

        val saveResponseDTO = SaveResponseDTO(
            id = accountId!!,
            statusCode = HttpStatus.CREATED.value(),
            message = "회원가입이 정상적으로 되었습니다.",
        )

        return ResponseEntity(saveResponseDTO, HttpStatus.CREATED)
    }
}
package org.store.clothstar.member.authentication.controller

import io.github.oshai.kotlinlogging.KotlinLogging
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.media.Content
import io.swagger.v3.oas.annotations.media.Schema
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.store.clothstar.common.dto.ErrorResponseDTO
import org.store.clothstar.common.dto.MessageDTO
import org.store.clothstar.common.dto.SaveResponseDTO
import org.store.clothstar.kakaoLogin.service.KakaoLoginService
import org.store.clothstar.member.application.MemberServiceApplication
import org.store.clothstar.member.authentication.domain.SignUpType
import org.store.clothstar.member.authentication.service.KakaoSignUpService
import org.store.clothstar.member.authentication.service.NormalSignUpService
import org.store.clothstar.member.authentication.service.SignUpServiceFactory
import org.store.clothstar.member.dto.request.CertifyNumRequest
import org.store.clothstar.member.dto.request.MemberLoginRequest
import org.store.clothstar.member.dto.request.ModifyPasswordRequest
import org.store.clothstar.member.dto.request.SignUpRequest
import org.store.clothstar.member.dto.response.MemberResponse

@Tag(name = "Auth", description = "회원가입과 인증에 관한 API 입니다.")
@RestController
class AuthenticationController(
    private val memberServiceApplication: MemberServiceApplication,
    private val signUpServiceFactory: SignUpServiceFactory,
    private val kakaoLoginService: KakaoLoginService,
) {
    private val log = KotlinLogging.logger {}

    @Operation(summary = "회원 비밀번호 수정", description = "회원 비밀번호를 수정한다.")
    @ApiResponses(
        value = [ApiResponse(
            responseCode = "200",
            description = "회원 비밀번호가 수정 되었습니다.",
            content = [Content(schema = Schema(implementation = MemberResponse::class))]
        ), ApiResponse(
            responseCode = "404",
            description = "회원 정보를 찾을 수 없습니다.",
            content = [Content(schema = Schema(implementation = ErrorResponseDTO::class))]
        )]
    )
    @PatchMapping("/v1/members/pass/{id}")
    fun modifyPassword(
        @PathVariable("id") accountId: Long,
        @Validated @RequestBody modifyPasswordRequest: ModifyPasswordRequest
    ): ResponseEntity<MessageDTO> {
        log.info { "회원 비밀번호 요청 데이터 : accountId=${accountId}, password=${modifyPasswordRequest}" }

        memberServiceApplication.modifyPassword(
            accountId = accountId,
            password = modifyPasswordRequest.password
        )

        val messageDTO = MessageDTO(
            HttpStatus.OK.value(),
            "회원 비밀번호가 수정 되었습니다."
        )

        return ResponseEntity.ok(messageDTO)
    }

    @Operation(summary = "회원가입", description = "회원가입시 회원 정보를 저장한다.")
    @PostMapping("/v1/members")
    fun signup(
        @Validated @RequestBody signUpRequest: SignUpRequest,
        @RequestParam signUpType: SignUpType,
        @RequestParam code: String?,
    ): ResponseEntity<SaveResponseDTO> {
        val signUpService = signUpServiceFactory.getSignUpService(signUpType)
        log.info { "사인업서비스종류: $signUpService" }

        val memberId = when (signUpService) {
            is NormalSignUpService -> {
                if (signUpRequest.createMemberRequest == null) {
                    throw IllegalArgumentException("일반 회원가입 시 회원 정보가 필요합니다.")
                }
                signUpService.signUp(signUpRequest.createMemberRequest)
            }

            is KakaoSignUpService -> {
                log.info { "왜 아무것도 안나와" }
                // 액세스 토큰 받아오기
                val accessToken = kakaoLoginService.getAccessToken(code!!)

                log.info { "accressToken = ${accessToken.accessToken}" }

                // 사용자 정보 받아오기
                val userInfo = kakaoLoginService.getUserInfo(accessToken.accessToken!!)

                // kakaoMemberRequest의 이메일 필드 업데이트
                val updatedKakaoMemberRequest =
                    signUpRequest.kakaoMemberRequest!!.addEmail(userInfo.kakaoAccount!!.email!!)

                // 카카오 회원 가입
                signUpService.signUp(updatedKakaoMemberRequest)
            }

            else -> throw IllegalArgumentException("지원하지 않는 회원가입 유형입니다.")
        }

        val saveResponseDTO = SaveResponseDTO(
            id = memberId,
            statusCode = HttpStatus.CREATED.value(),
            message = "회원가입이 정상적으로 되었습니다.",
        )
        return ResponseEntity(saveResponseDTO, HttpStatus.CREATED)
    }

    @Operation(summary = "회원 로그인", description = "아이디와 비밀번호를 입력후 로그인을 진행합니다.")
    @PostMapping("/v1/members/login")
    fun login(@Validated @RequestBody memberLoginRequest: MemberLoginRequest?) {
        // 실제 로그인 로직은 Spring Security에서 처리
    }

    @Operation(summary = "이메일로 인증번호 전송", description = "기입한 이메일로 인증번호를 전송합니다.")
    @PostMapping("/v1/members/auth")
    fun signupEmailAuthentication(@Validated @RequestBody certifyNumRequest: CertifyNumRequest): ResponseEntity<MessageDTO> {
        memberServiceApplication.signupCertifyNumEmailSend(certifyNumRequest.email)

        val messageDTO = MessageDTO(
            HttpStatus.OK.value(),
            "'${certifyNumRequest.email}' 메일로 인증메일이 전송 되었습니다."
        )

        return ResponseEntity.ok(messageDTO)
    }
}
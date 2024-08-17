package org.store.clothstar.member.authentication.service

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import org.store.clothstar.common.config.mail.MailContentBuilder
import org.store.clothstar.common.config.mail.MailService
import org.store.clothstar.common.config.redis.RedisUtil
import org.store.clothstar.common.error.ErrorCode
import org.store.clothstar.common.error.exception.SignupCertifyNumAuthFailedException

class AuthenticationServiceKotest : BehaviorSpec({
    val mailContentBuilder: MailContentBuilder = mockk()
    val mailService: MailService = mockk()
    val redisUtil: RedisUtil = mockk()

    val authenticationService = AuthenticationService(mailContentBuilder, mailService, redisUtil)

    Given("회원가입을 할 때") {
        val certifyNum = "hi"
        val redisCertifyNum = "hello"
        every { redisUtil.getData(any()) } returns certifyNum

        When("인증번호가 틀리면") {
            val ex = shouldThrow<SignupCertifyNumAuthFailedException> {
                authenticationService.verifyEmailCertifyNum("test@naver.com", redisCertifyNum)
            }

            Then("인증번호가 잘못 되었습니다. 메시지를 응답한다.") {
                ex.message shouldBe ErrorCode.INVALID_AUTH_CERTIFY_NUM.message
                ex.message shouldBe "인증번호가 잘못 되었습니다."
            }
        }
    }
})

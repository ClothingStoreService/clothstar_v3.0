package org.store.clothstar.member.authentication.service

import io.github.oshai.kotlinlogging.KotlinLogging
import org.springframework.stereotype.Service
import org.store.clothstar.common.config.mail.MailContentBuilder
import org.store.clothstar.common.config.mail.MailSendDTO
import org.store.clothstar.common.config.mail.MailService
import org.store.clothstar.common.config.redis.RedisUtil
import org.store.clothstar.common.error.ErrorCode
import org.store.clothstar.common.error.exception.SignupCertifyNumAuthFailedException

@Service
class AuthenticationService(
    private val mailContentBuilder: MailContentBuilder,
    private val mailService: MailService,
    private val redisUtil: RedisUtil,
) {
    private val log = KotlinLogging.logger {}

    fun signupCertifyNumEmailSend(email: String) {
        sendEmailAuthentication(email)
        log.info { "인증번호 전송 완료, email = ${email}" }
    }

    private fun sendEmailAuthentication(toEmail: String): String {
        val certifyNum = redisUtil.createdCertifyNum()
        val message = mailContentBuilder.build(certifyNum)
        val mailSendDTO = MailSendDTO(toEmail, "clothstar 회원가입 인증 메일 입니다.", message)

        mailService.sendMail(mailSendDTO)

        //메일 전송에 성공하면 redis에 key = email, value = 인증번호를 생성한다.
        //지속시간은 10분
        redisUtil.createRedisData(toEmail, certifyNum)

        return certifyNum
    }

    fun verifyEmailCertifyNum(email: String, certifyNum: String) {
        val certifyNumFoundByRedis = redisUtil.getData(email)

        if (certifyNumFoundByRedis != certifyNum) {
            throw SignupCertifyNumAuthFailedException(ErrorCode.INVALID_AUTH_CERTIFY_NUM)
        }
    }
}
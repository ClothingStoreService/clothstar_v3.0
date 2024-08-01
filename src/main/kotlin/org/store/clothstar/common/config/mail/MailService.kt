package org.store.clothstar.common.config.mail

import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.mail.internet.MimeMessage
import org.springframework.beans.factory.annotation.Value
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.mail.javamail.MimeMessagePreparator
import org.springframework.stereotype.Service

@Service
class MailService(
    private val mailSender: JavaMailSender,
) {
    private val log = KotlinLogging.logger {}

    @Value("\${email.send}")
    private val fromAddress: String? = null

    fun sendMail(mailSendDTO: MailSendDTO): Boolean {
        val messagePreparator = MimeMessagePreparator { mimeMessage: MimeMessage? ->
            val messageHelper = MimeMessageHelper(mimeMessage, true, "UTF-8")
            messageHelper.setFrom(fromAddress)
            messageHelper.setTo(mailSendDTO.address)
            messageHelper.setSubject(mailSendDTO.subject)
            messageHelper.setText(mailSendDTO.text, true)
        }

        log.info { "전송 메일주소 : ${fromAddress} -> ${mailSendDTO.address}" }
        mailSender.send(messagePreparator)

        return true
    }
}
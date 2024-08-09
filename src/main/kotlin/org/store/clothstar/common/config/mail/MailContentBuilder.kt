package org.store.clothstar.common.config.mail

import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context

@Service
class MailContentBuilder(
    private val templateEngine: TemplateEngine
) {
    fun build(certifyNum: String): String {
        val context = Context()
        context.setVariable("certifyNum", certifyNum)
        return templateEngine.process("mailTemplate", context)
    }
}
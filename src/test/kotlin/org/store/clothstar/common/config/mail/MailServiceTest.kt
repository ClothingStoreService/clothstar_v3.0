package org.store.clothstar.common.config.mail

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
class MailServiceTest(
    @Autowired
    val mailService: MailService,

    @Autowired
    val mailContentBuilder: MailContentBuilder,
) {
    @DisplayName("메일 전송 테스트")
    @Test
    fun mailSendTest() {
        //given
        val link = "https://www.naver.com/"
        val message = mailContentBuilder.build(link)
        val mailSendDTO = MailSendDTO("test@test.com", "test", message)

        //when
        val success = mailService.sendMail(mailSendDTO)

        //then
        Assertions.assertThat(success).isTrue()
    }
}
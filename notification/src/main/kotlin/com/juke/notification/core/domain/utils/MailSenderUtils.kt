package com.juke.notification.core.domain.utils

import com.juke.notification.core.domain.failure.MessageSendFailure
import com.juke.notification.core.domain.failure.ServiceUnavailableFailure
import com.juke.notification.core.domain.model.Data
import com.juke.notification.core.domain.model.Data.Error
import com.juke.notification.core.domain.model.Data.Success
import org.slf4j.Logger
import org.springframework.stereotype.Component
import jakarta.mail.MessagingException
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper

@Component
class MailSenderUtils(
    private val logger: Logger,
    private val javaMailSender: JavaMailSender,
) {

    fun sendMail(to: String, subject: String, text: String): Data<Unit> {
        return try {
            val mimeMessage = javaMailSender.createMimeMessage()
            val helper = MimeMessageHelper(mimeMessage, "UTF-8")

            helper.setTo(to)
            helper.setSubject(subject)
            helper.setText(text, true)

            javaMailSender.send(mimeMessage)

            Success(Unit)
        } catch (e: MessagingException) {
            logger.error("Could not send message to $to", e)
            Error(MessageSendFailure())
        } catch (t: Throwable) {
            logger.error("Unexpected exception thrown", t)
            Error(ServiceUnavailableFailure())
        }
    }
}
package com.juke.notification.feature.mail.domain.success

enum class MailStatusEnum {
    MAIL_SENT;

    fun getMessage(): String {
        return "mail is sent"
    }
}
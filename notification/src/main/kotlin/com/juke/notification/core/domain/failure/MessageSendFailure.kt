package com.juke.notification.core.domain.failure

data class MessageSendFailure(
    override val code: Int = 500,
    override val status: String = "message_not_sent",
    override val message: String = "Service could not send message due to some reasons"
) : Failure

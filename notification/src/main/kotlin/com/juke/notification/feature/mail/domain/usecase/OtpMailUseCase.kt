package com.juke.notification.feature.mail.domain.usecase

import com.juke.notification.core.domain.behavior.UserBehavior
import com.juke.notification.core.domain.model.Data
import com.juke.notification.core.domain.model.Data.Error
import com.juke.notification.core.domain.success.StatusResponse
import com.juke.notification.core.domain.usecase.UseCase
import com.juke.notification.core.domain.utils.MailSenderUtils
import com.juke.notification.feature.mail.domain.success.MailStatusEnum.MAIL_SENT
import com.juke.notification.feature.mail.domain.usecase.OtpMailUseCase.OtpMailParams
import org.springframework.stereotype.Component

@Component
class OtpMailUseCase(
    private val mailSenderUtils: MailSenderUtils,
    private val userService: UserBehavior,
) : UseCase<OtpMailParams, StatusResponse> {

    data class OtpMailParams(
        val code: String,
        val email: String,
    )

    override suspend fun invoke(params: OtpMailParams): Data<StatusResponse> {
        val userData = userService.findByEmail(params.email)
        if (userData is Error) return Error(userData.failure)

        val mailData = mailSenderUtils.sendMail(
            to = params.email,
            subject = "Verification code",
            text = formatCodeText(params.code))
        if (mailData is Error) return Error(mailData.failure)

        return Data.Success(StatusResponse(MAIL_SENT.getMessage()))
    }

    private fun formatCodeText(code: String): String {
        return """
                    <html>
                    <body>
                    <p>Привет!</p>
                    <p>Твой код подтверждения: <span style="font-weight: bold;">$code</span></p>
                    <p>Код действителен 5 минут.</p>
                    <p>Данное сообщение сформировано автоматически, пожалуйста, не отвечай на него 🙂</p>
                    </body>
                    </html>
                    """
    }
}
CREATE TABLE IF NOT EXISTS OTP_CODES
(
    ID                  UUID           NOT NULL PRIMARY KEY DEFAULT GEN_RANDOM_UUID(),
    USER_ID             UUID           NOT NULL REFERENCES USERS(ID),
    CODE                VARCHAR(10)    NOT NULL,
    STATUS              VARCHAR(30)    NOT NULL
    CHECK (STATUS IN ('PENDING', 'CONFIRMED', 'REVOKED')),
    CREATED_AT          TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    EXPIRES_AT          TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP,
    UPDATED_AT          TIMESTAMP      NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX IDX_OTP_CODES_USER_ID ON OTP_CODES(USER_ID);
CREATE TABLE refresh_tokens (
                                id BIGSERIAL PRIMARY KEY,
                                system_user_id BIGINT NOT NULL REFERENCES system_users(id) ON DELETE CASCADE,
                                family_id UUID NOT NULL,
                                token_hash CHAR(64) NOT NULL UNIQUE,
                                issued_at TIMESTAMP NOT NULL DEFAULT now(),
                                expires_at TIMESTAMP NOT NULL,
                                revoked_at TIMESTAMP,
                                replaced_by_id BIGINT REFERENCES refresh_tokens(id) ON DELETE SET NULL,
                                user_agent VARCHAR(255),
                                ip_address VARCHAR(45)
);

CREATE INDEX ix_refresh_tokens_system_user ON refresh_tokens (system_user_id);
CREATE INDEX ix_refresh_tokens_family ON refresh_tokens (family_id);
CREATE INDEX ix_refresh_tokens_expires_at ON refresh_tokens (expires_at);
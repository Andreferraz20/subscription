CREATE TABLE system_users (
                              id BIGSERIAL PRIMARY KEY,
                              user_id BIGINT NOT NULL UNIQUE REFERENCES users(id) ON DELETE CASCADE,
                              login VARCHAR(255) NOT NULL,
                              password_hash VARCHAR(255) NOT NULL,
                              status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
                                  CHECK (status IN ('ACTIVE', 'LOCKED', 'DISABLED')),
                              failed_login_attempts SMALLINT NOT NULL DEFAULT 0,
                              locked_until TIMESTAMP,
                              last_login_at TIMESTAMP,
                              password_changed_at TIMESTAMP,
                              created_at TIMESTAMP NOT NULL DEFAULT now(),
                              updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX uq_system_users_login ON system_users (lower(login));
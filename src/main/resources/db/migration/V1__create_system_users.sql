CREATE TABLE system_users (
                              id BIGSERIAL PRIMARY KEY,
                              email VARCHAR(255) NOT NULL,
                              password_hash VARCHAR(255) NOT NULL,
                              status VARCHAR(20) NOT NULL DEFAULT 'ACTIVE'
                                  CHECK (status IN ('ACTIVE', 'LOCKED', 'DISABLED')),
                              failed_login_attempts INTEGER NOT NULL DEFAULT 0,
                              locked_until TIMESTAMP,
                              last_login_at TIMESTAMP,
                              password_changed_at TIMESTAMP,
                              created_at TIMESTAMP NOT NULL DEFAULT now(),
                              updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX uq_system_users_email ON system_users (lower(email));

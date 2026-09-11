CREATE TABLE users (
                       id BIGSERIAL PRIMARY KEY,
                       system_user_id BIGINT NOT NULL UNIQUE REFERENCES system_users(id) ON DELETE CASCADE,
                       first_name VARCHAR(150) NOT NULL,
                       last_name VARCHAR(150) NOT NULL,
                       document VARCHAR(20),
                       phone VARCHAR(20),
                       birth_date DATE,
                       created_at TIMESTAMP NOT NULL DEFAULT now(),
                       updated_at TIMESTAMP NOT NULL DEFAULT now()
);

CREATE UNIQUE INDEX uq_users_document ON users (document) WHERE document IS NOT NULL;
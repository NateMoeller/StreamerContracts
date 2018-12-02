CREATE TABLE user_settings (
    id UUID PRIMARY KEY,
    user_id UUID REFERENCES users (id) NOT NULL,
    paypal_email VARCHAR(128) UNIQUE,
    created_at TIMESTAMP NOT NULL,
    updated_at TIMESTAMP NOT NULL
);
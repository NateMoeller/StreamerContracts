CREATE TABLE payment_info (
    id BIGSERIAL PRIMARY KEY,
	user_id BIGINT REFERENCES users (id),
	paypal_username TEXT NOT NULL UNIQUE
);
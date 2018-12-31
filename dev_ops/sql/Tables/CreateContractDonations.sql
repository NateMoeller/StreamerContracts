CREATE TABLE contract_donations (
    id UUID PRIMARY KEY,
	contract_id UUID REFERENCES contracts (id),
	donator_id UUID REFERENCES users (id),
	donation_amount MONEY CONSTRAINT positive_donation CHECK (donation_amount > 0::money) NOT NULL,
	created_at TIMESTAMP NOT NULL,
	paypal_payment_id VARCHAR(128) NOT NULL,
	paypal_authorization_id VARCHAR(128) NOT NULL,
	released_at TIMESTAMP,
	donation_state SMALLINT
);
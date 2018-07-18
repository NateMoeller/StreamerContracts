CREATE TABLE contract_donations (
    id BIGSERIAL PRIMARY KEY,
	contract_id BIGINT REFERENCES contracts (id),
	donator_id BIGINT REFERENCES users (id),
	donation_amount MONEY CONSTRAINT positive_donation CHECK (donation_amount > 0::money) NOT NULL,
	donation_timestamp TIMESTAMP NOT NULL
);
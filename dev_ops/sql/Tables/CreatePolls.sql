CREATE TABLE polls (
    id BIGSERIAL PRIMARY KEY,
	contract_id BIGINT REFERENCES contracts (id),
	is_open BOOLEAN NOT NULL
);
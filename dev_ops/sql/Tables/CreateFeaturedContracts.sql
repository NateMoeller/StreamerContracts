CREATE TABLE featured_contracts (
    id BIGSERIAL PRIMARY KEY,
	contract_id BIGINT REFERENCES contracts (id),
	hype_text TEXT NOT NULL
);
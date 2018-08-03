CREATE TABLE featured_contracts (
    id UUID PRIMARY KEY,
	contract_id UUID REFERENCES contracts (id),
	hype_text TEXT NOT NULL,
	created_at TIMESTAMP NOT NULL,
	updated_at TIMESTAMP NOT NULL
);
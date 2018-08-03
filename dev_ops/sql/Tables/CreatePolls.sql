CREATE TABLE polls (
    id UUID PRIMARY KEY,
	contract_id UUID REFERENCES contracts (id),
	is_open BOOLEAN NOT NULL
);
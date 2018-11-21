CREATE TABLE votes (
    id UUID PRIMARY KEY,
	contract_id UUID REFERENCES contracts (id),
	voter_id UUID REFERENCES users (id),
	viewer_flagged_complete BOOLEAN NOT NULL,
	voted_at TIMESTAMP NOT NULL
);
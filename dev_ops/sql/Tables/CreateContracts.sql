CREATE TABLE contracts (
    id UUID PRIMARY KEY,
	proposer_id UUID REFERENCES users (id) NOT NULL,
	streamer_id UUID REFERENCES users (id) NOT NULL,
	game VARCHAR(128) NULL,
	description TEXT NOT NULL,
	proposed_at TIMESTAMP NOT NULL,
	accepted_at TIMESTAMP NULL,
	expires_at TIMESTAMP NOT NULL,
	completed_at TIMESTAMP NULL,
	is_accepted BOOLEAN NOT NULL,
	is_completed BOOLEAN NOT NULL,
	is_community_contract BOOLEAN NOT NULL,
	CONSTRAINT valid_timestamps CHECK (
	    proposed_at < accepted_at AND -- the contract must be accepted after its proposed
		accepted_at < expires_at AND -- the contract must be accepted before it is expired
		completed_at <= expires_at AND -- the contract must be completed before it is expired
		completed_at > accepted_at)  -- if the contract is completed, it must be completed after its accepted
);
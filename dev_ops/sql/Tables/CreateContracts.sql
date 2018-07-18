CREATE TABLE contracts (
    id BIGSERIAL PRIMARY KEY,
	proposer_id BIGINT REFERENCES users (id),
	streamer_id BIGINT REFERENCES users (id),
	game TEXT NOT NULL,
	description TEXT NOT NULL,
	proposed_timestamp TIMESTAMP NOT NULL,
	accepted_timestamp TIMESTAMP NOT NULL,
	expiration_timestamp TIMESTAMP NOT NULL,
	completed_timestamp TIMESTAMP NOT NULL,
	is_accepted BOOLEAN NOT NULL,
	is_completed BOOLEAN NOT NULL,
	is_community_contract BOOLEAN NOT NULL,
	CONSTRAINT valid_timestamps CHECK (
	    proposed_timestamp < accepted_timestamp AND -- the contract must be accepted after its proposed
		accepted_timestamp < expiration_timestamp AND -- the contract must be accepted before it is expired
		completed_timestamp <= expiration_timestamp AND -- the contract must be completed before it is expired
		completed_timestamp > accepted_timestamp)  -- if the contract is completed, it must be completed after its accepted
);
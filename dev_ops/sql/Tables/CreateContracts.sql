CREATE TABLE contracts (
    id UUID PRIMARY KEY,
	proposer_id UUID REFERENCES users (id) NOT NULL,
	streamer_id UUID REFERENCES users (id) NOT NULL,
	game VARCHAR(128) NULL,
	description TEXT NOT NULL,
	proposed_at TIMESTAMP NOT NULL,
	activated_at TIMESTAMP NULL,
	deactivated_at TIMESTAMP NULL,
	declined_at TIMESTAMP NULL,
	settles_at TIMESTAMP NOT NULL,
	expired_at TIMESTAMP NULL,
	completed_at TIMESTAMP NULL,
	failed_at TIMESTAMP NULL,
	disputed_at TIMESTAMP NULL,
	is_community_contract BOOLEAN NOT NULL,
	state SMALLINT NOT NULL,
	dev_note TEXT NULL,
	CONSTRAINT valid_timestamps CHECK (
	    proposed_at < activated_at AND -- if the contract is activated, it must be activated after its proposed
		proposed_at < declined_at AND -- if the contract is declined, it must be declined after its proposed
		proposed_at < expired_at) -- if the contract is expired, it must be expired after its proposed
);
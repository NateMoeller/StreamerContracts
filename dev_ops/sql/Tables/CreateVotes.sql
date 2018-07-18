CREATE TABLE votes (
    id BIGSERIAL PRIMARY KEY,
	poll_id BIGINT REFERENCES polls (id),
	voter_id BIGINT REFERENCES users (id),
	vote BOOLEAN NOT NULL, -- True represents the vote that the streamer completed the contract
	vote_timestmap TIMESTAMP NOT NULL
);
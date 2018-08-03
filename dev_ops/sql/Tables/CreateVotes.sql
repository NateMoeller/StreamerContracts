CREATE TABLE votes (
    id UUID PRIMARY KEY,
	poll_id UUID REFERENCES polls (id),
	voter_id UUID REFERENCES users (id),
	viewer_flagged_complete BOOLEAN NOT NULL,
	voted_at TIMESTAMP NOT NULL
);
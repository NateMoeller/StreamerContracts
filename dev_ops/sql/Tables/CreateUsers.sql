CREATE TABLE users (
    id UUID PRIMARY KEY,
	twitch_username VARCHAR(128) UNIQUE,
	created_at TIMESTAMP NOT NULL,
	last_login TIMESTAMP NOT NULL,
	total_logins INT CONSTRAINT nonnegative_logins CHECK (total_logins >= 0) NOT NULL,
	CHECK (last_login >= created_at)
);
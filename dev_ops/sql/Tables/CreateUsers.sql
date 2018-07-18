CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
	twitch_username TEXT UNIQUE,
	mixer_username TEXT UNIQUE,
	youtube_username TEXT UNIQUE,
	create_timestamp TIMESTAMP NOT NULL,
	last_login TIMESTAMP NOT NULL,
	total_logins INT CONSTRAINT nonnegative_logins CHECK (total_logins >= 0) NOT NULL,
	CHECK (last_login >= create_timestamp)
);
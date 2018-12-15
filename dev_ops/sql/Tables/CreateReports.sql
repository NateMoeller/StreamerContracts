CREATE TABLE reports (
	id UUID PRIMARY KEY,
	reporting_user_id UUID REFERENCES users (id),
	report VARCHAR(2048) NOT NULL,
	contact_email VARCHAR(256) NOT NULL,
	dev_notes VARCHAR(2048),
	created_at TIMESTAMP NOT NULL,
	updated_at TIMESTAMP NOT NULL
);
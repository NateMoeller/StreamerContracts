CREATE DATABASE StreamerContractsDB;

REVOKE ALL PRIVILEGES ON ALL TABLES IN SCHEMA public FROM application_user;

REVOKE ALL PRIVILEGES ON DATABASE streamercontractsdb FROM application_user;

DROP USER application_user;

CREATE USER application_user WITH ENCRYPTED PASSWORD 'password';

GRANT ALL PRIVILEGES ON DATABASE streamercontractsdb TO application_user;

GRANT ALL PRIVILEGES ON ALL TABLES IN SCHEMA public TO application_user;
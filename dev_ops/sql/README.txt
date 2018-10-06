README

This README describes how to setup POSTGRES for local development.

1. First, download and install POSTGRES from this link: https://www.postgresql.org/download/windows/
Make sure you download and install postgres 10.

2. Postgres queries can be executed from either pgAdmin 4 or psql.
psql default path - C:\Program Files\PostgreSQL\10\bin
pgAdmin 4 default path - C:\Program Files\PostgreSQL\10\pgAdmin 4\bin

pgAdmin 4 is a web dashboard for executing postgres queries. psql is the command line version. I would recommend adding psql to your path.

3. Run the setup scripts

Use the password that you specified when you setup the postgres instance
psql --username=postgres --file=<root>/dev_ops/sql/CreateDatabase.sql

Use 'password' to run this command
psql --username=dbadmin --dbname=streamercontractsdb --file=<root>/dev_ops/sql/CreateSchema.sql

Done!
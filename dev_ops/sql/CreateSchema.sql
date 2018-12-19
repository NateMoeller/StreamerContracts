-- Creates all of the tables in the database
-- Need to use Linux style slashes, even on Windows

BEGIN;
\i 'dev_ops/sql/Tables/CreateUsers.sql'
\i 'dev_ops/sql/Tables/CreateContracts.sql'
\i 'dev_ops/sql/Tables/CreateContractDonations.sql'
\i 'dev_ops/sql/Tables/CreateVotes.sql'
\i 'dev_ops/sql/Tables/CreateFeaturedContracts.sql'
\i 'dev_ops/sql/Tables/CreateEmails.sql'
\i 'dev_ops/sql/Tables/CreateUserSettings.sql'
\i 'dev_ops/sql/Tables/CreateReports.sql'
COMMIT;
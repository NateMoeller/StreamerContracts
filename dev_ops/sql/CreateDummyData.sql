-- Creates dummy data in the database
-- Need to use Linux style slashes, even on Windows
-- Run this script from project root directory (i.e: )

BEGIN;
\i 'dev_ops/sql/dev_scripts/CreateDummyUsers.sql'
\i 'dev_ops/sql/dev_scripts/CreateDummyContracts.sql'
\i 'dev_ops/sql/dev_scripts/CreateDummyDonations.sql'
COMMIT;
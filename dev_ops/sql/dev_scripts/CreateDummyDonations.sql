CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE OR REPLACE FUNCTION createDonations() RETURNS void AS
$func$
DECLARE
    challenges CURSOR FOR SELECT id, proposer_id, proposed_at FROM contracts;
BEGIN
	FOR tbl_record IN challenges LOOP
		INSERT INTO contract_donations VALUES (uuid_generate_v1(), tbl_record.id, tbl_record.proposer_id, (random() * 100)::numeric::money, tbl_record.proposed_at);
	END LOOP;
END;

$func$ LANGUAGE plpgsql;

select createDonations();
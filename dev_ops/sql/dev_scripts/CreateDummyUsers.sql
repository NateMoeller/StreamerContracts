CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE OR REPLACE FUNCTION createUsers() RETURNS void AS
$func$
DECLARE
   streamers text[] := '{"Ninja","NICKMERCS","shroud","summit1g","TimTheTatman","TSM_Myth","CDNThe3rd","Vivid","Snipe3down","masoncobb","DrLupo","Hysteria","Fearitself","Heinz","DrDisRespectLIVE","rudeboi2hot","LEGIQN","Halo","nickmercs","Walshy","Tsquared"}';
BEGIN
    FOR i in 1..array_length(streamers, 1) LOOP
	    INSERT INTO users VALUES (uuid_generate_v1(), streamers[i], NOW(), NOW(), 1);
    END LOOP;
END;

$func$ LANGUAGE plpgsql;

select createUsers();

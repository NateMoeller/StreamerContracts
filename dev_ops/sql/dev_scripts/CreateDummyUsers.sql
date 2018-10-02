CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE OR REPLACE FUNCTION createUsers() RETURNS void AS
$func$
DECLARE
   adjectives text[] := '{"Crazy","Lazy","Hazy","Salty","Tender","Fluid","Tilted","Pleasant","Short","Tough"}';
   nouns text[] := '{"Horse","Bitch","Nick","Nathan","Justin","Potato","Cookie","Wing","Hippo","Dog","Phone"}';
BEGIN
    FOR i in 1..array_length(adjectives, 1) LOOP
	    FOR j in 1..array_length(nouns, 1) LOOP
		    INSERT INTO users VALUES (uuid_generate_v1(), CONCAT(adjectives[i], nouns[j]), NOW(), NOW(), 1);
		END LOOP;
    END LOOP;
END;

$func$ LANGUAGE plpgsql;

select createUsers();

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE OR REPLACE FUNCTION createUsers() RETURNS void AS '
DECLARE numUsers INTEGER := 10;

BEGIN
    FOR i in 0.. numUsers LOOP
            INSERT INTO users VALUES (uuid_generate_v1(), md5(random()::text), NOW(), NOW(), 1);
    END LOOP;
END;
' LANGUAGE plpgsql;

select createUsers();

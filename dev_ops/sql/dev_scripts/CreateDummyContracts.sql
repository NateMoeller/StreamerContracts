CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE OR REPLACE FUNCTION createContracts() RETURNS void AS
$func$
DECLARE
   numContracts INTEGER := 15;
   random_user1 UUID;
   random_user2 UUID;
   descriptions text[] := '{"Win this game!",
                            "Kill 10 players this game.",
							"Kill Ninja this game.",
							"Kill a player by pick axing them.",
							"Dont take any damage this game.",
							"Use no materials this game.",
							"Use only gray weapons.",
							"Use only green weapons.",
							"Use only blue weapons.",
							"Use only purple weapons.",
							"Use only gold weapons.",
							"Dont build with wood at all.",
							"Win this solo squad game.",
							"15 kills?.",
							"Dont die this game.",
							"30 bomb.",
							"Walk the width of the map.",
							"Use every gun in the game.",
							"Dont talk to your teammates at all.",
							"Win this game with 0 kills.",
							"Win this game listening to nothing but Kesha in the background."}';
							
	games text[] :=       '{"Fortnite",
                            "Halo 5 Guardians",
							"Halo MCC",
							"Player Unknowns Battlegrounds",
							"Call of Duty Black Ops 4"}';
BEGIN
    FOR i in 1..numContracts LOOP
	    random_user1 := (select id from users ORDER BY random() LIMIT 1);
		random_user2 := (select id from users WHERE id != random_user1 ORDER BY random() LIMIT 1);
		INSERT INTO contracts VALUES (uuid_generate_v1(), random_user1, random_user2, games[floor(random() * ((array_length(games, 1))-1+1) + 1)], descriptions[floor(random() * ((array_length(descriptions, 1))-1+1) + 1)], NOW(), NULL, NULL, NOW() + interval '3' day, NULL, NULL, NULL, NULL, false, 0, NULL);
    END LOOP;
END;

$func$ LANGUAGE plpgsql;

select createContracts();

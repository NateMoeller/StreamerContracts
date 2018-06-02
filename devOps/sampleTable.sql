BEGIN;

CREATE TABLE example_table (
    example_id uuid,
    example_value varchar(255)
);

INSERT INTO example_table (example_id, example_value)
VALUES ('92950a04-6606-11e8-adc0-fa7ae01bbebc', 'AWS-RDS stored value');

COMMIT;
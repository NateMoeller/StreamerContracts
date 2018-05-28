BEGIN;

CREATE TABLE sampleTable (
    sampleId int,
    sampleValue varchar(255)
);

INSERT INTO sampleTable (sampleId, sampleValue)
VALUES (1, 'AWS-RDS stored value');

COMMIT;
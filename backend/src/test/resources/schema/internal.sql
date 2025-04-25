DROP TABLE IF EXISTS results CASCADE;

CREATE TABLE results
(
    id         BIGSERIAL PRIMARY KEY,
    timestamp  TIMESTAMPTZ     NOT NULL DEFAULT now(),
    check_name VARCHAR(255)    NOT NULL,
    result     NUMERIC(38, 16) NOT NULL
);

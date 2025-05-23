DROP TABLE IF EXISTS results CASCADE;

CREATE TABLE results
(
    id             BIGSERIAL PRIMARY KEY,
    timestamp      TIMESTAMPTZ     NOT NULL DEFAULT now(),
    check_name     VARCHAR         NOT NULL,
    result         NUMERIC(38, 16) NOT NULL,
    execution_time BIGINT          NOT NULL
);

CREATE INDEX idx_check_name ON results (check_name);

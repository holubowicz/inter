DROP TABLE IF EXISTS calculations CASCADE;

CREATE TABLE calculations
(
    id     SERIAL PRIMARY KEY,
    date   DATE,
    number NUMERIC(5, 2)
);

INSERT INTO calculations (date, number)
VALUES
    ('1981-07-12', 18.35),
    ('1981-07-13', 18.98),
    ('1981-07-14', -1.5),
    ('1981-07-15', -27.03),
    ('1981-07-16', -21.14),
    ('1981-07-17', -21.35),
    ('1981-07-18', 81.13),
    ('1981-07-19', 89.22),
    ('1981-07-20', -74.24),
    ('1981-07-21', -89.72),
    ('1981-07-22', 74.2),
    ('1981-07-23', -29.21),
    ('1981-07-24', -2.32),
    ('1981-07-25', -12.55);
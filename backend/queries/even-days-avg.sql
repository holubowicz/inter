SELECT AVG(number) AS value
FROM calculations
WHERE EXTRACT(DAY FROM date) % 2 = 0;
SELECT AVG(number) AS value
FROM calculations
WHERE EXTRACT(MONTH FROM date) IN (6, 7, 8);
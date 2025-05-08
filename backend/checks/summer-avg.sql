SELECT AVG(number) AS result
FROM calculations
WHERE EXTRACT(MONTH FROM date) IN (6, 7, 8)
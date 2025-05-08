SELECT AVG(number) AS result
FROM calculations
WHERE EXTRACT(DAY FROM date) % 2 = 0
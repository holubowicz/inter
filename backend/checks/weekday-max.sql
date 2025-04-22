SELECT MAX(number) AS result
FROM calculations
WHERE EXTRACT(DOW FROM date) BETWEEN 1 AND 5;
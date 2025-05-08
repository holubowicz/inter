SELECT MIN(number) AS result
FROM calculations
WHERE EXTRACT(DOW FROM date) IN (0, 6)
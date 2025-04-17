SELECT MIN(number) AS value
FROM calculations
WHERE EXTRACT(DOW FROM date) IN (0, 6);
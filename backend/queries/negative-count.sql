SELECT SUM(CASE WHEN number < 0 THEN 1 ELSE 0 END) AS value
FROM calculations;
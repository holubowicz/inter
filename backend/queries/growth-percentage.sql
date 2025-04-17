SELECT 
  ((SELECT number FROM calculations ORDER BY date DESC LIMIT 1) - 
   (SELECT number FROM calculations ORDER BY date ASC LIMIT 1)) / 
   ABS((SELECT number FROM calculations ORDER BY date ASC LIMIT 1)) * 100 AS value
FROM calculations
LIMIT 1;
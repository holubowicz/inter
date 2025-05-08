SELECT 
  (SELECT number FROM calculations ORDER BY date ASC LIMIT 1) - 
  (SELECT number FROM calculations ORDER BY date DESC LIMIT 1) AS result
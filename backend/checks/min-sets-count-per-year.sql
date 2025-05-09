SELECT COUNT(set_num) AS sets_count
FROM sets
GROUP BY year
ORDER BY sets_count LIMIT 1
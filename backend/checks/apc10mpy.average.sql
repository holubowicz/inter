WITH raw_data AS (SELECT SUM(num_parts) AS parts_num
              FROM sets
              GROUP BY year
ORDER BY parts_num DESC LIMIT 10
    )
SELECT AVG(parts_num)
FROM raw_data
SELECT COUNT(part_num) AS parts_count
FROM parts
GROUP BY part_cat_id
ORDER BY parts_count LIMIT 1
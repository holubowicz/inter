WITH raw_data AS (SELECT *, lt.id AS lt_id
                  FROM sets ls
                           FULL JOIN themes lt ON ls.theme_id = lt.id
                           FULL JOIN themes ltp ON ltp.id = lt.parent_id
                           FULL JOIN themes lt2 ON ls.num_parts = lt2.id
                           FULL JOIN parts lp ON ls.theme_id = lp.part_cat_id)
SELECT AVG(rd.num_parts)
FROM raw_data rd
         FULL JOIN raw_data rd2 ON rd.lt_id = rd2.lt_id

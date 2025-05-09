WITH raw_data AS (SELECT *, lt.id AS lt_id
                  FROM lego_sets ls
                           FULL JOIN lego_themes lt ON ls.theme_id = lt.id
                           FULL JOIN lego_themes ltp ON ltp.id = lt.parent_id
                           FULL JOIN lego_themes lt2 ON ls.num_parts = lt2.id
                           FULL JOIN lego_parts lp ON ls.theme_id = lp.part_cat_id)
SELECT avg(rd.num_parts)
FROM raw_data rd
         FULL JOIN raw_data rd2 ON rd.lt_id = rd2.lt_id

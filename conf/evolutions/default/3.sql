# --- !Ups
-- パスワードは"nextbeat"
INSERT INTO `users` VALUES ('1', '相生 葵', 'aoi@example.com', 'b0b1f71190c0ea7018dbbd96a930c3fab63be447d98349de78a82cc6a5964076', '3kRPLNNnyUBI4QLFV9b4', CAST('2018-04-01 00:00:00' AS DATETIME), CAST('2018-04-01 00:00:00' AS DATETIME));
INSERT INTO `users` VALUES ('2', '阿江 愛', 'ai@example.com', '2fdc314e91c6a00394885dba26c6fed19adf9adb367e653a9849d12bdbc908fc', 'Sh72PWhmjIdw79G7h9CS', CAST('2018-04-01 00:00:00' AS DATETIME), CAST('2018-04-01 00:00:00' AS DATETIME));

INSERT INTO `tasks` VALUES ('1', 'Scalaの勉強', TRUE, '1', CAST('2018-04-01 00:00:00' AS DATETIME), CAST('2018-04-01 00:00:00' AS DATETIME), CAST('2018-04-01 00:00:00' AS DATETIME));
INSERT INTO `tasks` VALUES ('2', 'Playの勉強', FALSE, '1', CAST('2018-04-01 00:00:00' AS DATETIME), CAST('2018-04-01 00:00:00' AS DATETIME), CAST('2018-04-01 00:00:00' AS DATETIME));
INSERT INTO `tasks` VALUES ('3', '英語の勉強', FALSE, '1', NULL, CAST('2018-04-01 00:00:00' AS DATETIME), CAST('2018-04-01 00:00:00' AS DATETIME));
INSERT INTO `tasks` VALUES ('4', 'たまご買う', TRUE, '2', CAST('2018-04-01 00:00:00' AS DATETIME), CAST('2018-04-01 00:00:00' AS DATETIME), CAST('2018-04-01 00:00:00' AS DATETIME));
INSERT INTO `tasks` VALUES ('5', 'ねぎ買う', FALSE, '2', CAST('2018-04-01 00:00:00' AS DATETIME), CAST('2018-04-01 00:00:00' AS DATETIME), CAST('2018-04-01 00:00:00' AS DATETIME));

# --- !Downs
DELETE FROM `tasks`;
DELETE FROM `users`;

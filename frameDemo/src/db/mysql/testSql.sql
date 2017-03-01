DROP TABLE IF EXISTS `tb_test`;
CREATE TABLE `tb_test` (
  `id` char(32) NOT NULL,
  `name` varchar(20) COLLATE utf8_bin NOT NULL,
  `url` varchar(200) COLLATE utf8_bin DEFAULT NULL,

  `add_date` date DEFAULT NULL,
  `add_user` char(32) COLLATE utf8_bin DEFAULT NULL,
  `edit_date` date DEFAULT NULL,
  `edit_user` char(32) COLLATE utf8_bin DEFAULT NULL,
  `activate` char(1) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
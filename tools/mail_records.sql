DROP TABLE IF EXISTS `mail_records`;
CREATE TABLE `mail_records` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `subject` varchar(128) NOT NULL COMMENT '邮件主题',
  `receivers` varchar(256) NOT NULL COMMENT '接收人',
  `content` mediumtext NOT NULL COMMENT '邮件正文不可超过15M',
  `source` varchar(128) DEFAULT '监控平台' COMMENT '邮件发送方名称',
  `send_time` int(10) unsigned NOT NULL COMMENT 'unix时间',
  `send_result` tinyint(4) DEFAULT '0' COMMENT '0->成功; others->失败',
  PRIMARY KEY (`id`),
  KEY `send_time` (`send_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

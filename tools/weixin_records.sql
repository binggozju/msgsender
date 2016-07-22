DROP TABLE IF EXISTS `weixin_records`;
CREATE TABLE `weixin_records` (
  `id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `receivers` varchar(256) NOT NULL,
  `content` varchar(1024) NOT NULL,
  `source` varchar(128) DEFAULT '监控平台' COMMENT '微信发送方名称',
  `send_time` int(10) unsigned NOT NULL COMMENT 'unix时间',
  `send_result` tinyint(4) DEFAULT '0' COMMENT '0->成功; others->失败',
  PRIMARY KEY (`id`),
  KEY `send_time` (`send_time`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

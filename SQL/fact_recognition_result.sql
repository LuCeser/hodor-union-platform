/*
Navicat MySQL Data Transfer

Source Database       : hodor

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2019-01-11 14:37:47
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for fact_recognition_result
-- ----------------------------
DROP TABLE IF EXISTS `fact_recognition_result`;
CREATE TABLE `fact_recognition_result` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `file_id` int(11) unsigned NOT NULL DEFAULT '0',
  `engine` int(11) NOT NULL DEFAULT '0',
  `duration` int(11) NOT NULL DEFAULT '0',
  `result` longtext,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;

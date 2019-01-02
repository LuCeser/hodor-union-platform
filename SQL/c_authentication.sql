/*
Navicat MySQL Data Transfer

Source Database       : hodor

Target Server Type    : MYSQL
Target Server Version : 50505
File Encoding         : 65001

Date: 2019-01-02 14:10:57
*/

SET FOREIGN_KEY_CHECKS=0;

-- ----------------------------
-- Table structure for c_authentication
-- ----------------------------
DROP TABLE IF EXISTS `c_authentication`;
CREATE TABLE `c_authentication` (
  `id` char(36) NOT NULL,
  `engine` smallint(2) NOT NULL,
  `app_access_key` varchar(50) NOT NULL,
  `app_access_secret` varchar(50) NOT NULL,
  `app_id` varchar(50) NOT NULL,
  `extend_info` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

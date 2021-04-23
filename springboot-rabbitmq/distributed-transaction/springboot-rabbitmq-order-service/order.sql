/*
SQLyog  v12.2.6 (64 bit)
MySQL - 5.7.33 : Database - test_order
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`test_order` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `test_order`;

/*Table structure for table `mq_order_message` */

DROP TABLE IF EXISTS `mq_order_message`;

CREATE TABLE `mq_order_message` (
  `id` bigint(12) NOT NULL AUTO_INCREMENT COMMENT '订单消息冗余表ID',
  `order_id` varchar(100) DEFAULT NULL COMMENT '订单ID',
  `msg_status` int(12) DEFAULT '0' COMMENT '消息状态，默认为0：未投递队列，1：已入列，2：在重试次数大于2还是异常',
  `order_content` varchar(255) DEFAULT NULL COMMENT '订单内容',
  `retry_count` int(4) DEFAULT '0' COMMENT '重试次数',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;

/*Table structure for table `order` */

DROP TABLE IF EXISTS `order`;

CREATE TABLE `order` (
  `order_id` varchar(100) NOT NULL COMMENT '订单ID',
  `user_id` int(32) DEFAULT NULL COMMENT '用户ID',
  `order_content` varchar(255) DEFAULT NULL COMMENT '订单内容',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`order_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

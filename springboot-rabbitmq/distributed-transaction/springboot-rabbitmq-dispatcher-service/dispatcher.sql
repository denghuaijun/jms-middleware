/*
SQLyog  v12.2.6 (64 bit)
MySQL - 5.7.33 : Database - test_dispatcher
*********************************************************************
*/

/*!40101 SET NAMES utf8 */;

/*!40101 SET SQL_MODE=''*/;

/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;
CREATE DATABASE /*!32312 IF NOT EXISTS*/`test_dispatcher` /*!40100 DEFAULT CHARACTER SET utf8 */;

USE `test_dispatcher`;

/*Table structure for table `dispatcher` */

DROP TABLE IF EXISTS `dispatcher`;

CREATE TABLE `dispatcher` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT COMMENT '运单ID',
  `order_id` varchar(100) DEFAULT NULL COMMENT '订单ID',
  `user_id` varchar(100) DEFAULT NULL COMMENT '用户ID',
  `order_content` varchar(255) DEFAULT NULL COMMENT '订单内容',
  `status` varchar(32) DEFAULT NULL COMMENT '运送状态',
  `create_time` datetime DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

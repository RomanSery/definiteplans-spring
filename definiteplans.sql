-- MySQL dump 10.13  Distrib 8.0.16, for Win64 (x86_64)
--
-- Host: localhost    Database: definiteplans
-- ------------------------------------------------------
-- Server version	8.0.16

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
 SET NAMES utf8 ;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `blocked_user`
--

DROP TABLE IF EXISTS `blocked_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `blocked_user` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(10) unsigned NOT NULL,
  `blocked_user_id` int(10) unsigned NOT NULL,
  `blocked_date` datetime NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `chat_msg`
--

DROP TABLE IF EXISTS `chat_msg`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `chat_msg` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `from_id` int(11) NOT NULL,
  `to_id` int(11) NOT NULL,
  `sent_date` datetime NOT NULL,
  `message` text NOT NULL,
  `is_read` bit(1) NOT NULL DEFAULT b'0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `definite_date`
--

DROP TABLE IF EXISTS `definite_date`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `definite_date` (
  `date_id` int(10) unsigned NOT NULL AUTO_INCREMENT,
  `doing_what` varchar(45) NOT NULL,
  `location_name` varchar(45) NOT NULL,
  `doing_when` datetime NOT NULL,
  `owner_id` int(10) unsigned NOT NULL,
  `datee_id` int(10) unsigned NOT NULL,
  `created_date` datetime NOT NULL,
  `owner_last_update` datetime NOT NULL,
  `datee_last_update` datetime DEFAULT NULL,
  `owner_status_id` tinyint(3) unsigned NOT NULL,
  `datee_status_id` tinyint(3) unsigned NOT NULL,
  `email_reminder_sent` bit(1) DEFAULT NULL,
  `post_date_email_sent` bit(1) DEFAULT NULL,
  `date_status_id` tinyint(3) unsigned NOT NULL,
  `owner_wants_more` bit(1) DEFAULT NULL,
  `datee_wants_more` bit(1) DEFAULT NULL,
  `owner_was_safe` bit(1) DEFAULT NULL,
  `datee_was_safe` bit(1) DEFAULT NULL,
  `owner_feedback` varchar(100) DEFAULT NULL,
  `datee_feedback` varchar(100) DEFAULT NULL,
  `owner_gave_feedback` bit(1) DEFAULT NULL,
  `datee_gave_feedback` bit(1) DEFAULT NULL,
  `greeting_msg` varchar(300) NOT NULL,
  `owner_no_show` bit(1) DEFAULT NULL,
  `datee_no_show` bit(1) DEFAULT NULL,
  PRIMARY KEY (`date_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `enum_value`
--

DROP TABLE IF EXISTS `enum_value`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `enum_value` (
  `id` smallint(5) unsigned NOT NULL AUTO_INCREMENT,
  `type` smallint(5) unsigned NOT NULL,
  `value` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=127 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `fb_user`
--

DROP TABLE IF EXISTS `fb_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `fb_user` (
  `user_id` int(11) NOT NULL,
  `fb_id` varchar(100) NOT NULL,
  `access_token` text,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `google_user`
--

DROP TABLE IF EXISTS `google_user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `google_user` (
  `user_id` int(11) NOT NULL,
  `sub_id` varchar(100) NOT NULL,
  `access_token` text,
  `id_token` text,
  `img_url` varchar(200) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sent_email`
--

DROP TABLE IF EXISTS `sent_email`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `sent_email` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `email_subject` varchar(500) DEFAULT NULL,
  `email_body` text,
  `from_addr` varchar(100) DEFAULT NULL,
  `to_addr` varchar(200) DEFAULT NULL,
  `sent_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `unsubscriber`
--

DROP TABLE IF EXISTS `unsubscriber`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `unsubscriber` (
  `email` varchar(100) NOT NULL,
  `unsub_date` datetime NOT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `user_status_id` tinyint(4) NOT NULL,
  `email` varchar(200) NOT NULL,
  `display_name` varchar(200) DEFAULT NULL,
  `pwd` varchar(200) DEFAULT NULL,
  `postal_code` char(5) DEFAULT NULL,
  `creation_date` datetime NOT NULL,
  `last_modified_date` datetime NOT NULL,
  `dob` datetime DEFAULT NULL,
  `about_me` varchar(300) DEFAULT NULL,
  `interests` varchar(300) DEFAULT NULL,
  `gender` smallint(5) unsigned NOT NULL,
  `languages` varchar(45) DEFAULT NULL,
  `state` char(2) DEFAULT NULL,
  `city` varchar(45) DEFAULT NULL,
  `neighborhood` varchar(45) DEFAULT NULL,
  `country` smallint(5) unsigned NOT NULL,
  `ethnicity` smallint(5) unsigned NOT NULL,
  `height` tinyint(3) unsigned NOT NULL,
  `smokes` smallint(5) unsigned NOT NULL,
  `religion` smallint(5) unsigned NOT NULL,
  `education` smallint(5) unsigned NOT NULL,
  `income` smallint(5) unsigned NOT NULL,
  `kids` smallint(5) unsigned NOT NULL,
  `wants_kids` smallint(5) unsigned NOT NULL,
  `marital_status` smallint(5) unsigned NOT NULL,
  `last_login_date` datetime DEFAULT NULL,
  `send_notifications` bit(1) DEFAULT NULL,
  `notifications_email` varchar(200) DEFAULT NULL,
  `age_min` tinyint(3) unsigned NOT NULL,
  `age_max` tinyint(3) unsigned NOT NULL,
  `search_prefs` json DEFAULT NULL,
  `num_no_shows` smallint(5) unsigned NOT NULL DEFAULT '0',
  `full_img_url` varchar(500) DEFAULT NULL,
  `thumb_img_url` varchar(500) DEFAULT NULL,
  `is_complete` bit(1) NOT NULL,
  `fb_id` varchar(100) DEFAULT NULL,
  `google_sub_id` varchar(100) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_email`
--

DROP TABLE IF EXISTS `user_email`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user_email` (
  `email` varchar(255) NOT NULL,
  `user_id` int(11) NOT NULL,
  PRIMARY KEY (`email`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_image`
--

DROP TABLE IF EXISTS `user_image`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user_image` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `full_img_url` varchar(500) DEFAULT NULL,
  `thumb_img_url` varchar(500) DEFAULT NULL,
  `file_name` varchar(500) DEFAULT NULL,
  `mime_type` varchar(45) DEFAULT NULL,
  `timestamp` varchar(45) DEFAULT NULL,
  `creation_date` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `user_token`
--

DROP TABLE IF EXISTS `user_token`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `user_token` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `token` text NOT NULL,
  `creation_date` datetime NOT NULL,
  `expiration` datetime DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `zip_code`
--

DROP TABLE IF EXISTS `zip_code`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
 SET character_set_client = utf8mb4 ;
CREATE TABLE `zip_code` (
  `zip` char(5) NOT NULL,
  `primary_city` varchar(45) DEFAULT NULL,
  `state` char(2) DEFAULT NULL,
  `timezone` varchar(45) DEFAULT NULL,
  `latitude` decimal(5,2) DEFAULT NULL,
  `longitude` decimal(5,2) DEFAULT NULL,
  PRIMARY KEY (`zip`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping events for database 'definiteplans'
--

--
-- Dumping routines for database 'definiteplans'
--
/*!50003 DROP PROCEDURE IF EXISTS `DELETE_ACCOUNT` */;
/*!50003 SET @saved_cs_client      = @@character_set_client */ ;
/*!50003 SET @saved_cs_results     = @@character_set_results */ ;
/*!50003 SET @saved_col_connection = @@collation_connection */ ;
/*!50003 SET character_set_client  = utf8mb4 */ ;
/*!50003 SET character_set_results = utf8mb4 */ ;
/*!50003 SET collation_connection  = utf8mb4_0900_ai_ci */ ;
/*!50003 SET @saved_sql_mode       = @@sql_mode */ ;
/*!50003 SET sql_mode              = 'STRICT_TRANS_TABLES,NO_ENGINE_SUBSTITUTION' */ ;
DELIMITER ;;
CREATE DEFINER=`root`@`localhost` PROCEDURE `DELETE_ACCOUNT`(IN user_to_delete INT)
BEGIN

    DELETE from blocked_user where user_id = user_to_delete;
	DELETE from chat_msg where from_id = user_to_delete or to_id = user_to_delete;
    DELETE from definite_date where owner_id = user_to_delete or datee_id = user_to_delete;
	DELETE from fb_user where user_id = user_to_delete;
	DELETE from google_user where user_id = user_to_delete;
	DELETE from user where id = user_to_delete;
	DELETE from user_email where user_id = user_to_delete;
    DELETE from user_image where user_id = user_to_delete;
    DELETE from user_token where user_id = user_to_delete;

END ;;
DELIMITER ;
/*!50003 SET sql_mode              = @saved_sql_mode */ ;
/*!50003 SET character_set_client  = @saved_cs_client */ ;
/*!50003 SET character_set_results = @saved_cs_results */ ;
/*!50003 SET collation_connection  = @saved_col_connection */ ;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-05-24 10:16:45

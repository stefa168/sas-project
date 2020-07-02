-- MySQL dump 10.13  Distrib 8.0.19, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: catering
-- ------------------------------------------------------
-- Server version	8.0.19

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `availabilities`
--

DROP TABLE IF EXISTS `availabilities`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `availabilities` (
  `turn_id` int NOT NULL,
  `user_id` int NOT NULL,
  PRIMARY KEY (`turn_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `availabilities`
--

LOCK TABLES `availabilities` WRITE;
/*!40000 ALTER TABLE `availabilities` DISABLE KEYS */;
INSERT INTO `availabilities` (`turn_id`, `user_id`) VALUES (1,7),(2,7),(3,4),(3,6),(4,4),(4,6),(7,4),(7,5),(8,4),(8,5),(9,5),(10,4),(11,7),(12,7),(13,6),(14,7),(15,4),(15,6),(16,4),(16,6),(19,5),(20,5);
/*!40000 ALTER TABLE `availabilities` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `event`
--

DROP TABLE IF EXISTS `event`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `event` (
  `event_id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(128) NOT NULL,
  `date_start` timestamp NULL DEFAULT NULL,
  `date_end` timestamp NULL DEFAULT NULL,
  `expected_participants` int DEFAULT NULL,
  `organizer_id` int NOT NULL,
  `state` int NOT NULL,
  `chef_id` int NOT NULL,
  PRIMARY KEY (`event_id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `event`
--

LOCK TABLES `event` WRITE;
/*!40000 ALTER TABLE `event` DISABLE KEYS */;
INSERT INTO `event` (`event_id`, `name`, `date_start`, `date_end`, `expected_participants`, `organizer_id`, `state`, `chef_id`) VALUES (1,'Convegno Agile Community','2020-09-24 22:00:00','2020-09-25 21:59:59',100,2,1,3),(2,'Compleanno di Sara','2020-08-12 22:00:00','2020-08-13 21:59:59',25,2,1,4),(3,'Fiera del Sedano Rapa','2020-10-01 22:00:00','2020-10-03 22:00:00',400,1,1,2);
/*!40000 ALTER TABLE `event` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `kitchenjob`
--

DROP TABLE IF EXISTS `kitchenjob`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `kitchenjob` (
  `kitchenJob_id` int NOT NULL AUTO_INCREMENT,
  `task_id` int NOT NULL,
  `turn_id` int NOT NULL,
  `cook_id` int DEFAULT NULL,
  `amount` int NOT NULL,
  `estimatedDuration` int NOT NULL,
  PRIMARY KEY (`kitchenJob_id`,`task_id`,`turn_id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `kitchenjob`
--

LOCK TABLES `kitchenjob` WRITE;
/*!40000 ALTER TABLE `kitchenjob` DISABLE KEYS */;
INSERT INTO `kitchenjob` (`kitchenJob_id`, `task_id`, `turn_id`, `cook_id`, `amount`, `estimatedDuration`) VALUES (5,31,7,5,10,180),(6,31,7,5,10,20),(18,75,7,NULL,20,140);
/*!40000 ALTER TABLE `kitchenjob` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menufeatures`
--

DROP TABLE IF EXISTS `menufeatures`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menufeatures` (
  `menu_id` int NOT NULL,
  `name` varchar(128) NOT NULL DEFAULT '',
  `value` tinyint(1) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menufeatures`
--

LOCK TABLES `menufeatures` WRITE;
/*!40000 ALTER TABLE `menufeatures` DISABLE KEYS */;
INSERT INTO `menufeatures` (`menu_id`, `name`, `value`) VALUES (80,'Richiede cuoco',0),(80,'Buffet',0),(80,'Richiede cucina',0),(80,'Finger food',0),(80,'Piatti caldi',0),(82,'Richiede cuoco',0),(82,'Buffet',0),(82,'Richiede cucina',0),(82,'Finger food',0),(82,'Piatti caldi',0),(86,'Richiede cuoco',0),(86,'Buffet',0),(86,'Richiede cucina',0),(86,'Finger food',0),(86,'Piatti caldi',0),(87,'Richiede cuoco',0),(87,'Buffet',0),(87,'Richiede cucina',0),(87,'Finger food',0),(87,'Piatti caldi',0);
/*!40000 ALTER TABLE `menufeatures` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menuitems`
--

DROP TABLE IF EXISTS `menuitems`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menuitems` (
  `id` int NOT NULL AUTO_INCREMENT,
  `menu_id` int NOT NULL,
  `section_id` int DEFAULT NULL,
  `description` tinytext,
  `recipe_id` int NOT NULL,
  `position` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=140 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menuitems`
--

LOCK TABLES `menuitems` WRITE;
/*!40000 ALTER TABLE `menuitems` DISABLE KEYS */;
INSERT INTO `menuitems` (`id`, `menu_id`, `section_id`, `description`, `recipe_id`, `position`) VALUES (96,80,0,'Croissant vuoti',9,0),(97,80,0,'Croissant alla marmellata',9,1),(98,80,0,'Pane al cioccolato mignon',10,2),(99,80,0,'Panini al latte con prosciutto crudo',12,4),(100,80,0,'Panini al latte con prosciutto cotto',12,5),(101,80,0,'Panini al latte con formaggio spalmabile alle erbe',12,6),(102,80,0,'Girelle all\'uvetta mignon',11,3),(103,82,0,'Biscotti',13,1),(104,82,0,'Lingue di gatto',14,2),(105,82,0,'Bigné alla crema',15,3),(106,82,0,'Bigné al caffè',15,4),(107,82,0,'Pizzette',16,5),(108,82,0,'Croissant al prosciutto crudo mignon',9,6),(109,82,0,'Tramezzini tonno e carciofini mignon',17,7),(112,86,41,'Vitello tonnato',1,0),(113,86,41,'Carpaccio di spada',2,1),(114,86,41,'Alici marinate',3,2),(115,86,42,'Penne alla messinese',5,0),(116,86,42,'Risotto alla zucca',20,1),(117,86,43,'Salmone al forno',8,0),(118,86,44,'Sorbetto al limone',18,0),(119,86,44,'Torta Saint Honoré',19,1),(120,87,46,'Vitello tonnato',1,0),(121,87,46,'Carpaccio di spada',2,1),(122,87,46,'Alici marinate',3,2),(123,87,47,'Penne alla messinese',5,0),(124,87,47,'Risotto alla zucca',20,1),(125,87,48,'Salmone al forno',8,0),(126,87,49,'Sorbetto al limone',18,0),(127,87,49,'Torta Saint Honoré',19,1);
/*!40000 ALTER TABLE `menuitems` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menus`
--

DROP TABLE IF EXISTS `menus`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menus` (
  `id` int NOT NULL AUTO_INCREMENT,
  `title` tinytext,
  `owner_id` int DEFAULT NULL,
  `published` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=89 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menus`
--

LOCK TABLES `menus` WRITE;
/*!40000 ALTER TABLE `menus` DISABLE KEYS */;
INSERT INTO `menus` (`id`, `title`, `owner_id`, `published`) VALUES (80,'Coffee break mattutino',2,1),(82,'Coffee break pomeridiano',2,1),(86,'Cena di compleanno pesce',3,1),(87,'Cena di compleanno pesce',2,0);
/*!40000 ALTER TABLE `menus` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `menusections`
--

DROP TABLE IF EXISTS `menusections`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `menusections` (
  `id` int NOT NULL AUTO_INCREMENT,
  `menu_id` int NOT NULL,
  `name` tinytext,
  `position` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=54 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `menusections`
--

LOCK TABLES `menusections` WRITE;
/*!40000 ALTER TABLE `menusections` DISABLE KEYS */;
INSERT INTO `menusections` (`id`, `menu_id`, `name`, `position`) VALUES (41,86,'Antipasti',0),(42,86,'Primi',1),(43,86,'Secondi',2),(44,86,'Dessert',3),(45,87,'Antipasti',0),(46,87,'Antipasti',0),(47,87,'Primi',1),(48,87,'Secondi',2),(49,87,'Dessert',3);
/*!40000 ALTER TABLE `menusections` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `patch`
--

DROP TABLE IF EXISTS `patch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `patch` (
  `patch_id` int NOT NULL AUTO_INCREMENT,
  `service_id` int NOT NULL,
  `menuItem_id` int DEFAULT NULL,
  `recipe_id` int NOT NULL,
  PRIMARY KEY (`patch_id`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `patch`
--

LOCK TABLES `patch` WRITE;
/*!40000 ALTER TABLE `patch` DISABLE KEYS */;
INSERT INTO `patch` (`patch_id`, `service_id`, `menuItem_id`, `recipe_id`) VALUES (2,1,119,19),(3,1,NULL,9),(4,4,NULL,15);
/*!40000 ALTER TABLE `patch` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `preparation`
--

DROP TABLE IF EXISTS `preparation`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `preparation` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` varchar(128) DEFAULT NULL,
  `tacc` int DEFAULT NULL,
  `tacv` int DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=401 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `preparation`
--

LOCK TABLES `preparation` WRITE;
/*!40000 ALTER TABLE `preparation` DISABLE KEYS */;
INSERT INTO `preparation` (`id`, `name`, `tacc`, `tacv`) VALUES (1,'Mayonnaise',10,5),(2,'Cipolla carammelata',17,3),(3,'bacon grigliato',12,2),(4,'Pane di Matera',4,4);
/*!40000 ALTER TABLE `preparation` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `recipes`
--

DROP TABLE IF EXISTS `recipes`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `recipes` (
  `id` int NOT NULL AUTO_INCREMENT,
  `name` tinytext,
  `tacc` int NOT NULL DEFAULT '0',
  `tacv` int NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=22 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `recipes`
--

LOCK TABLES `recipes` WRITE;
/*!40000 ALTER TABLE `recipes` DISABLE KEYS */;
INSERT INTO `recipes` (`id`, `name`, `tacc`, `tacv`) VALUES (1,'Vitello tonnato',15,2),(2,'Carpaccio di spada',15,10),(3,'Alici marinate',15,5),(4,'Insalata di riso',15,15),(5,'Penne al sugo di baccalà',15,5),(6,'Pappa al pomodoro',15,11),(7,'Hamburger con bacon e cipolla caramellata',15,5),(8,'Salmone al forno',15,13),(9,'Croissant',15,5),(10,'Pane al cioccolato',15,14),(11,'Girelle all\'uvetta',15,12),(12,'Panini al latte',15,7),(13,'Biscotti di pasta frolla',15,15),(14,'Lingue di gatto',15,10),(15,'Bigné farciti',15,25),(16,'Pizzette',15,10),(17,'Tramezzini',15,13),(18,'Sorbetto al limone',15,10),(19,'Torta Saint Honoré',15,9),(20,'Risotto alla zucca',15,2),(21,'Baccalà al forno',15,5);
/*!40000 ALTER TABLE `recipes` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles` (
  `id` char(1) NOT NULL,
  `role` varchar(128) NOT NULL DEFAULT 'servizio',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` (`id`, `role`) VALUES ('c','cuoco'),('h','chef'),('o','organizzatore'),('s','servizio');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `service`
--

DROP TABLE IF EXISTS `service`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `service` (
  `service_id` int NOT NULL AUTO_INCREMENT,
  `event_id` int NOT NULL,
  `name` varchar(128) NOT NULL,
  `menu_id` int NOT NULL,
  `offsetDay` int NOT NULL,
  `startHour` time NOT NULL,
  `endHour` time NOT NULL,
  `diners` int NOT NULL,
  `typology` varchar(128) NOT NULL,
  `place` varchar(128) NOT NULL,
  `state` int NOT NULL,
  PRIMARY KEY (`service_id`)
) ENGINE=InnoDB AUTO_INCREMENT=9 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `service`
--

LOCK TABLES `service` WRITE;
/*!40000 ALTER TABLE `service` DISABLE KEYS */;
INSERT INTO `service` (`service_id`, `event_id`, `name`, `menu_id`, `offsetDay`, `startHour`, `endHour`, `diners`, `typology`, `place`, `state`) VALUES (1,2,'cena',86,0,'20:00:00','23:30:00',25,'formale','Cracco Milano',1),(2,1,'coffe break mattino',80,0,'10:30:00','11:30:00',100,'finger food','Area Clienti',1),(3,1,'colazione di lavoro',0,0,'13:00:00','14:00:00',80,'informale','Dubai Torino',0),(4,1,'coffe break pomeriggio',82,0,'16:00:00','16:30:00',100,'finger food','Area Clienti',1),(5,1,'cena sociale',0,0,'00:00:20','22:30:00',40,'informale','Dubai',0),(6,3,'pranzo giorno 1',0,0,'12:00:00','15:00:00',200,'vegetariano','Cherasco',0),(7,3,'pranzo giorno 2',0,1,'12:00:00','15:00:00',300,'vegetariano','Cherasco',0),(8,3,'pranzo giorno 3',0,2,'12:00:00','15:00:00',400,'vegetariano','Cherasco',0);
/*!40000 ALTER TABLE `service` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `subduties`
--

DROP TABLE IF EXISTS `subduties`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `subduties` (
  `kitchenDuty_id` int NOT NULL,
  `subKitchenDuty_id` int NOT NULL,
  `subIsRecipe` tinyint(1) NOT NULL,
  PRIMARY KEY (`kitchenDuty_id`,`subKitchenDuty_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `subduties`
--

LOCK TABLES `subduties` WRITE;
/*!40000 ALTER TABLE `subduties` DISABLE KEYS */;
INSERT INTO `subduties` (`kitchenDuty_id`, `subKitchenDuty_id`, `subIsRecipe`) VALUES (1,1,0),(5,21,1),(7,2,0),(7,3,0),(10,4,0);
/*!40000 ALTER TABLE `subduties` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `task`
--

DROP TABLE IF EXISTS `task`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `task` (
  `id` int NOT NULL AUTO_INCREMENT,
  `kitchenDuty_id` int NOT NULL,
  `service_id` int NOT NULL,
  `amount` int NOT NULL DEFAULT '0',
  `estimatedDuration` int NOT NULL,
  `toDo` tinyint(1) NOT NULL,
  `optionalDuty` tinyint(1) NOT NULL,
  `order_number` int NOT NULL,
  `isRecipe` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=78 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `task`
--

LOCK TABLES `task` WRITE;
/*!40000 ALTER TABLE `task` DISABLE KEYS */;
INSERT INTO `task` (`id`, `kitchenDuty_id`, `service_id`, `amount`, `estimatedDuration`, `toDo`, `optionalDuty`, `order_number`, `isRecipe`) VALUES (31,9,2,100,60,1,0,0,1),(32,9,2,20,25,1,0,1,1),(33,10,2,10,10,1,0,2,1),(34,4,2,0,0,1,0,5,0),(35,11,2,0,0,1,0,6,1),(36,12,2,0,0,1,0,7,1),(37,12,2,0,0,1,0,8,1),(38,12,2,0,0,1,0,9,1),(66,13,4,0,0,1,0,0,1),(67,14,4,0,0,1,0,1,1),(68,15,4,0,0,1,0,2,1),(69,15,4,0,0,1,0,3,1),(70,16,4,0,0,1,0,4,1),(71,9,4,0,0,1,0,5,1),(72,17,4,0,0,1,0,6,1),(73,15,4,0,0,1,0,7,1),(75,7,4,20,120,1,1,8,1),(76,2,4,0,0,0,1,9,0),(77,3,4,0,0,1,1,10,0);
/*!40000 ALTER TABLE `task` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `timekeeper`
--

DROP TABLE IF EXISTS `timekeeper`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `timekeeper` (
  `turn_id` int NOT NULL,
  `user_id` int NOT NULL,
  `usedTime` int NOT NULL,
  PRIMARY KEY (`turn_id`,`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `timekeeper`
--

LOCK TABLES `timekeeper` WRITE;
/*!40000 ALTER TABLE `timekeeper` DISABLE KEYS */;
/*!40000 ALTER TABLE `timekeeper` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `turn`
--

DROP TABLE IF EXISTS `turn`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `turn` (
  `turn_id` int NOT NULL AUTO_INCREMENT,
  `startDate` timestamp NULL DEFAULT NULL,
  `endDate` timestamp NULL DEFAULT NULL,
  `complete` tinyint(1) NOT NULL,
  PRIMARY KEY (`turn_id`)
) ENGINE=InnoDB AUTO_INCREMENT=21 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `turn`
--

LOCK TABLES `turn` WRITE;
/*!40000 ALTER TABLE `turn` DISABLE KEYS */;
INSERT INTO `turn` (`turn_id`, `startDate`, `endDate`, `complete`) VALUES (1,'2020-09-24 20:00:00','2020-09-25 01:59:59',0),(2,'2020-08-12 20:00:00','2020-08-13 01:59:59',0),(3,'2020-10-01 20:00:00','2020-10-02 01:59:59',0),(4,'2020-10-02 20:00:00','2020-10-03 19:59:59',0),(5,'2020-10-03 20:00:00','2020-10-04 01:59:59',0),(6,'2020-09-25 02:00:00','2020-09-25 07:59:59',1),(7,'2020-09-25 08:00:00','2020-09-25 13:59:59',0),(8,'2020-09-25 14:00:00','2020-09-25 19:59:59',0),(9,'2020-08-13 02:00:00','2020-08-13 07:59:59',0),(10,'2020-08-13 08:00:00','2020-08-13 13:59:59',0),(11,'2020-08-13 14:00:00','2020-08-13 19:59:59',0),(12,'2020-10-02 02:00:00','2020-10-02 07:59:59',0),(13,'2020-10-02 08:00:00','2020-10-02 13:59:59',0),(14,'2020-10-02 14:00:00','2020-10-02 19:59:59',0),(15,'2020-10-03 02:00:00','2020-10-03 07:59:59',0),(16,'2020-10-03 08:00:00','2020-10-03 13:59:59',0),(17,'2020-10-03 14:00:00','2020-10-03 19:59:59',0),(18,'2020-10-04 02:00:00','2020-10-04 07:59:59',0),(19,'2020-10-04 08:00:00','2020-10-04 13:59:59',0),(20,'2020-10-04 14:00:00','2020-10-04 19:59:59',0);
/*!40000 ALTER TABLE `turn` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `userroles`
--

DROP TABLE IF EXISTS `userroles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `userroles` (
  `user_id` int NOT NULL,
  `role_id` char(1) NOT NULL DEFAULT 's'
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `userroles`
--

LOCK TABLES `userroles` WRITE;
/*!40000 ALTER TABLE `userroles` DISABLE KEYS */;
INSERT INTO `userroles` (`user_id`, `role_id`) VALUES (1,'o'),(2,'o'),(2,'h'),(3,'h'),(4,'h'),(4,'c'),(5,'c'),(6,'c'),(7,'c'),(8,'s'),(9,'s'),(10,'s'),(7,'s');
/*!40000 ALTER TABLE `userroles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users` (
  `id` int NOT NULL AUTO_INCREMENT,
  `username` varchar(128) NOT NULL DEFAULT '',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` (`id`, `username`) VALUES (1,'Carlin'),(2,'Lidia'),(3,'Tony'),(4,'Marinella'),(5,'Guido'),(6,'Antonietta'),(7,'Paola'),(8,'Silvia'),(9,'Marco'),(10,'Piergiorgio');
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2020-07-02 16:29:39

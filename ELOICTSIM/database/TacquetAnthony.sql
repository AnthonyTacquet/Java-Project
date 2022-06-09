-- MySQL dump 10.13  Distrib 8.0.29, for Win64 (x86_64)
--
-- Host: localhost    Database: eloictsim
-- ------------------------------------------------------
-- Server version	8.0.29

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `beroepsprofielen`
--

DROP TABLE IF EXISTS `beroepsprofielen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `beroepsprofielen` (
  `id` int NOT NULL AUTO_INCREMENT,
  `naam` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `beroepsprofielen`
--

LOCK TABLES `beroepsprofielen` WRITE;
/*!40000 ALTER TABLE `beroepsprofielen` DISABLE KEYS */;
INSERT INTO `beroepsprofielen` VALUES (1,'TELECOMMUNICATIONS_ENGINEER'),(2,'INTERNET_OF_THINGS_DEVELOPER'),(3,'NETWORK_SECURITY_ENGINEER'),(4,'SOFTWARE_AI_DEVELOPER'),(5,'WEB_MOBILE_DEVELOPER');
/*!40000 ALTER TABLE `beroepsprofielen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `deuren`
--

DROP TABLE IF EXISTS `deuren`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `deuren` (
  `id` int NOT NULL AUTO_INCREMENT,
  `x1` int NOT NULL,
  `y1` int NOT NULL,
  `x2` int NOT NULL,
  `y2` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=18 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `deuren`
--

LOCK TABLES `deuren` WRITE;
/*!40000 ALTER TABLE `deuren` DISABLE KEYS */;
INSERT INTO `deuren` VALUES (1,68,226,88,226),(2,343,8,343,70),(3,564,226,584,226),(4,648,226,668,226),(5,960,226,980,226),(6,1016,226,1062,226),(7,90,314,150,314),(8,98,438,118,438),(9,136,438,156,438),(10,79,410,79,430),(11,161,410,161,430),(12,469,314,489,314),(13,556,314,576,314),(14,871,314,891,314),(15,725,321,725,556),(16,951,314,997,314),(17,1004,327,1004,347);
/*!40000 ALTER TABLE `deuren` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `docenten`
--

DROP TABLE IF EXISTS `docenten`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `docenten` (
  `id` int NOT NULL,
  `rol` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_docenten_personen_idx` (`id`),
  CONSTRAINT `fk_docenten_personen` FOREIGN KEY (`id`) REFERENCES `personen` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `docenten`
--

LOCK TABLES `docenten` WRITE;
/*!40000 ALTER TABLE `docenten` DISABLE KEYS */;
INSERT INTO `docenten` VALUES (2,'Opleidingshoofd'),(3,'Trajectbegeleider'),(5,'Ankerpersoon Internationalisering'),(6,NULL),(8,NULL),(9,'Stage- en Bachelorproefcoordinator');
/*!40000 ALTER TABLE `docenten` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `docenten_has_vakken`
--

DROP TABLE IF EXISTS `docenten_has_vakken`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `docenten_has_vakken` (
  `docent_id` int NOT NULL,
  `vak_id` int NOT NULL,
  PRIMARY KEY (`docent_id`,`vak_id`),
  KEY `fk_docenten_has_vakken_vakken1_idx` (`vak_id`),
  KEY `fk_docenten_has_vakken_docenten1_idx` (`docent_id`),
  CONSTRAINT `fk_docenten_has_vakken_docenten1` FOREIGN KEY (`docent_id`) REFERENCES `docenten` (`id`),
  CONSTRAINT `fk_docenten_has_vakken_vakken1` FOREIGN KEY (`vak_id`) REFERENCES `vakken` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `docenten_has_vakken`
--

LOCK TABLES `docenten_has_vakken` WRITE;
/*!40000 ALTER TABLE `docenten_has_vakken` DISABLE KEYS */;
INSERT INTO `docenten_has_vakken` VALUES (5,1),(6,1),(5,3),(6,3),(8,3),(3,5),(5,5),(6,5),(5,6),(6,6),(8,6),(3,9),(6,9),(8,9),(9,9),(9,10),(8,11),(8,12),(9,13),(2,17),(9,17),(2,18),(3,18),(2,19),(3,19);
/*!40000 ALTER TABLE `docenten_has_vakken` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `informatiepunten`
--

DROP TABLE IF EXISTS `informatiepunten`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `informatiepunten` (
  `id` int NOT NULL AUTO_INCREMENT,
  `lokaal_id` int DEFAULT NULL,
  `persoon_id` int DEFAULT NULL,
  `x` int NOT NULL,
  `y` int NOT NULL,
  `beschrijving` text NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_informatiepanelen_lokalen1_idx` (`lokaal_id`),
  KEY `fk_informatiepanelen_personen1_idx` (`persoon_id`),
  CONSTRAINT `fk_informatiepanelen_lokalen1` FOREIGN KEY (`lokaal_id`) REFERENCES `lokalen` (`id`),
  CONSTRAINT `fk_informatiepanelen_personen1` FOREIGN KEY (`persoon_id`) REFERENCES `personen` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `informatiepunten`
--

LOCK TABLES `informatiepunten` WRITE;
/*!40000 ALTER TABLE `informatiepunten` DISABLE KEYS */;
INSERT INTO `informatiepunten` VALUES (1,14,NULL,40,280,'Gebruik je pijltjes om de speler door onze gang te navigeren. â­  â­¢ â­¡ â­£ \\nGa naar een informatiebord of Ã©Ã©n van de andere personen in het spel\\nom meer te weten te komen.'),(2,NULL,1,800,60,'Naast mijn studies ben ik ook bezig met mijn eigen bedrijf.'),(3,NULL,2,200,500,'Omdat we je niet willen opzadelen met examenstress op het einde van elk semester, werken we in de opleiding met permanente evaluatie. Evaluatieopdrachten voer je uit tijdens de lessen. We zien evaluaties dus niet als momentopnames, maar integreren ze in de modules.'),(4,NULL,3,50,500,'Wil je om een bepaalde reden een flexibel traject volgen, dan stelt de trajectbegeleider samen met jou een individueel uurrooster op waarin je de kans krijgt om alle vakken te volgen. Ongeveer de helft van onze studenten kiest hiervoor.'),(5,1,NULL,200,60,'In dit lokaal worden verschillende vakken gegeven die gebaseerd zijn op het cisco curriculum.'),(6,NULL,4,500,160,'Onze opleiding werkt met permanente evaluatie doorheen het jaar, in plaats van grote examens. Dat vind ik veel beter, omdat je sneller gerichte feedback krijgt. Bovendien word je zo gemotiveerd om elke dag te studeren waardoor je dingen beter onthoudt. En je hebt geen examenstress!'),(7,NULL,8,100,150,'Bij Odisee kun je het certificaat van certified ethical hacker behalen. Met dat certificaat op zak mag je zwakheden in systemen zoeken.'),(8,NULL,9,520,180,'We dagen je uit om het maximaleuit je opleiding te halen aan de hand van verschillende werkvormen zoals projecten, hoorcolleges, gastlessen, zelfstandig opzoekwerken labosessies. Geleidelijk aan leer je zelfstandig problemen oplossen op een kritische en professionele manier.'),(9,NULL,10,400,500,'Ik had geen eerdere ervaring met ICT. Daardoor vond ik onze inleidende vakken in het eerste jaar zo boeiend: ik vind het spannend om nieuwe dingen te leren. In het laatste jaar komt alle leerstof dan weer samen, zodat je je kennis kunt bundelen tot Ã©Ã©n geheel. Ook dat is supertof.'),(10,NULL,11,450,400,'Mijn specialisatie is zo veelzijdig en toch wat onbekend. Onterecht want het biedt zoveel kansen op de arbeidsmarkt. Met ons diploma kunnen we o.a. audio- of videoapparatuur ontwerpen en herstellen, FPGAâ€™s programmeren en ons verdiepen in het Internet of Things. Ik zou later graag onderzoek doen en overweeg een job in de ruimtevaartindustrie. Alles is nog mogelijk.'),(11,NULL,12,600,450,'Door de vele keuzemogelijkheden in het nieuwe curriculum kun je bijna alleen die vakken kiezen die je zelf leuk vindt. Web & Mobile Front-endis tot nu toe mijn favoriet opleidings-onderdeel. Daarin mag je zelf een webapplicatie maken. Je begint van nul en realiseert iets moois dat echt werkt.'),(12,4,NULL,1000,200,'In dit lokaal zitten de ICT-docenten. Ben je voor of na je les op zoek naar een docent. Kom dan eens kijken in dit lokaal. Kan je je docent niet vinden, maak dan eerst een afspraak.'),(13,12,NULL,850,400,'Dit lokaal wordt ook wel het groot labo of het dubbel labo genoemd. Hier zijn in totaal 36 plaatsen, voorzien van scherm, stopcontacten en netwerkaansluitingen. Bovendien zijn de meeste plaatsen ondertussen uitgerust met een docking station om je laptop eenvoudig aan te sluiten met USB-C. ');
/*!40000 ALTER TABLE `informatiepunten` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `keuzevakken`
--

DROP TABLE IF EXISTS `keuzevakken`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `keuzevakken` (
  `student_id` int NOT NULL,
  `vak_id` int NOT NULL,
  PRIMARY KEY (`student_id`,`vak_id`),
  KEY `fk_studenten_has_vakken_vakken1_idx` (`vak_id`),
  KEY `fk_studenten_has_vakken_studenten1_idx` (`student_id`),
  CONSTRAINT `fk_studenten_has_vakken_studenten1` FOREIGN KEY (`student_id`) REFERENCES `studenten` (`id`),
  CONSTRAINT `fk_studenten_has_vakken_vakken1` FOREIGN KEY (`vak_id`) REFERENCES `vakken` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `keuzevakken`
--

LOCK TABLES `keuzevakken` WRITE;
/*!40000 ALTER TABLE `keuzevakken` DISABLE KEYS */;
INSERT INTO `keuzevakken` VALUES (7,15),(7,16);
/*!40000 ALTER TABLE `keuzevakken` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `lokalen`
--

DROP TABLE IF EXISTS `lokalen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `lokalen` (
  `id` int NOT NULL AUTO_INCREMENT,
  `naam` varchar(45) NOT NULL,
  `lokaalcode` varchar(45) DEFAULT NULL,
  `x` int NOT NULL,
  `y` int NOT NULL,
  `breedte` int NOT NULL,
  `lengte` int NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=15 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `lokalen`
--

LOCK TABLES `lokalen` WRITE;
/*!40000 ALTER TABLE `lokalen` DISABLE KEYS */;
INSERT INTO `lokalen` VALUES (1,'lab1','D028',52,2,290,224),(2,'lab2','D029',344,2,290,224),(3,'lab3','D030',634,2,306,224),(4,'Docentenlokaal','D031',940,2,240,224),(5,'koffiehoek','D033',1004,226,173,88),(6,'UX-lab','D032',1004,314,186,197),(7,'Datacenter','D039',4,314,75,124),(8,'bureau K. Van Assche','D038',4,438,126,124),(9,'bureau P. Demeester','D037',130,438,126,124),(10,'Erasmuslokaal','D036',161,314,94,124),(11,'lab6','D035',255,314,250,248),(12,'lab5','D034b',505,314,220,248),(13,'lab4','D034a',725,314,220,248),(14,'gang',NULL,4,226,1000,88);
/*!40000 ALTER TABLE `lokalen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `personen`
--

DROP TABLE IF EXISTS `personen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `personen` (
  `id` int NOT NULL AUTO_INCREMENT,
  `familienaam` varchar(45) NOT NULL,
  `voornaam` varchar(45) NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `personen`
--

LOCK TABLES `personen` WRITE;
/*!40000 ALTER TABLE `personen` DISABLE KEYS */;
INSERT INTO `personen` VALUES (1,'Guillermin','Fabrice'),(2,'Demeester','Peter'),(3,'Van Assche','Kristien'),(4,'Abdul Khalil','Atal'),(5,'Jacobs','Evert-Jan'),(6,'Vermeulen','Tim'),(7,'Thys','Ruben'),(8,'Sanders','Sven'),(9,'Knockaert','Sven'),(10,'Verleysen','Jirka'),(11,'Lux','Lukas'),(12,'Poppe','Ruben');
/*!40000 ALTER TABLE `personen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `studenten`
--

DROP TABLE IF EXISTS `studenten`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `studenten` (
  `id` int NOT NULL,
  `beroepsprofiel_id` int NOT NULL,
  `inschrijvingsjaar` smallint NOT NULL,
  PRIMARY KEY (`id`),
  KEY `fk_studenten_personen1_idx` (`id`),
  KEY `fk_studenten_beroepsprofielen1_idx` (`beroepsprofiel_id`),
  CONSTRAINT `fk_studenten_beroepsprofielen1` FOREIGN KEY (`beroepsprofiel_id`) REFERENCES `beroepsprofielen` (`id`),
  CONSTRAINT `fk_studenten_personen1` FOREIGN KEY (`id`) REFERENCES `personen` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `studenten`
--

LOCK TABLES `studenten` WRITE;
/*!40000 ALTER TABLE `studenten` DISABLE KEYS */;
INSERT INTO `studenten` VALUES (1,5,2018),(4,5,2020),(7,3,2022),(10,1,2019),(11,2,2019),(12,5,2018);
/*!40000 ALTER TABLE `studenten` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vakken`
--

DROP TABLE IF EXISTS `vakken`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vakken` (
  `id` int NOT NULL AUTO_INCREMENT,
  `naam` varchar(45) NOT NULL,
  `aantal_studiepunten` int NOT NULL,
  `periode` set('SEM1','SEM2') NOT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vakken`
--

LOCK TABLES `vakken` WRITE;
/*!40000 ALTER TABLE `vakken` DISABLE KEYS */;
INSERT INTO `vakken` VALUES (1,'Programming fundamentals',6,'SEM1'),(2,'Web & mobile introduction',6,'SEM1'),(3,'Infrastructure fundamentals',6,'SEM1'),(4,'Elektronische netwerken',6,'SEM1'),(5,'Object orientation & database concepts',12,'SEM2'),(6,'Hackathon',6,'SEM2'),(7,'Problem solving for ICT',6,'SEM1'),(8,'Elektronische realisatietechnieken',6,'SEM1'),(9,'Stage',18,'SEM1,SEM2'),(10,'Server infrastructure: Windows',6,'SEM2'),(11,'Server infrastructure: Linux',6,'SEM1'),(12,'Data security',6,'SEM2'),(13,'Infrastructure as code',6,'SEM2'),(14,'Datacenter infrastructure',6,'SEM1'),(15,'Web & mobile front-end',12,'SEM2'),(16,'Web & mobile full-stack',12,'SEM2'),(17,'Agile Team Project',6,'SEM2'),(18,'Algo & Data',6,'SEM1'),(19,'C# OO Programming',6,'SEM1');
/*!40000 ALTER TABLE `vakken` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `vakken_has_lokalen`
--

DROP TABLE IF EXISTS `vakken_has_lokalen`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `vakken_has_lokalen` (
  `vak_id` int NOT NULL,
  `lokaal_id` int NOT NULL,
  PRIMARY KEY (`vak_id`,`lokaal_id`),
  KEY `fk_vakken_has_lokalen_lokalen1_idx` (`lokaal_id`),
  KEY `fk_vakken_has_lokalen_vakken1_idx` (`vak_id`),
  CONSTRAINT `fk_vakken_has_lokalen_lokalen1` FOREIGN KEY (`lokaal_id`) REFERENCES `lokalen` (`id`),
  CONSTRAINT `fk_vakken_has_lokalen_vakken1` FOREIGN KEY (`vak_id`) REFERENCES `vakken` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `vakken_has_lokalen`
--

LOCK TABLES `vakken_has_lokalen` WRITE;
/*!40000 ALTER TABLE `vakken_has_lokalen` DISABLE KEYS */;
INSERT INTO `vakken_has_lokalen` VALUES (3,1),(10,1),(11,1),(12,1),(13,1),(1,2),(2,2),(3,2),(5,2),(10,2),(11,2),(14,2),(17,2),(1,3),(3,3),(5,3),(11,3),(17,3),(1,11),(2,11),(5,11),(15,11),(16,11),(17,11),(19,11),(1,12),(2,12),(5,12),(6,12),(7,12),(15,12),(18,12),(19,12),(1,13),(2,13),(5,13),(6,13),(7,13),(15,13),(17,13),(18,13),(19,13);
/*!40000 ALTER TABLE `vakken_has_lokalen` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `verplichte_vakken`
--

DROP TABLE IF EXISTS `verplichte_vakken`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `verplichte_vakken` (
  `vak_id` int NOT NULL,
  `beroepsprofiel_id` int NOT NULL,
  PRIMARY KEY (`vak_id`,`beroepsprofiel_id`),
  KEY `fk_vakken_has_beroepsprofielen_beroepsprofielen1_idx` (`beroepsprofiel_id`),
  KEY `fk_vakken_has_beroepsprofielen_vakken1_idx` (`vak_id`),
  CONSTRAINT `fk_vakken_has_beroepsprofielen_beroepsprofielen1` FOREIGN KEY (`beroepsprofiel_id`) REFERENCES `beroepsprofielen` (`id`),
  CONSTRAINT `fk_vakken_has_beroepsprofielen_vakken1` FOREIGN KEY (`vak_id`) REFERENCES `vakken` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `verplichte_vakken`
--

LOCK TABLES `verplichte_vakken` WRITE;
/*!40000 ALTER TABLE `verplichte_vakken` DISABLE KEYS */;
INSERT INTO `verplichte_vakken` VALUES (1,1),(2,1),(3,1),(4,1),(5,1),(6,1),(8,1),(9,1),(1,2),(2,2),(3,2),(4,2),(5,2),(6,2),(8,2),(9,2),(1,3),(2,3),(3,3),(4,3),(5,3),(6,3),(7,3),(9,3),(10,3),(11,3),(12,3),(13,3),(14,3),(17,3),(1,4),(2,4),(3,4),(4,4),(5,4),(6,4),(7,4),(9,4),(17,4),(19,4),(1,5),(2,5),(3,5),(4,5),(5,5),(6,5),(7,5),(9,5),(15,5),(16,5),(17,5);
/*!40000 ALTER TABLE `verplichte_vakken` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `volgtijdelijkheden`
--

DROP TABLE IF EXISTS `volgtijdelijkheden`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `volgtijdelijkheden` (
  `vak_id` int NOT NULL,
  `vervolgvak_id` int NOT NULL,
  PRIMARY KEY (`vak_id`,`vervolgvak_id`),
  KEY `fk_vakken_has_vakken_vakken2_idx` (`vervolgvak_id`),
  KEY `fk_vakken_has_vakken_vakken1_idx` (`vak_id`),
  CONSTRAINT `fk_vakken_has_vakken_vakken1` FOREIGN KEY (`vak_id`) REFERENCES `vakken` (`id`),
  CONSTRAINT `fk_vakken_has_vakken_vakken2` FOREIGN KEY (`vervolgvak_id`) REFERENCES `vakken` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `volgtijdelijkheden`
--

LOCK TABLES `volgtijdelijkheden` WRITE;
/*!40000 ALTER TABLE `volgtijdelijkheden` DISABLE KEYS */;
INSERT INTO `volgtijdelijkheden` VALUES (1,5),(1,6),(4,6),(8,9),(16,9),(18,9),(3,10),(3,11),(3,12),(11,13),(10,14),(11,14),(2,15),(15,16),(18,17),(19,17),(5,18),(5,19);
/*!40000 ALTER TABLE `volgtijdelijkheden` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2022-06-09  9:22:42

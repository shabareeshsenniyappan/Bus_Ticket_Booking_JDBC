CREATE TABLE `adminusers` (
  `adminName` varchar(20) DEFAULT NULL,
  `password` varchar(20) DEFAULT NULL,
  `id` bigint NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`id`)
);
CREATE TABLE `ticketdetails` (
  `bus_id` bigint DEFAULT NULL,
  `noOfTicket` int DEFAULT NULL,
  `totalFare` int DEFAULT NULL,
  `ticket_id` int NOT NULL AUTO_INCREMENT,
  `agentcode` int NOT NULL,
  PRIMARY KEY (`ticket_id`)
);
CREATE TABLE `busdetails` (
  `bus_id` bigint NOT NULL AUTO_INCREMENT,
  `bus_name` varchar(20) NOT NULL,
  `from_location` varchar(20) NOT NULL,
  `to_location` varchar(20) NOT NULL,
  `totalseat` int NOT NULL,
  `price` int NOT NULL,
  PRIMARY KEY (`bus_id`)
);
CREATE TABLE `agentdetails` (
  `agentcode` int NOT NULL AUTO_INCREMENT,
  `agentname` varchar(50) NOT NULL,
  `mobileno` int DEFAULT NULL,
  `password` varchar(20) NOT NULL,
  PRIMARY KEY (`agentcode`)
);
-- phpMyAdmin SQL Dump
-- version 4.6.5.2
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: May 08, 2017 at 04:08 PM
-- Server version: 10.1.21-MariaDB
-- PHP Version: 5.6.30

drop schema bookstore;
create schema bookstore;
use bookstore;

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `bookstore`
--

-- --------------------------------------------------------

--
-- Table structure for table `book`
--

CREATE TABLE `book` (
  `ISBN` int(11) NOT NULL,
  `title` varchar(200) NOT NULL,
  `publisherName` varchar(40) NOT NULL,
  `year` date DEFAULT NULL,
  `price` double DEFAULT '20',
  `numberOfCopies` int(11) DEFAULT '0',
  `threshold` int(11) DEFAULT '0',
  `category` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Triggers `book`
--
DELIMITER $$
CREATE TRIGGER `Modify` BEFORE UPDATE ON `book` FOR EACH ROW BEGIN
	declare no_of_copies int;
    set no_of_copies = NEW.numberOfCopies;
    IF no_of_copies < 0 THEN
    	SIGNAL SQLSTATE '45000'
        SET MESSAGE_TEXT = 'Number of copies is going to be negative';
    END IF;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `category` BEFORE INSERT ON `book` FOR EACH ROW BEGIN

declare category_var varchar(40);

set category_var = NEW.category ;

if category_var != 'art' 
AND category_var != 'history' 
AND category_var != 'science' 
AND category_var != 'religion' 
AND category_var != 'geography' 
THEN
	SIGNAL SQLSTATE '45000'
	SET MESSAGE_TEXT = 'Invalid Category';
end if;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `placeOrder` AFTER UPDATE ON `book` FOR EACH ROW BEGIN
	DECLARE threshold int;
    DECLARE no_of_copies int;
    DECLARE max_id int;
	set threshold =  new.threshold;
   	set no_of_copies = NEW.numberOfCopies;
	if no_of_copies < threshold and  NEW.numberOfCopies < OLD.numberOfCopies THEN
        select max(orderid) into max_id from `order`;
        if max_id is null THEN
            set max_id = 0;
        end if;
        Insert into `order` (ISBN,orderId,quantity)
        values (new.ISBN,max_id+1,threshold-no_of_copies);
     end if;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `bookauthor`
--

CREATE TABLE `bookauthor` (
  `ISBN` int(11) NOT NULL,
  `authorName` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `order`
--

CREATE TABLE `order` (
  `orderId` int(11) NOT NULL,
  `ISBN` int(11) NOT NULL,
  `quantity` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Triggers `order`
--
DELIMITER $$
CREATE TRIGGER `confirmOrder` BEFORE DELETE ON `order` FOR EACH ROW BEGIN
	DECLARE orderId int;
    DECLARE bookId int;
    DECLARE quantity int;
	DECLARE no_of_copies int;
    set orderId = old.orderId;
    set bookId = old.ISBN;
    set quantity = old.quantity;
    select numberOfCopies into no_of_copies from book where isbn = bookId;
    UPDATE book set numberOfCopies= no_of_copies+ quantity where ISBN = bookId;

END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Table structure for table `publisher`
--

CREATE TABLE `publisher` (
  `name` varchar(40) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `publisheraddress`
--

CREATE TABLE `publisheraddress` (
  `name` varchar(40) NOT NULL,
  `address` varchar(100) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `publisherphone`
--

CREATE TABLE `publisherphone` (
  `name` varchar(40) NOT NULL,
  `phone` varchar(15) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `sales`
--

CREATE TABLE `sales` (
  `ISBN` int(11) NOT NULL,
  `userName` varchar(40) NOT NULL,
  `sellingDate` date NOT NULL,
  `sellingTime` time NOT NULL,
  `salesNumber` int(11) DEFAULT NULL,
  `price` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `name` varchar(40) NOT NULL,
  `password` varchar(40) NOT NULL,
  `Lname` varchar(40) NOT NULL,
  `Fname` varchar(40) NOT NULL,
  `Email` varchar(100) NOT NULL,
  `phoneNumber` varchar(15) DEFAULT NULL,
  `shippingAddress` varchar(100) DEFAULT NULL,
  `isManager` tinyint(1) DEFAULT '0'
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`name`, `password`, `Lname`, `Fname`, `Email`, `phoneNumber`, `shippingAddress`, `isManager`) VALUES
('a', 'b', 'a', 'a', 'a', 'a', 'a', 1);

--
-- Indexes for dumped tables
--

--
-- Indexes for table `book`
--
ALTER TABLE `book`
  ADD PRIMARY KEY (`ISBN`),
  ADD UNIQUE KEY `title` (`title`),
  ADD KEY `publisherfk` (`publisherName`);

--
-- Indexes for table `bookauthor`
--
ALTER TABLE `bookauthor`
  ADD PRIMARY KEY (`ISBN`,`authorName`);

--
-- Indexes for table `order`
--
ALTER TABLE `order`
  ADD PRIMARY KEY (`orderId`),
  ADD KEY `orderfk` (`ISBN`);

--
-- Indexes for table `publisher`
--
ALTER TABLE `publisher`
  ADD PRIMARY KEY (`name`);

--
-- Indexes for table `publisheraddress`
--
ALTER TABLE `publisheraddress`
  ADD PRIMARY KEY (`name`,`address`);

--
-- Indexes for table `publisherphone`
--
ALTER TABLE `publisherphone`
  ADD PRIMARY KEY (`name`,`phone`);

--
-- Indexes for table `sales`
--
ALTER TABLE `sales`
  ADD PRIMARY KEY (`ISBN`,`userName`,`sellingDate`,`sellingTime`),
  ADD KEY `salesfk2` (`userName`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`name`),
  ADD UNIQUE KEY `Email` (`Email`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `book`
--
ALTER TABLE `book`
  ADD CONSTRAINT `publisherfk` FOREIGN KEY (`publisherName`) REFERENCES `publisher` (`name`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `bookauthor`
--
ALTER TABLE `bookauthor`
  ADD CONSTRAINT `authorfk` FOREIGN KEY (`ISBN`) REFERENCES `book` (`ISBN`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `order`
--
ALTER TABLE `order`
  ADD CONSTRAINT `orderfk` FOREIGN KEY (`ISBN`) REFERENCES `book` (`ISBN`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `publisheraddress`
--
ALTER TABLE `publisheraddress`
  ADD CONSTRAINT `namefk` FOREIGN KEY (`name`) REFERENCES `publisher` (`name`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `publisherphone`
--
ALTER TABLE `publisherphone`
  ADD CONSTRAINT `phonefk` FOREIGN KEY (`name`) REFERENCES `publisher` (`name`) ON DELETE CASCADE ON UPDATE CASCADE;

--
-- Constraints for table `sales`
--
ALTER TABLE `sales`
  ADD CONSTRAINT `salesfk1` FOREIGN KEY (`ISBN`) REFERENCES `book` (`ISBN`) ON DELETE CASCADE ON UPDATE CASCADE,
  ADD CONSTRAINT `salesfk2` FOREIGN KEY (`userName`) REFERENCES `user` (`name`) ON DELETE CASCADE ON UPDATE CASCADE;

insert into user (name , password , Lname , Fname , Email , phoneNumber , shippingAddress , isManager)
VALUES('mm' , 'mm' , 'mm' , 'mm' , 'mm' , '012' , 'mm' , true);

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

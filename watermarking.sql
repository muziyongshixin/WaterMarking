-- phpMyAdmin SQL Dump
-- version 4.0.10deb1
-- http://www.phpmyadmin.net
--
-- 主机: localhost
-- 生成日期: 2017-09-16 11:27:47
-- 服务器版本: 5.5.54-0ubuntu0.14.04.1
-- PHP 版本: 5.5.9-1ubuntu4.21

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;

--
-- 数据库: `watermarking`
--

-- --------------------------------------------------------

--
-- 表的结构 `admin`
--

CREATE TABLE IF NOT EXISTS `admin` (
  `admin_id` varchar(30) NOT NULL,
  `admin_name` varchar(10) NOT NULL,
  `admin_password` varchar(15) NOT NULL,
  `admin_identity` varchar(30) NOT NULL,
  PRIMARY KEY (`admin_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `Asset`
--

CREATE TABLE IF NOT EXISTS `Asset` (
  `user_phone` varchar(30) NOT NULL,
  `asset_no` varchar(30) NOT NULL,
  `asset_name` varchar(30) NOT NULL,
  `asset_money` varchar(30) NOT NULL,
  PRIMARY KEY (`asset_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `photo`
--

CREATE TABLE IF NOT EXISTS `photo` (
  `photo_no` varchar(30) NOT NULL,
  `user_phone` varchar(30) NOT NULL,
  `time` datetime NOT NULL,
  `photo_url` text NOT NULL,
  `info_in_picture` text NOT NULL,
  PRIMARY KEY (`photo_no`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

--
-- 转存表中的数据 `photo`
--

INSERT INTO `photo` (`photo_no`, `user_phone`, `time`, `photo_url`, `info_in_picture`) VALUES
('2017031321101315271581490', '15271581490', '2017-03-13 21:10:13', 'image/20170313/2017031321101315271581490.jpg', '这是一条写死的测试信息==============xxxxxxxxxxxxxxxx'),
('2017031322573515271581490', '15271581490', '2017-03-13 22:57:35', 'image/20170313/2017031322573515271581490.jpg', '这是一条写死的测试信息==============xxxxxxxxxxxxxxxx'),
('2017031323354415271581490', '15271581490', '2017-03-13 23:35:44', 'image/20170313/2017031323354415271581490.jpg', '这是一条写死的测试信息==============xxxxxxxxxxxxxxxx'),
('2017031323355115271581490', '15271581490', '2017-03-13 23:35:51', 'image/20170313/2017031323355115271581490.jpg', '这是一条写死的测试信息==============xxxxxxxxxxxxxxxx'),
('2017031323360015271581490', '15271581490', '2017-03-13 23:36:00', 'image/20170313/2017031323360015271581490.jpg', '这是一条写死的测试信息==============xxxxxxxxxxxxxxxx'),
('2017031323361715271581490', '15271581490', '2017-03-13 23:36:17', 'image/20170313/2017031323361715271581490.jpg', '这是一条写死的测试信息==============xxxxxxxxxxxxxxxx'),
('2017031323415715271581490', '15271581490', '2017-03-13 23:41:57', 'image/20170313/2017031323415715271581490.jpg', '这是一条写死的测试信息==============xxxxxxxxxxxxxxxx'),
('2017031323420215271581490', '15271581490', '2017-03-13 23:42:02', 'image/20170313/2017031323420215271581490.jpg', '这是一条写死的测试信息==============xxxxxxxxxxxxxxxx'),
('2017031323421015271581490', '15271581490', '2017-03-13 23:42:10', 'image/20170313/2017031323421015271581490.jpg', '这是一条写死的测试信息==============xxxxxxxxxxxxxxxx'),
('2017031323421715271581490', '15271581490', '2017-03-13 23:42:17', 'image/20170313/2017031323421715271581490.jpg', '这是一条写死的测试信息==============xxxxxxxxxxxxxxxx'),
('2017031323422115271581490', '15271581490', '2017-03-13 23:42:21', 'image/20170313/2017031323422115271581490.jpg', '这是一条写死的测试信息==============xxxxxxxxxxxxxxxx'),
('2017031323422615271581490', '15271581490', '2017-03-13 23:42:26', 'image/20170313/2017031323422615271581490.jpg', '这是一条写死的测试信息==============xxxxxxxxxxxxxxxx'),
('2017031400063415271581490', '15271581490', '2017-03-14 00:06:34', 'image/20170314/2017031400063415271581490.jpg', '这是一条写死的测试信息==============xxxxxxxxxxxxxxxx'),
('2017031400085015271581490', '15271581490', '2017-03-14 00:08:50', 'image/20170314/2017031400085015271581490.jpg', '这是一条写死的测试信息==============xxxxxxxxxxxxxxxx');

-- --------------------------------------------------------

--
-- 表的结构 `test`
--

CREATE TABLE IF NOT EXISTS `test` (
  `id` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8;

-- --------------------------------------------------------

--
-- 表的结构 `user`
--

CREATE TABLE IF NOT EXISTS `user` (
  `user_phone` varchar(15) NOT NULL,
  `user_name` varchar(15) NOT NULL,
  `user_password` varchar(30) NOT NULL,
  `user_sex` varchar(3) NOT NULL,
  PRIMARY KEY (`user_phone`),
  UNIQUE KEY `phone` (`user_phone`),
  UNIQUE KEY `phone_2` (`user_phone`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='管理员信息表';

--
-- 转存表中的数据 `user`
--

INSERT INTO `user` (`user_phone`, `user_name`, `user_password`, `user_sex`) VALUES
('15271581490', 'liyongzhi', 'password', '男');

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;

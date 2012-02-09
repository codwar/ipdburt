SET SQL_MODE="NO_AUTO_VALUE_ON_ZERO";

--
-- Database: `iddb`
--

-- --------------------------------------------------------

--
-- Table structure for table `alias`
--

DROP TABLE IF EXISTS `alias`;
CREATE TABLE IF NOT EXISTS `alias` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `playerid` int(11) unsigned NOT NULL,
  `count` int(11) unsigned NOT NULL DEFAULT '0',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `nickname` varchar(75) COLLATE utf8_unicode_ci NOT NULL,
  `nameindex` varchar(225) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  KEY `nickname` (`nickname`),
  KEY `playerid_nick` (`playerid`,`nickname`),
  KEY `playeridupnick` (`playerid`,`updated`,`nickname`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `aliasip`
--

DROP TABLE IF EXISTS `aliasip`;
CREATE TABLE IF NOT EXISTS `aliasip` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `playerid` int(11) unsigned NOT NULL,
  `ip` int(11) unsigned NOT NULL,
  `count` int(11) unsigned NOT NULL DEFAULT '0',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  KEY `playerid_upd` (`playerid`,`updated`),
  KEY `ip_playerid_ipd` (`ip`,`playerid`,`updated`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `penalty`
--

DROP TABLE IF EXISTS `penalty`;
CREATE TABLE IF NOT EXISTS `penalty` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `playerid` int(11) unsigned NOT NULL,
  `adminid` int(11) unsigned DEFAULT NULL,
  `type` tinyint(4) unsigned NOT NULL,
  `reason` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  `duration` int(11) unsigned NOT NULL DEFAULT '0',
  `synced` tinyint(1) NOT NULL,
  `active` tinyint(1) NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `expires` timestamp NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `expires` (`type`,`duration`,`expires`,`active`),
  KEY `type_ct` (`type`,`created`),
  KEY `playerid_type_crt` (`playerid`,`type`,`created`),
  KEY `playerid_type_st_upd` (`playerid`,`type`,`active`,`updated`),
  KEY `synced` (`synced`),
  KEY `id_playerid` (`id`,`playerid`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `penalty_history`
--

DROP TABLE IF EXISTS `penalty_history`;
CREATE TABLE IF NOT EXISTS `penalty_history` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `penaltyid` int(11) unsigned NOT NULL,
  `funcid` tinyint(3) unsigned NOT NULL,
  `adminid` int(11) unsigned DEFAULT NULL,
  `status` tinyint(4) unsigned NOT NULL DEFAULT '0',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `error` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `penaltyid` (`penaltyid`),
  KEY `id_pen_upd` (`id`,`penaltyid`,`updated`),
  KEY `status` (`status`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `player`
--

DROP TABLE IF EXISTS `player`;
CREATE TABLE IF NOT EXISTS `player` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `guid` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `serverid` int(11) unsigned NOT NULL,
  `nickname` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `ip` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `clientid` int(11) unsigned DEFAULT NULL,
  `connected` tinyint(1) NOT NULL DEFAULT '0',
  `level` tinyint(4) unsigned DEFAULT NULL,
  `baninfo` timestamp NULL DEFAULT NULL,
  `updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `note` timestamp NULL DEFAULT NULL,
  `gaekey` varchar(75) COLLATE utf8_unicode_ci DEFAULT NULL,
  `rguid` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `guid` (`guid`,`serverid`),
  KEY `connected` (`connected`,`updated`),
  KEY `baninfo` (`baninfo`),
  KEY `gaekey` (`gaekey`),
  KEY `serverid_upd` (`serverid`,`updated`),
  KEY `clientid_upd` (`clientid`,`updated`),
  KEY `serverid_con_upd` (`serverid`,`connected`,`updated`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `server`
--

DROP TABLE IF EXISTS `server`;
CREATE TABLE IF NOT EXISTS `server` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `uid` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `name` varchar(100) COLLATE utf8_unicode_ci NOT NULL,
  `admin` varchar(50) COLLATE utf8_unicode_ci NOT NULL,
  `address` varchar(15) COLLATE utf8_unicode_ci DEFAULT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated` timestamp NULL DEFAULT NULL,
  `onlineplayers` tinyint(4) unsigned NOT NULL DEFAULT '0',
  `pluginversion` varchar(10) COLLATE utf8_unicode_ci DEFAULT NULL,
  `maxlevel` tinyint(4) unsigned NOT NULL DEFAULT '0',
  `isdirty` tinyint(1) NOT NULL DEFAULT '0',
  `disabled` tinyint(1) NOT NULL DEFAULT '0',
  `permission` tinyint(4) unsigned NOT NULL DEFAULT '0',
  `gaekey` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  `totalplayers` int(11) unsigned NOT NULL DEFAULT '0',
  `maxban` int(11) unsigned NOT NULL DEFAULT '0',
  `display_address` varchar(25) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uid` (`uid`),
  KEY `gaekey` (`gaekey`),
  KEY `updated` (`updated`),
  KEY `name` (`name`),
  KEY `active_srvs` (`disabled`,`updated`,`name`),
  KEY `disabled` (`disabled`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `server_ban_perm`
--

DROP TABLE IF EXISTS `server_ban_perm`;
CREATE TABLE IF NOT EXISTS `server_ban_perm` (
  `serverid` int(11) unsigned NOT NULL,
  `level` tinyint(4) unsigned NOT NULL,
  `value` int(11) unsigned NOT NULL,
  KEY `serverid` (`serverid`,`level`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `server_permission`
--

DROP TABLE IF EXISTS `server_permission`;
CREATE TABLE IF NOT EXISTS `server_permission` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `serverid` int(11) unsigned NOT NULL,
  `funcid` tinyint(4) unsigned NOT NULL,
  `level` tinyint(4) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `serverid_func` (`serverid`,`funcid`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
CREATE TABLE IF NOT EXISTS `user` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `loginid` varchar(75) COLLATE utf8_unicode_ci NOT NULL,
  `roles` varchar(200) COLLATE utf8_unicode_ci NOT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `password` varchar(128) COLLATE utf8_unicode_ci NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `loginid` (`loginid`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `userserver`
--

DROP TABLE IF EXISTS `userserver`;
CREATE TABLE IF NOT EXISTS `userserver` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `userid` int(11) unsigned NOT NULL,
  `serverid` int(11) unsigned NOT NULL,
  `playerid` int(11) DEFAULT NULL,
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  KEY `serverid` (`serverid`),
  KEY `userid_server` (`userid`,`serverid`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user_session`
--

DROP TABLE IF EXISTS `user_session`;
CREATE TABLE IF NOT EXISTS `user_session` (
  `id` varchar(40) COLLATE utf8_unicode_ci NOT NULL,
  `userid` int(11) unsigned NOT NULL,
  `ip` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `created` (`created`),
  KEY `key` (`id`,`userid`,`ip`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

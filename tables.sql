--
-- Table structure for table `alias`
--

CREATE TABLE IF NOT EXISTS `alias` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `playerid` int(11) unsigned NOT NULL,
  `count` int(11) unsigned NOT NULL DEFAULT '0',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `nickname` varchar(75) COLLATE utf8_unicode_ci NOT NULL,
  `normalized` varchar(75) COLLATE utf8_unicode_ci NOT NULL,
  `textindex` text COLLATE utf8_unicode_ci,
  PRIMARY KEY (`id`),
  KEY `playerid` (`playerid`,`nickname`),
  KEY `playerid_2` (`playerid`,`updated`,`nickname`),
  KEY `playerid_3` (`playerid`),
  KEY `playerid_4` (`playerid`,`updated`),
  FULLTEXT KEY `alias_search` (`nickname`,`normalized`,`textindex`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `aliasip`
--

CREATE TABLE IF NOT EXISTS `aliasip` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `playerid` int(11) unsigned NOT NULL,
  `ip` int(11) unsigned NOT NULL,
  `count` int(11) unsigned NOT NULL DEFAULT '0',
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `updated` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  KEY `playerid` (`playerid`,`ip`),
  KEY `playerid_2` (`playerid`),
  KEY `playerid_3` (`playerid`,`updated`),
  KEY `playerid_4` (`ip`,`playerid`,`updated`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `penalty`
--

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
  KEY `playerid` (`playerid`),
  KEY `type` (`type`),
  KEY `type_2` (`type`,`created`),
  KEY `playerid_2` (`playerid`,`type`,`active`),
  KEY `playerid_3` (`playerid`,`type`),
  KEY `playerid_4` (`playerid`,`type`,`created`),
  KEY `playerid_5` (`playerid`,`type`,`active`,`updated`),
  KEY `expires` (`type`,`duration`,`expires`,`active`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `player`
--

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
  PRIMARY KEY (`id`),
  KEY `guid` (`guid`,`serverid`),
  KEY `serverid` (`serverid`),
  KEY `serverid_2` (`serverid`,`updated`),
  KEY `connected` (`connected`,`updated`),
  KEY `baninfo` (`baninfo`),
  KEY `serverid_3` (`serverid`,`connected`),
  KEY `clientid` (`clientid`),
  KEY `clientid_2` (`clientid`,`updated`),
  KEY `gaekey` (`gaekey`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `server`
--

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
  `adminlevel` tinyint(4) unsigned NOT NULL DEFAULT '0',
  `maxlevel` tinyint(4) unsigned NOT NULL DEFAULT '0',
  `isdirty` tinyint(1) NOT NULL DEFAULT '0',
  `disabled` tinyint(1) NOT NULL DEFAULT '0',
  `permission` tinyint(4) unsigned NOT NULL DEFAULT '0',
  `gaekey` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `uid` (`uid`),
  KEY `gaekey` (`gaekey`),
  KEY `updated` (`updated`),
  KEY `disabled` (`updated`,`disabled`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

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

CREATE TABLE IF NOT EXISTS `userserver` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `userid` int(11) unsigned NOT NULL,
  `serverid` int(11) unsigned NOT NULL,
  `playerid` int(11) DEFAULT NULL,
  `owner` tinyint(1) NOT NULL DEFAULT '0',
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  `created` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  PRIMARY KEY (`id`),
  KEY `userid` (`userid`),
  KEY `serverid` (`serverid`),
  KEY `userid_2` (`userid`,`serverid`)
) ENGINE=MyISAM  DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;


--
-- Table structure for table `user_session`
--

CREATE TABLE IF NOT EXISTS `user_session` (
  `id` varchar(40) COLLATE utf8_unicode_ci NOT NULL,
  `userid` int(11) unsigned NOT NULL,
  `ip` varchar(15) COLLATE utf8_unicode_ci NOT NULL,
  `created` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `created` (`created`),
  KEY `key` (`id`,`userid`,`ip`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;
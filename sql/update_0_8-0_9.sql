SET FOREIGN_KEY_CHECKS = 0;

#
# DDL START
#
CREATE TABLE IF NOT EXISTS penalty_history (
    id int(11) unsigned NOT NULL DEFAULT 0 COMMENT '' auto_increment,
    penaltyid int(11) unsigned NOT NULL DEFAULT 0 COMMENT '',
    adminid int(11) unsigned NULL DEFAULT NULL COMMENT '',
    status tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '',
    created timestamp NOT NULL DEFAULT 'CURRENT_TIMESTAMP' COMMENT '',
    updated timestamp NOT NULL DEFAULT '0000-00-00 00:00:00' COMMENT '',
    error varchar(255) NULL DEFAULT NULL COMMENT '' COLLATE utf8_unicode_ci,
    PRIMARY KEY (id),
    INDEX id (id, penaltyid),
    INDEX penaltyid (penaltyid),
    INDEX id_pen_upd (id, penaltyid, updated)
) DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS user_session (
    id varchar(40) NOT NULL DEFAULT '' COMMENT '' COLLATE utf8_unicode_ci,
    userid int(11) unsigned NOT NULL DEFAULT 0 COMMENT '',
    ip varchar(15) NOT NULL DEFAULT '' COMMENT '' COLLATE utf8_unicode_ci,
    created timestamp NOT NULL DEFAULT 'CURRENT_TIMESTAMP' COMMENT '',
    PRIMARY KEY (id),
    INDEX created (created),
    INDEX `key` (id, userid, ip)
) DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE penalty
    ADD INDEX synced (synced),
    ADD INDEX id_playerid (id, playerid);

ALTER TABLE player
    ADD rguid varchar(50) NULL DEFAULT NULL COMMENT '' COLLATE utf8_unicode_ci AFTER gaekey;

ALTER TABLE server
    ADD INDEX name (name);

CREATE TABLE IF NOT EXISTS `server_permission` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `serverid` int(11) unsigned NOT NULL,
  `funcid` tinyint(4) unsigned NOT NULL,
  `level` tinyint(4) unsigned NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `serverid_func` (`serverid`,`funcid`),
  KEY `serverid` (`serverid`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci

ALTER TABLE `server` ADD `totalplayers` INT( 11 ) UNSIGNED NOT NULL DEFAULT '0';
ALTER TABLE `server` ADD `maxban` INT( 11 ) UNSIGNED NOT NULL DEFAULT '0';

#
# DDL END
#

SET FOREIGN_KEY_CHECKS = 1;
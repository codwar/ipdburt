SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE user_session (
    id varchar(40) NOT NULL DEFAULT '' COMMENT '' COLLATE utf8_unicode_ci,
    userid int(11) unsigned NOT NULL DEFAULT 0 COMMENT '',
    ip varchar(15) NOT NULL DEFAULT '' COMMENT '' COLLATE utf8_unicode_ci,
    created timestamp NOT NULL DEFAULT 'CURRENT_TIMESTAMP' COMMENT '',
    PRIMARY KEY (id),
    INDEX created (created),
    INDEX sessionkey (id, userid, ip)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

CREATE TABLE IF NOT EXISTS `penalty_history` (
  `id` int(11) unsigned NOT NULL AUTO_INCREMENT,
  `penaltyid` int(11) unsigned NOT NULL,
  `adminid` int(11) unsigned DEFAULT NULL,
  `status` tinyint(4) unsigned NOT NULL DEFAULT '0',
  `created` timestamp NOT NULL DEFAULT '0000-00-00 00:00:00',
  `updated` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP,
  `error` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `id` (`id`,`penaltyid`),
  KEY `penaltyid` (`penaltyid`),
  KEY `created` (`created`)
) ENGINE=MyISAM DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE penalty ADD expires timestamp NULL DEFAULT NULL;
ALTER TABLE penalty ADD INDEX (synced);
ALTER TABLE penalty ADD INDEX expires (`type`,`duration`,`expires`,`active`);

ALTER TABLE player ADD rguid varchar(50) NULL DEFAULT NULL COMMENT '' COLLATE utf8_unicode_ci AFTER guid;

ALTER TABLE server ADD adminlevel tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '' AFTER pluginversion,

ALTER TABLE server ADD INDEX (name);

SET FOREIGN_KEY_CHECKS = 1;


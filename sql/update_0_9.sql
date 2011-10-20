SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE user_session (
    id varchar(40) NOT NULL DEFAULT '' COMMENT '' COLLATE utf8_unicode_ci,
    userid int(11) unsigned NOT NULL DEFAULT 0 COMMENT '',
    ip varchar(15) NOT NULL DEFAULT '' COMMENT '' COLLATE utf8_unicode_ci,
    created timestamp NOT NULL DEFAULT 'CURRENT_TIMESTAMP' COMMENT '',
    PRIMARY KEY (id),
    INDEX created (created),
    INDEX sessionkey (id, userid, ip)
) DEFAULT CHARSET=utf8 COLLATE=utf8_unicode_ci;

ALTER TABLE player
    ADD rguid varchar(50) NULL DEFAULT NULL COMMENT '' COLLATE utf8_unicode_ci AFTER guid;

ALTER TABLE server 
  ADD adminlevel tinyint(4) unsigned NOT NULL DEFAULT '0' COMMENT '' AFTER pluginversion,
  ALTER TABLE server ADD INDEX (name);

SET FOREIGN_KEY_CHECKS = 1;


alter table alias drop column textindex;
alter table alias drop column normalized;
alter table alias add index (nickname);
alter table alias add column nameindex varchar(225) COLLATE utf8_unicode_ci NOT NULL;
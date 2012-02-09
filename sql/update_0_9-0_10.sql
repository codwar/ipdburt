alter table alias drop column textindex,
                    drop column normalized,
                    add column nameindex varchar(225) COLLATE utf8_unicode_ci NOT NULL,
                    add index (nickname);

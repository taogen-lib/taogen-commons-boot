DROP TABLE IF EXISTS `user`;
CREATE TABLE `user`
(
    `id`        int          NOT NULL AUTO_INCREMENT,
    `name`      varchar(100) NOT NULL,
    `age`       int NULL,
    `dept_id`   int NULL,
    `hobby_ids` varchar(100) NULL,
    `area_id`   int NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `users_table_un` (`name`)
);

DROP TABLE IF EXISTS `department`;
CREATE TABLE `department`
(
    `id`          int          NOT NULL AUTO_INCREMENT,
    `name`        varchar(100) NOT NULL,
    `description` varchar(100) NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `users_table_un` (`name`)
);

DROP TABLE IF EXISTS `hobby`;
CREATE TABLE `hobby`
(
    `id`   int          NOT NULL AUTO_INCREMENT,
    `name` varchar(100) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `users_table_un` (`name`)
);

DROP TABLE IF EXISTS `area`;
CREATE TABLE `area`
(
    `id`        int          NOT NULL AUTO_INCREMENT,
    `name`      varchar(100) NOT NULL,
    `code`      varchar(100) NOT NULL,
    `parent_id` int          NOT NULL DEFAULT 0,
    PRIMARY KEY (`id`),
    UNIQUE KEY `users_table_un` (`name`)
);

DROP TABLE IF EXISTS `role`;
CREATE TABLE `role`
(
    `id`   int          NOT NULL AUTO_INCREMENT,
    `name` varchar(100) NOT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `users_table_un` (`name`)
);
DROP TABLE IF EXISTS `user_role`;
CREATE TABLE `user_role`
(
    `id`      int not null AUTO_INCREMENT,
    `user_id` int NOT NULL,
    `role_id` int NOT NULL,
    PRIMARY KEY (`id`)
);

CREATE TABLE `user_account` (
  `id`          varchar(255) NOT NULL ,
  `status`      enum('active', 'suspended', 'deleted') NOT NULL default 'active',
  `email`       varchar(255) NOT NULL,
  `password`    varchar(255) NOT NULL,
  `first_name`  varchar(255) NOT NULL,
  `last_name`   varchar(255) NOT NULL,
  `created_at`  datetime(6) NOT NULL,
  `updated_at`  datetime(6),
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;


CREATE TABLE `user_message` (
  `user_id`     varchar(255) NOT NULL,
  `message_id`  bigint NOT NULL,
  `status`      enum('active', 'suspended', 'deleted') NOT NULL default 'active',
  `message`   varchar(255) NOT NULL,
  `created_at`  datetime(6) NOT NULL,
  `updated_at`  datetime(6),
  PRIMARY KEY (`user_id`, `message_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;



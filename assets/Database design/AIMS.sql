CREATE DATABASE aims;

CREATE TABLE `media_product` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `title` varchar(255) NOT NULL,
  `category` ENUM ('Book', 'CD', 'LP_record', 'DVD') NOT NULL,
  `value` decimal(10,2) NOT NULL,
  `current_price` decimal(10,2) NOT NULL
);

CREATE TABLE `Book` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `product_id` integer NOT NULL,
  `cover_type` ENUM ('paperback', 'hardcover') NOT NULL,
  `author` varchar(255) NOT NULL,
  `publisher` varchar(255) NOT NULL,
  `publication_date` datetime NOT NULL,
  `num_page` integer,
  `language` varchar(255),
  `genre` varchar(255)
);

CREATE TABLE `Music_product` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `product_id` integer NOT NULL,
  `artist` varchar(255) NOT NULL,
  `record_label` varchar(255) NOT NULL,
  `genre` varchar(255) NOT NULL,
  `release_date` datetime
);

CREATE TABLE `DVD` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `product_id` integer NOT NULL,
  `disc_type` ENUM ('Blu_ray', 'HD_DVD') NOT NULL,
  `director` varchar(255) NOT NULL,
  `runtime` integer NOT NULL,
  `studio` varchar(255) NOT NULL,
  `language` varchar(255) NOT NULL,
  `subtitles` varchar(255) NOT NULL,
  `release_date` datetime,
  `genre` varchar(255)
);

CREATE TABLE `order_item` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `media_id` integer NOT NULL,
  `order_id` integer NOT NULL,
  `quantity` integer NOT NULL
);

CREATE TABLE `order` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `total_product_amount` integer NOT NULL,
  `shipping_fee` integer NOT NULL,
  `total_final` integer NOT NULL
);

CREATE TABLE `deliver_info` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `order_id` integer NOT NULL,
  `recipient_name` varchar(255) NOT NULL,
  `email` varchar(255) NOT NULL,
  `phone_number` varchar(255) NOT NULL,
  `city` varchar(255) NOT NULL,
  `address` varchar(255) NOT NULL,
  `is_rush` bool NOT NULL
);

CREATE TABLE `transaction` (
  `id` integer PRIMARY KEY AUTO_INCREMENT,
  `order_id` integer NOT NULL,
  `content` integer NOT NULL,
  `transaction_time` timestamp NOT NULL
);

ALTER TABLE `Book` ADD FOREIGN KEY (`product_id`) REFERENCES `media_product` (`id`);

ALTER TABLE `Music_product` ADD FOREIGN KEY (`product_id`) REFERENCES `media_product` (`id`);

ALTER TABLE `DVD` ADD FOREIGN KEY (`product_id`) REFERENCES `media_product` (`id`);

ALTER TABLE `order_item` ADD FOREIGN KEY (`media_id`) REFERENCES `media_product` (`id`);

ALTER TABLE `order_item` ADD FOREIGN KEY (`order_id`) REFERENCES `order` (`id`);

ALTER TABLE `deliver_info` ADD FOREIGN KEY (`order_id`) REFERENCES `order` (`id`);

ALTER TABLE `transaction` ADD FOREIGN KEY (`order_id`) REFERENCES `order` (`id`);

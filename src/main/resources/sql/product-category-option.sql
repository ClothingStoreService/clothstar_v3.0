DROP TABLE IF EXISTS product;
DROP TABLE IF EXISTS product_line;
DROP TABLE IF EXISTS category;


CREATE TABLE `category`
(
    `category_id`   BIGINT      NOT NULL AUTO_INCREMENT,
    `category_type` VARCHAR(20) NOT NULL,
    PRIMARY KEY (`category_id`)
);

CREATE TABLE `productLine`
(
    `product_line_id` BIGINT      NOT NULL AUTO_INCREMENT,
    `member_id`       BIGINT      NOT NULL,
    `category_id`     BIGINT      NOT NULL,
    `name`            VARCHAR(30) NOT NULL,
    `content`         TEXT        NULL NOT NULL,
    `price`           INT         NOT NULL,
    `total_stock`     INT         NOT NULL,
    `status`          VARCHAR(20) NOT NULL COMMENT '준비중, 판매중, 할인중, 품절. 숨김, 단종',
    `sale_count`      BIGINT      NOT NULL,
    `created_at`      TIMESTAMP   NOT NULL,
    `modified_at`     TIMESTAMP,
    `deleted_at`      TIMESTAMP,
    PRIMARY KEY (`product_line_id`),
    FOREIGN KEY (`member_id`) REFERENCES seller (`member_id`),
    FOREIGN KEY (`category_id`) REFERENCES `category` (`category_id`)
);


CREATE TABLE `product`
(
    `product_id`      BIGINT      NOT NULL AUTO_INCREMENT,
    `product_line_id` BIGINT      NOT NULL,
    `name`            VARCHAR(20) NOT NULL,
    `value`           VARCHAR(20),
    `stock`           BIGINT,
    PRIMARY KEY (`product_id`),
    FOREIGN KEY (`product_line_id`) REFERENCES `productLine` (`product_line_id`)
);


select *
from seller;
select *
from member;
select *
from product_line;
select *
from product;
select *
from category;


INSERT INTO category (category_type)
VALUES ('상의');

alter table product_line
    modify column sale_count bigint after status;
ALTER TABLE product_line
    ADD content TEXT AFTER name;
SELECT *
FROM category
WHERE category_id = 2;
ALTER TABLE product_line
    MODIFY COLUMN total_stock BIGINT;

ALTER TABLE product
    MODIFY COLUMN value INT;

ALTER TABLE product
    CHANGE COLUMN extra_fee extra_charge INT;

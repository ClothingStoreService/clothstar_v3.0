CREATE TABLE `member`
(
    `member_id`           BIGINT       NOT NULL auto_increment,
    `email`               varchar(255) NOT NULL,
    `password`            varchar(255) NOT NULL,
    `name`                varchar(255) NOT NULL,
    `tel_no`              varchar(255) NOT NULL,
    `total_payment_price` INT          NULL,
    `point`               INT          NULL,
    `role`                varchar(100) NOT NULL COMMENT 'ADMIN, SELLER, USER',
    `grade`               varchar(100) NOT NULL COMMENT 'BRONZE, SILVER, GOLD, PLATINUM, DIAMOND',
    `created_at`          timestamp    NOT NULL,
    `modified_at`         timestamp    NULL,
    `deleted_at`          timestamp    NULL,
    `enabled`             boolean      NOT NULL DEFAULT FALSE,

    CONSTRAINT PK_member PRIMARY KEY (member_id),
    CONSTRAINT UK_member_email UNIQUE (email)
);

DROP TABLE IF EXISTS `member`;

DROP TABLE IF EXISTS `address`;

CREATE TABLE `address`
(
    `address_id`       BIGINT       NOT NULL auto_increment,
    `member_id`        BIGINT       NOT NULL,
    `receiver_name`    varchar(255) NULL,
    `zip_no`           varchar(255) NOT NULL,
    `address_basic`    varchar(255) NOT NULL,
    `address_detail`   varchar(255) NOT NULL,
    `tel_no`           varchar(255) NOT NULL,
    `delivery_request` varchar(255) NULL,
    `default_address`  boolean      NOT NULL DEFAULT 0,

    CONSTRAINT PK_ADDRESS PRIMARY KEY (address_id)
);

DROP TABLE IF EXISTS `seller`;

CREATE TABLE `seller`
(
    `member_id`        BIGINT       NOT NULL,
    `brand_name`       varchar(255) NOT NULL,
    `biz_no`           varchar(255) NULL,
    `total_sell_price` int          NULL,
    `created_at`       timestamp    NOT NULL,

    CONSTRAINT PK_seller PRIMARY KEY (member_id)
);

select *
from member;
select *
from address;
select *
from seller;

select *
from information_schema.table_constraints
where constraint_schema = 'dev_clothstar';

select *
from information_schema.TABLES
where TABLE_SCHEMA = 'dev_clothstar';

select *
from address;
select *
from seller;
select *
from productLine;
select *
from member
where email = 'rkdgustn@test.com';

select *
from information_schema.table_constraints
where constraint_schema = 'dev_clothstar'
  and CONSTRAINT_TYPE = 'FOREIGN KEY';

alter table `productLine`
    drop foreign key `FK_seller_TO_product_1`;

alter table `seller`
    drop foreign key `FK_member_TO_seller_1`;

ALTER TABLE `seller`
    ADD CONSTRAINT `FK_member_TO_seller_1` FOREIGN KEY (`member_id`)
        REFERENCES `member` (`member_id`);

ALTER TABLE `productLine`
    ADD CONSTRAINT `FK_seller_TO_product_1` FOREIGN KEY (`member_id`)
        REFERENCES `seller` (`member_id`);

INSERT INTO clothstar.seller (member_id, brand_name, biz_no, total_sell_price, created_at)
VALUES (3, '내셔널지오그래픽키즈 제주점', '232-05-02861', 3000000, '2024-03-29 03:59:07');

select *
from address;
where member_id = 1;

select *
from member;

select *
from address;

select *
from seller;

alter table member
    change modified_at updated_at timestamp;

ALTER TABLE member
    ADD COLUMN enabled boolean not null default false;

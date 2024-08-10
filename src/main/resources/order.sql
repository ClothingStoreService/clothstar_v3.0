CREATE TABLE `order_detail`
(
    `fixed_price`         int         DEFAULT NULL,
    `onekind_total_price` int         DEFAULT NULL,
    `quantity`            int    NOT NULL,
    `created_at`          datetime(6) DEFAULT NULL,
    `deleted_at`          datetime(6) DEFAULT NULL,
    `item_id`             bigint      DEFAULT NULL,
    `order_detail_id`     bigint NOT NULL AUTO_INCREMENT,
    `order_id`            VARCHAR(64) DEFAULT NULL,
    `product_id`          bigint      DEFAULT NULL,
    `updated_at`          datetime(6) DEFAULT NULL,
    PRIMARY KEY (`order_detail_id`),
    KEY `FKrws2q0si6oyd6il8gqe2aennc` (`order_id`),
    CONSTRAINT `FKrws2q0si6oyd6il8gqe2aennc` FOREIGN KEY (`order_id`) REFERENCES `orders` (`order_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 9
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

CREATE TABLE `orders`
(
    `total_payment_price`  int                                                                                                      NOT NULL,
    `total_products_price` int                                                                                                      NOT NULL,
    `total_shipping_price` int                                                                                                      NOT NULL,
    `address_id`           bigint                                                                                                   NOT NULL,
    `created_at`           datetime(6) DEFAULT NULL,
    `deleted_at`           datetime(6) DEFAULT NULL,
    `member_id`            bigint                                                                                                   NOT NULL,
    `order_id`             VARCHAR(64)                                                                                              NOT NULL,
    `updated_at`           datetime(6) DEFAULT NULL,
    `payment_method`       enum ('CARD','KAKAOPAY','NAVERPAY') COLLATE utf8mb4_general_ci                                           NOT NULL,
    `status`               enum ('CANCELED','COMPLETED','CONFIRMED','DELIVERED','PROCESSING','SHIPPING') COLLATE utf8mb4_general_ci NOT NULL,
    PRIMARY KEY (`order_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;

INSERT INTO orders (order_id, member_id, address_id, created_at, status, total_shipping_price, total_products_price,
                    payment_method, total_payment_price)
VALUES ('14241232', '242', '334', CURRENT_TIMESTAMP, 'WAITING', '3000', '50000', 'CARD', '53000');

INSERT INTO member (point, total_payment_price, created_at, deleted_at, member_id, updated_at, name, tel_no, grade)
VALUES (100, 5000, CURRENT_TIMESTAMP, NULL, 1, NULL, '수빈', 010 - 1234 - 5678, 'GOLD');

INSERT INTO address (address_id, created_at, deleted_at, member_id, updated_at, address_basic, address_detail,
                     delivery_request, receiver_name, tel_no, zip_no)
VALUES (1, CURRENT_TIMESTAMP, NULL, 1, NULL, '123', '123', '문앞', '수빈', 010 - 1234 - 5678, 010101);

INSERT INTO item (display_status, final_price, name, sale_status, stock, item_id)
VALUES (0,19900,'오구',0,10,1);

INSERT INTO product (product_id, created_at, deleted_at, updated_at, category_id, content, display_status, member_id, name, price, sale_count, sale_status)
VALUES (1,CURRENT_TIMESTAMP,NULL,CURRENT_TIMESTAMP,1,'반팔','HIDDEN',1,'오구반팔',19900,1000,'ON_SALE');

show create table order_detail;

drop table if exists orders;

REPAIR TABLE orders;


select *
from orders;
select *
from order_detail;
select *
from item;
select *
from product;
select *
from address;
DROP TABLE IF EXISTS orders;

CREATE TABLE orders
(
    `order_id`             bigint       NOT NULL,
    `member_id`            bigint       NOT NULL,
    `address_id`           bigint       NOT NULL,
    `status`               varchar(255) NOT NULL,
    `total_shipping_price` int          NOT NULL,
    `total_products_price` int          NOT NULL,
    `payment_method`       varchar(255) NOT NULL,
    `total_payment_price`  int          NOT NULL,
    `created_at`           timestamp    NOT NULL,
    `updated_at`           timestamp    NULL,
    `deleted_at`           timestamp    NULL,

    PRIMARY KEY (`order_id`)
);


ALTER TABLE orders
    DROP PRIMARY KEY;

select distinct *
from orders oe1_0
         #          join member m1_0 on m1_0.member_id = oe1_0.member_id
#          join address a1_0 on a1_0.address_id = oe1_0.address_id
         join order_detail od1_0 on oe1_0.order_id = od1_0.order_id
#          join product p1_0 on p1_0.product_id = od1_0.product_id
#          join product_line pl1_0 on pl1_0.product_line_id = p1_0.product_line_id
where oe1_0.order_id = 202406297932955;


select oe1_0.order_id,
       oe1_0.address_id,
       oe1_0.created_at,
       oe1_0.member_id,
       oe1_0.payment_method,
       oe1_0.status,
       oe1_0.total_payment_price,
       oe1_0.total_products_price,
       oe1_0.total_shipping_price,
       od1_0.order_detail_id,
       od1_0.brand_name,
       od1_0.fixed_price,
       od1_0.name,
       od1_0.onekind_total_price,
       od1_0.option_name,
       od1_0.product_id,
       od1_0.product_line_id,
       od1_0.quantity,
       od1_0.stock,
       m1_0.member_id,
       m1_0.created_at,
       m1_0.deleted_at,
       m1_0.email,
       m1_0.grade,
       m1_0.name,
       m1_0.password,
       m1_0.point,
       m1_0.role,
       m1_0.tel_no,
       m1_0.total_payment_price,
       m1_0.updated_at,
       a1_0.address_id,
       a1_0.address_basic,
       a1_0.address_detail,
       a1_0.default_address,
       a1_0.delivery_request,
       a1_0.member_id,
       a1_0.receiver_name,
       a1_0.tel_no,
       a1_0.zip_no,
       pl1_0.product_line_id,
       pl1_0.category_id,
       pl1_0.content,
       pl1_0.created_at,
       pl1_0.deleted_at,
       pl1_0.modified_at,
       pl1_0.name,
       pl1_0.price,
       pl1_0.sale_count,
       pl1_0.member_id,
       pl1_0.status,
       pl1_0.total_stock
from orders oe1_0
         join member m1_0 on m1_0.member_id = oe1_0.member_id
         join address a1_0 on a1_0.address_id = oe1_0.address_id
         join order_detail od1_0 on oe1_0.order_id = od1_0.order_id
         join product p1_0 on p1_0.product_id = od1_0.product_id
         join product_line pl1_0 on pl1_0.product_line_id = p1_0.product_line_id
where oe1_0.order_id = 202406297932955
group by oe1_0.order_id;

select ae1_0.address_id,
       ae1_0.address_basic,
       ae1_0.address_detail,
       ae1_0.default_address,
       ae1_0.delivery_request,
       m1_0.member_id,
       m1_0.created_at,
       m1_0.deleted_at,
       m1_0.email,
       m1_0.grade,
       m1_0.name,
       m1_0.password,
       m1_0.point,
       m1_0.role,
       m1_0.tel_no,
       m1_0.total_payment_price,
       m1_0.updated_at,
       ae1_0.receiver_name,
       ae1_0.tel_no,
       ae1_0.zip_no
from address ae1_0
         left join member m1_0 on m1_0.member_id = ae1_0.member_id
where ae1_0.address_id = 8;

select ode1_0.order_detail_id,
       ode1_0.brand_name,
       ode1_0.fixed_price,
       ode1_0.name,
       ode1_0.onekind_total_price,
       ode1_0.option_name,
       ode1_0.order_id,
       ode1_0.product_id,
       ode1_0.product_line_id,
       ode1_0.quantity,
       ode1_0.stock
from order_detail ode1_0
where ode1_0.order_id = 202407037765571;


select *
from orders;
select *
from member;
select *
from address;
select *
from order_detail;
select *
from product_line;
select *
from product;

select *
from order_detail
where order_id = 202405251562452;

ALTER TABLE `orders`
    ADD CONSTRAINT `FK_member_TO_orders_1` FOREIGN KEY (`member_id`)
        REFERENCES `member` (`member_id`);

ALTER TABLE orders
    DROP FOREIGN KEY `FK_member_TO_orders_1`;

ALTER TABLE `orders`
    ADD CONSTRAINT `FK_address_TO_orders_1` FOREIGN KEY (`address_id`)
        REFERENCES `address` (`address_id`);

ALTER TABLE orders
    DROP FOREIGN KEY `FK_address_TO_orders_1`;

SHOW CREATE TABLE orders;
SHOW CREATE TABLE address;
SHOW CREATE TABLE member;

show index from orders;

select se1_0.member_id,
       se1_0.biz_no,
       se1_0.brand_name,
       se1_0.created_at,
       se1_0.deleted_at,
       m1_0.member_id,
       m1_0.created_at,
       m1_0.deleted_at,
       m1_0.email,
       m1_0.grade,
       m1_0.name,
       m1_0.password,
       m1_0.point,
       m1_0.role,
       m1_0.tel_no,
       m1_0.total_payment_price,
       m1_0.updated_at,
       se1_0.total_sell_price,
       se1_0.updated_at
from seller se1_0
         join member m1_0 on m1_0.member_id = se1_0.member_id
where se1_0.member_id = 1;


select distinct oe1_0.order_id,
                oe1_0.address_id,
                oe1_0.created_at,
                oe1_0.member_id,
                oe1_0.payment_method,
                oe1_0.status,
                oe1_0.total_payment_price,
                oe1_0.total_products_price,
                oe1_0.total_shipping_price,
                od1_0.order_detail_id,
                od1_0.brand_name,
                od1_0.fixed_price,
                od1_0.name,
                od1_0.onekind_total_price,
                od1_0.option_name,
                od1_0.product_id,
                od1_0.product_line_id,
                od1_0.quantity,
                od1_0.stock,
                m1_0.member_id,
                m1_0.created_at,
                m1_0.deleted_at,
                m1_0.email,
                m1_0.grade,
                m1_0.name,
                m1_0.password,
                m1_0.point,
                m1_0.role,
                m1_0.tel_no,
                m1_0.total_payment_price,
                m1_0.updated_at,
                a1_0.address_id,
                a1_0.address_basic,
                a1_0.address_detail,
                a1_0.default_address,
                a1_0.delivery_request,
                a1_0.member_id,
                a1_0.receiver_name,
                a1_0.tel_no,
                a1_0.zip_no,
                pl1_0.product_line_id,
                pl1_0.category_id,
                pl1_0.content,
                pl1_0.created_at,
                pl1_0.deleted_at,
                pl1_0.modified_at,
                pl1_0.name,
                pl1_0.price,
                pl1_0.sale_count,
                pl1_0.member_id,
                pl1_0.status,
                pl1_0.total_stock
from orders oe1_0
         join member m1_0 on m1_0.member_id = oe1_0.member_id
         join address a1_0 on a1_0.address_id = oe1_0.address_id
         join order_detail od1_0 on oe1_0.order_id = od1_0.order_id
         join product p1_0 on p1_0.product_id = od1_0.product_id
         join product_line pl1_0 on pl1_0.product_line_id = p1_0.product_line_id
where oe1_0.order_id = 202405287820770;


drop index FK_member_TO_orders_1 on orders;

DELETE
FROM orders
where member_id = 1;

INSERT INTO orders (order_id, member_id, address_id, created_at, status, total_shipping_price, total_products_price,
                    payment_method, total_payment_price)
VALUES ('14241232', '242', '334', CURRENT_TIMESTAMP, 'WAITING', '3000', '50000', 'CARD', '53000');



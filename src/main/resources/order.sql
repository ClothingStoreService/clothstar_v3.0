INSERT INTO orders (order_id, member_id, address_id, created_at, status, total_shipping_price, total_products_price,
                    payment_method, total_payment_price)
VALUES ('14241232', '242', '334', CURRENT_TIMESTAMP, 'WAITING', '3000', '50000', 'CARD', '53000');

select *
from address;

select o1_0.order_id,o1_0.address_id,o1_0.created_at,o1_0.deleted_at,o1_0.member_id,o1_0.payment_method,o1_0.status,o1_0.total_payment_price,o1_0.total_products_price,o1_0.total_shipping_price,o1_0.updated_at from orders o1_0 where o1_0.status='CONFIRMED' and o1_0.deleted_at is null;

select o1_0.order_id,
       o1_0.address_id,
       o1_0.created_at,
       o1_0.deleted_at,
       o1_0.member_id,
       o1_0.payment_method,
       o1_0.status,
       o1_0.total_payment_price,
       o1_0.total_products_price,
       o1_0.total_shipping_price,
       o1_0.updated_at
from orders o1_0
where o1_0.order_id = 202407176782686;



select o1_0.order_id,
       o1_0.address_id,
       o1_0.created_at,
       o1_0.deleted_at,
       o1_0.member_id,
       o1_0.payment_method,
       o1_0.status,
       o1_0.total_payment_price,
       o1_0.total_products_price,
       o1_0.total_shipping_price,
       o1_0.updated_at
from orders o1_0
where o1_0.order_id = 202407176782686;

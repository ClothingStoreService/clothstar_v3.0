INSERT INTO orders (order_id, member_id, address_id, created_at, status, total_shipping_price, total_products_price,
                    payment_method, total_payment_price)
VALUES ('14241232', '242', '334', CURRENT_TIMESTAMP, 'WAITING', '3000', '50000', 'CARD', '53000');

INSERT INTO member (point, total_payment_price, created_at, deleted_at, member_id, updated_at, name, tel_no, grade)
VALUES (100, 5000, CURRENT_TIMESTAMP, NULL, 1, NULL, '수빈', 010 - 1234 - 5678, 'GOLD')

INSERT INTO address (address_id, created_at, deleted_at, member_id, updated_at, address_basic, address_detail,
                     delivery_request, receiver_name, tel_no, zip_no)
VALUES (1, CURRENT_TIMESTAMP, NULL, 1, NULL, '123', '123', '문앞', '수빈', 010 - 1234 - 5678, 010101);

select *
from orders;
select *
from order_detail;

select *
from address;
select *
from item;
select *
from product;
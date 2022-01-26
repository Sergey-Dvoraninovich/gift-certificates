INSERT INTO gift_certificates(name, description, price, duration, create_date, last_update_date, available)
VALUES ('certificate first and second tags', 'certificate with first tag and second tags', 50.00, 90,
        '2000-01-01 11:11:11.222', '2000-01-01 11:11:11.222', true);

INSERT INTO gift_certificates(name, description, price, duration, create_date, last_update_date, available)
VALUES ('certificate second tag', 'certificate with second tag', 100.00, 180, '2011-01-01 11:11:11.222',
        '2011-01-01 11:11:11.222', true);

INSERT INTO gift_certificates(name, description, price, duration, create_date, last_update_date, available)
VALUES ('certificate', 'certificate', 200.00, 180, '2022-01-01 11:11:11.222', '2022-01-01 11:11:11.222', true);

INSERT INTO tags (name)
VALUES ('first tag');
INSERT INTO tags (name)
VALUES ('second tag');
INSERT INTO tags (name)
VALUES ('third tag');

INSERT INTO gift_certificates_tags(id_gift_certificate, id_tag)
VALUES (1, 1);
INSERT INTO gift_certificates_tags(id_gift_certificate, id_tag)
VALUES (1, 2);
INSERT INTO gift_certificates_tags(id_gift_certificate, id_tag)
VALUES (2, 2);

INSERT INTO ROLES(id, name)
VALUES (1, 'USER');
INSERT INTO ROLES(id, name)
VALUES (2, 'ADMIN');

INSERT INTO users(login, name, surname, email, id_role, password)
VALUES ('christian_altman', 'Christian', 'Altman', 'christian.altman@gmail.com', 1,
        'a0f3285b07c26c0dcd2191447f391170d06035e8d57e31a048ba87074f3a9a15');
INSERT INTO users(login, name, surname, email, id_role, password)
VALUES ('cindy_clark', 'Cindy', 'Clark', 'cindy.clark@gmail.com', 1,
        'a0f3285b07c26c0dcd2191447f391170d06035e8d57e31a048ba87074f3a9a15');

INSERT INTO orders(id, id_user, create_order_time, update_order_time)
VALUES (1, 1, '2000-01-01 11:11:11.222', '2000-01-01 11:11:11.222');
INSERT INTO orders(id, id_user, create_order_time, update_order_time)
VALUES (2, 1, '2011-01-01 11:11:11.222', '2011-01-01 11:11:11.222');
INSERT INTO orders(id, id_user, create_order_time, update_order_time)
VALUES (3, 2, '2022-01-01 11:11:11.222', '2022-01-01 11:11:11.222');

INSERT INTO order_items(id, id_order, id_gift_certificate, price)
VALUES (1, 1, 1, 72.92);
INSERT INTO order_items(id, id_order, id_gift_certificate, price)
VALUES (2, 1, 2, 392.92);

INSERT INTO order_items(id, id_order, id_gift_certificate, price)
VALUES (3, 2, 2, 92.92);

INSERT INTO order_items(id, id_order, id_gift_certificate, price)
VALUES (4, 3, 3, 192.92);
INSERT INTO order_items(id, id_order, id_gift_certificate, price)
VALUES (5, 3, 2, 67.92);


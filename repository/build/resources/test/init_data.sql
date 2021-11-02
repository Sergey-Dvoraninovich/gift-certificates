USE in_memory_certificates;

INSERT INTO gift_certificates(name, description, price, duration, create_date, last_update_date)
VALUES('certificate first and second tags', 'certificate with first tag and second tag', 50.00, 90, '2000-01-01 11:11:11.222', '2000-01-01 11:11:11.222');

INSERT INTO gift_certificates(name, description, price, duration, create_date, last_update_date)
VALUES('certificate second tags', 'certificate with second tag', 100.00, 180, '2011-01-01 11:11:11.222', '2011-01-01 11:11:11.222');

INSERT INTO gift_certificates(name, description, price, duration, create_date, last_update_date)
VALUES('certificate', 'certificate', 200.00, 180, '2022-01-01 11:11:11.222', '2022-01-01 11:11:11.222');

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
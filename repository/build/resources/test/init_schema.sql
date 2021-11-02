USE in_memory_certificates;

CREATE TABLE gift_certificates (
    id INT NOT NULL AUTO_INCREMENT UNIQUE,
    name VARCHAR(45),
    description VARCHAR(500),
    price DECIMAL(5, 2),
    duration SMALLINT,
    create_date TIMESTAMP(3),
    last_update_date TIMESTAMP(3),
    PRIMARY KEY (id)
);

CREATE TABLE tags (
    id INT NOT NULL AUTO_INCREMENT UNIQUE,
    name VARCHAR(45),
    PRIMARY KEY (id)
);

CREATE TABLE gift_certificates_tags (
    id_gift_certificate INT NOT NULL,
    id_tag INT NOT NULL,
    CONSTRAINT gift_certificates_fk FOREIGN KEY (id_gift_certificate) REFERENCES gift_certificates (id) ON DELETE CASCADE,
    CONSTRAINT tags_fk FOREIGN KEY (id_tag) REFERENCES tags (id) ON DELETE CASCADE
);
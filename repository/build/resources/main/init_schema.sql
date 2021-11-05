USE in_memory_certificates;

CREATE TABLE gift_certificates (
    id INT NOT NULL AUTO_INCREMENT UNIQUE,
    name VARCHAR(45) NOT NULL UNIQUE,
    description VARCHAR(500) NOT NULL,
    price DECIMAL(5, 2) NOT NULL,
    duration SMALLINT NOT NULL,
    create_date TIMESTAMP(3) NOT NULL,
    last_update_date TIMESTAMP(3) NOT NULL,
    PRIMARY KEY (id),
    INDEX full_ordering (name, create_date),
    INDEX date_ordering (create_date)
);

CREATE TABLE tags (
    id INT NOT NULL AUTO_INCREMENT UNIQUE,
    name VARCHAR(45) NOT NULL UNIQUE,
    PRIMARY KEY (id)
);

CREATE TABLE gift_certificates_tags (
    id_gift_certificate INT NOT NULL,
    id_tag INT NOT NULL,
    CONSTRAINT gift_certificates_fk FOREIGN KEY (id_gift_certificate) REFERENCES gift_certificates (id) ON DELETE CASCADE,
    CONSTRAINT tags_fk FOREIGN KEY (id_tag) REFERENCES tags (id) ON DELETE CASCADE,
    INDEX gift_certificates_search (id_gift_certificate, id_tag)
);
CREATE TABLE contact
(
    id        BIGINT AUTO_INCREMENT NOT NULL,
    name      VARCHAR(255) NULL,
    telephone VARCHAR(255) NULL,
    email     VARCHAR(255) NULL,
    CONSTRAINT pk_contact PRIMARY KEY (id)
);

CREATE TABLE person
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    name       VARCHAR(255) NULL,
    cpf        VARCHAR(14) NULL,
    birth_date date NOT NULL,
    CONSTRAINT pk_person PRIMARY KEY (id)
);

CREATE TABLE person_contact_list
(
    person_id       BIGINT NOT NULL,
    contact_list_id BIGINT NOT NULL
);

ALTER TABLE person_contact_list
    ADD CONSTRAINT fk_perconlis_on_contact FOREIGN KEY (contact_list_id) REFERENCES contact (id);

ALTER TABLE person_contact_list
    ADD CONSTRAINT fk_perconlis_on_person FOREIGN KEY (person_id) REFERENCES person (id);
create table individual_info
(
    id             bigserial
        constraint individual_info_pk
            primary key,
    last_name      text,
    first_name     text,
    patronymic     text,
    phone          text,
    contact_email  text,
    user_id        bigint not null
        constraint individual_info_fk_user_id
            references users,
    avatar_file_id text,
    type           varchar(255) default 'IP'::character varying
);

create table organization_info
(
    id           bigserial
        constraint organization_info_pk
            primary key,
    inn          text,
    kpp          text,
    address      text,
    ogrn         text,
    user_id      bigint not null
        constraint organization_info_fk_user_id
            references users,
    company_name text,
    email        varchar(255),
    fio          varchar(255),
    okvd         text,
    type         varchar(255) default 'LEGAL_ENTITY'::character varying not null
);
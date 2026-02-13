create table users
(
    user_id       bigserial
        constraint user_pk
            primary key,
    registered_at timestamp default CURRENT_TIMESTAMP not null
);

create table token
(
    id            bigserial
        constraint token_pk
            primary key,
    user_id       bigint    not null
        constraint auth_email_fk_user_id
            references users,
    expire_at     timestamp not null,
    refresh_token text      not null
);

create table user_authority
(
    id        bigserial not null
        constraint user_authority_pk
            primary key,
    user_id   bigint    not null
        constraint user_authority_users_user_id_fk
            references users,
    authority text      not null
);

create table auth_code
(
    id                  bigserial
        constraint auth_code_pk
            primary key,
    code                text      not null
        constraint auth_code_uq_code
            unique,
    verification_id     text      not null
        constraint auth_code_uq_verification_id
            unique,
    status              text      not null,
    approved_by_user_id bigint
        constraint auth_code_fk_user_id
            references users,
    expire_at           timestamp not null,
    external_id         bigint
);

create table auth_phone
(
    id      bigserial
        constraint auth_phone_pk
            primary key,
    phone   text   not null,
    user_id bigint not null
        constraint auth_phone_fk_user_id
            references users
);

create table phone_code
(
    id             bigserial
        constraint phone_code_pk
            primary key,
    phone          text      not null,
    code           text      not null,
    attempt_number integer   not null,
    expire_at      timestamp not null
);
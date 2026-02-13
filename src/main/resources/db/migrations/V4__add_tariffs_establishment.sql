create table tariff
(
    id                 bigserial
        constraint tariff_pk
            primary key,
    name               varchar(255)       not null,
    description        text,
    type               varchar(255)       not null,
    price              double precision   not null,
    active             boolean            not null,
    created_by_user_id bigint
        constraint tariff_users_user_id_fk
            references users,
    "order"            smallint default 0 not null
);

create table tariff_feature
(
    id        bigserial
        constraint feature_pk
            primary key,
    name      varchar(255) not null,
    recurrent boolean      not null,
    tariff_id bigint       not null
        constraint feature_tariff_id_fk
            references tariff
);

create table tariff_sub_feature
(
    id                bigserial
        constraint tariff_sub_feature_pk
            primary key,
    name              varchar(255)     not null,
    approval_required boolean          not null,
    price             double precision not null,
    tariff_feature_id bigint           not null
        constraint tariff_sub_feature_tariff_feature_id_fk
            references tariff_feature
);

INSERT INTO tariff (name, description, type, price, active)
VALUES ('Тариф 1', 'Базовое описание Тарифа 1', 'COMMON', 7990, true),
       ('Тариф 2', 'Базовое описание Тарифа 2', 'COMMON', 10000, true);

INSERT INTO tariff_feature (name, recurrent, tariff_id)
VALUES ('Выплаты правообладателям', false, 1);

INSERT INTO tariff_sub_feature (name, approval_required, price, tariff_feature_id)
VALUES ('Оплачиваю сам', false, 0, 1),
       ('Оплачиваю через ПУСК', true, 0, 1);

INSERT INTO tariff_feature (name, recurrent, tariff_id)
VALUES ('Выплаты правообладателям', true, 2);

INSERT INTO tariff_sub_feature (name, approval_required, price, tariff_feature_id)
VALUES ('Оплачиваю сам', false, 0, 2),
       ('Оплачиваю через ПУСК', true, 0, 2);

INSERT INTO tariff_feature (name, recurrent, tariff_id)
VALUES ('Обновление контента', true, 2);

INSERT INTO tariff_sub_feature (name, approval_required, price, tariff_feature_id)
VALUES ('Нужно', false, 15000, 3),
       ('Не нужно', false, 0, 3);

INSERT INTO tariff_feature (name, recurrent, tariff_id)
VALUES ('Разработка концепции', false, 2);

INSERT INTO tariff_sub_feature (name, approval_required, price, tariff_feature_id)
VALUES ('Нужна', false, 50000, 4),
       ('Не нужна', false, 0, 4);

create table tariff_playlist
(
    id          bigserial
        constraint tariff_playlist_pk
            primary key,
    tariff_id   bigint not null
        constraint tariff_playlist_tariff_id_fk
            references tariff
            on update cascade on delete cascade,
    playlist_id bigint not null
        constraint tariff_playlist_playlist_id_fk
            references playlist
            on update cascade on delete cascade,
    user_id     bigint
        constraint tariff_playlist_users_user_id_fk
            references users
            on update cascade on delete cascade
);

create table establishment
(
    id               bigserial
        constraint establishment_pk
            primary key,
    name             varchar(255)     not null,
    address          varchar(255)     not null,
    area             double precision not null,
    layout_file_id   varchar(255),
    user_id          bigint           not null
        constraint establishment_users_user_id_fk
            references users,
    layout_file_size bigint default 0
);

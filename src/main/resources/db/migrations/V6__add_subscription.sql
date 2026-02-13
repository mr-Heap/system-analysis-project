create table subscription
(
    id                   bigserial
        constraint subscription_pk
            primary key,
    created_at           timestamp        not null,
    establishment_id     bigint           not null
        constraint subscription_pk_2
            unique
        constraint subscription___fk
            references establishment,
    tariff_id            bigint           not null
        constraint subscription___fk_2
            references tariff,
    user_id              bigint           not null
        constraint subscription___fk_3
            references users,
    price                double precision not null,
    active               boolean          not null,
    start_date           timestamp        not null,
    end_date             timestamp        not null,
    organization_info_id bigint
        constraint subscription_organization_info_id_fk
            references organization_info,
    payment_account_id   bigint,
    payment_card_id      bigint
);

create table payment
(
    id              bigserial
        constraint payment_pk
            primary key,
    name            varchar(255),
    datetime        timestamp             not null,
    price           double precision      not null,
    status          varchar(64)           not null,
    payment_id      varchar(64)           not null,
    order_id        varchar(64)           not null,
    user_id         bigint                not null
        constraint payment_users_user_id_fk
            references users,
    recurrent       boolean default false not null,
    rebill_id       bigint,
    subscription_id bigint                not null
        constraint payment___fk
            references subscription
);

create table receipt
(
    id         bigserial not null
        constraint receipt_pk
            primary key,
    url        varchar(512) default ''::character varying                 not null,
    payment_id bigint                                                     not null
        constraint receipt_payment_id_fk
            references payment
);

create table subscription_tariff_sub_feature
(
    subscription_id       bigint not null
        constraint table_name_subscription_id_fk
            references subscription
            on update cascade on delete cascade,
    tariff_sub_feature_id bigint not null
        constraint table_name_tariff_sub_feature_id_fk
            references tariff_sub_feature
            on update cascade on delete cascade
);


create table payment_card
(
    id                   bigserial
        constraint payment_card_pk
            primary key,
    external_card_id     varchar(255) not null,
    pan                  varchar(255) not null,
    bank_logo_url        text,
    organization_info_id bigint       not null
        constraint payment_card_organization_info_id_fk
            references organization_info
            on update cascade on delete cascade,
    owner_user_id        bigint       not null
        constraint payment_card_users_user_id_fk
            references users
            on update cascade on delete cascade,
    re_bill_id           bigint
);

create table payment_account
(
    id                   bigserial
        constraint payment_account_pk
            primary key,
    account_number       varchar(255) not null,
    bank_branch_name     text         not null,
    bank_branch_address  text         not null,
    bank_bik             varchar(255) not null,
    correspondent_number varchar(255) not null,
    organization_info_id bigint       not null
        constraint payment_account_organization_info_id_fk
            references organization_info
            on update cascade on delete cascade,
    owner_user_id        bigint       not null
        constraint payment_account_users_id_fk
            references users
            on update cascade on delete cascade
);
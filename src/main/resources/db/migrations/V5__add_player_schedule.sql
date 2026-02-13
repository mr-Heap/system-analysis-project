create table player
(
    id                bigserial
        constraint player_pk
            primary key,
    owner_user_id     bigint           not null
        constraint player_fk_user_id
            references users,
    type              text,
    name              text,
    address           text,
    serial_box_number text,
    establishment_id  bigint default 1 not null
        constraint player_establishment_id_fk
            references establishment,
    version           text
);

create table schedule
(
    id                 bigserial
        constraint schedule_pk
            primary key,
    weekdays           text not null,
    start_time         time,
    end_time           time,
    playlist_id        bigint
        constraint fk_schedule_playlist_id
            references playlist
            on delete cascade,
    start_weekend_time time,
    end_weekend_time   time,
    establishment_id   bigint
        constraint schedule_establishment_id_fk
            references establishment,
    timezone           varchar(255)
);

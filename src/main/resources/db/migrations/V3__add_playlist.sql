create table playlist
(
    id                      bigserial
        constraint playlist_pk
            primary key,
    avatar_file_id          text,
    name                    text,
    description             text,
    year                    smallint,
    added_by_user_id        bigint not null
        constraint playlist_fk_added_by_user_id
            references users,
    summary_track_file_id   text,
    summary_track_file_name text
);
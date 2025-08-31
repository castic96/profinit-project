CREATE SCHEMA IF NOT EXISTS gitlab_service

USE gitlab_service

create table if not exists gitlab_service.gitlab_user
(
    id         bigint       not null primary key,
    username   varchar(255) null,
    name       varchar(255) null,
    state      varchar(255) null,
    locked     tinyint(1)   null,
    avatar_url varchar(255) null,
    updated    datetime(6)  null,
    web_url    varchar(255) null
);

create table if not exists gitlab_service.gitlab_project
(
    id              bigint       not null primary key,
    description     varchar(255) null,
    name            varchar(255) null,
    web_url         varchar(255) null,
    git_lab_user_id bigint       null,
    constraint gitlab_project_ibfk_1
        foreign key (git_lab_user_id) references gitlab_service.gitlab_user (id)
);

create index gitLabUserId
    on gitlab_service.gitlab_project (git_lab_user_id);


create sequence IF NOT EXISTS answer_seq start 1 increment 1;
create sequence IF NOT EXISTS answer_vote_seq start 1 increment 1;
create sequence IF NOT EXISTS question_seq start 1 increment 1;
create sequence IF NOT EXISTS badge_seq start 1 increment 1;
create sequence IF NOT EXISTS chat_seq start 1 increment 1;
create sequence IF NOT EXISTS comment_seq start 1 increment 1;
create sequence IF NOT EXISTS ignore_tag_seq start 1 increment 1;
create sequence IF NOT EXISTS message_seq start 1 increment 1;
create sequence IF NOT EXISTS question_viewed_seq start 1 increment 1;
create sequence IF NOT EXISTS role_seq start 1 increment 1;
create sequence IF NOT EXISTS user_seq start 1 increment 1;
create sequence IF NOT EXISTS related_tag_seq start 1 increment 1;
create sequence IF NOT EXISTS reputation_seq start 1 increment 1;
create sequence IF NOT EXISTS tag_seq start 1 increment 1;
create sequence IF NOT EXISTS tracked_tag_seq start 1 increment 1;
create sequence IF NOT EXISTS user_badges_seq start 1 increment 1;
create sequence IF NOT EXISTS user_favorite_question_seq start 1 increment 1;
create sequence IF NOT EXISTS vote_question_seq start 1 increment 1;



create table if not exists role
(
    id   bigint not null
        constraint role_pkey
            primary key,
    name varchar(255)
);

create table if not exists user_entity
(
    id                  bigint    not null
        constraint user_entity_pkey
            primary key,
    about               varchar(255),
    city                varchar(255),
    email               varchar(255),
    full_name           varchar(255),
    image_link          varchar(255),
    is_deleted          boolean,
    is_enabled          boolean,
    last_redaction_date timestamp not null,
    link_github         varchar(255),
    link_site           varchar(255),
    link_vk             varchar(255),
    nickname            varchar(255),
    password            varchar(255),
    persist_date        timestamp,
    role_id             bigint    not null
        constraint FK_USER_ENTITY_ON_ROLE
            references role
);



create table if not exists question
(
    id                  bigint       not null
        constraint question_pkey
            primary key,
    description         text         not null,
    is_deleted          boolean,
    last_redaction_date timestamp    not null,
    persist_date        timestamp,
    title               varchar(255) not null,
    user_id             bigint       not null
        constraint FK_QUESTION_ON_USER
            references user_entity
);

create table if not exists answer
(
    id                      bigint    not null
        constraint answer_pkey
            primary key,
    date_accept_time        timestamp,
    html_body               text      not null,
    is_deleted              boolean   not null,
    is_deleted_by_moderator boolean   not null,
    is_helpful              boolean   not null,
    persist_date            timestamp,
    update_date             timestamp not null,
    question_id             bigint    not null
        constraint FK_ANSWER_ON_QUESTION
            references question,
    user_id                 bigint    not null
        constraint FK_ANSWER_ON_USER
            references user_entity
);


create table if not exists badges
(
    id                    bigint not null
        constraint badges_pkey
            primary key,
    badge_name            varchar(255),
    description           varchar(255),
    reputations_for_merit integer
);

create table if not exists bookmarks
(
    id          bigserial not null
        constraint bookmarks_pkey
            primary key,
    question_id bigint    not null
        constraint FK_BOOKMARKS_ON_QUESTION
            references question,
    user_id     bigint    not null
        constraint FK_BOOKMARKS_ON_USER
            references user_entity
);

create table if not exists chat
(
    id           int8 not null
        constraint chat_pkey primary key,
    chat_type    smallint,
    persist_date timestamp,
    title        varchar(255)
);

create table if not exists comment
(
    id                  bigint       not null
        constraint comment_pkey
            primary key,
    comment_type        smallint     not null,
    last_redaction_date timestamp,
    persist_date        timestamp,
    text                varchar(255) not null,
    user_id             bigint       not null
        constraint FK_COMMENT_ON_USER
            references user_entity
);


create table if not exists comment_answer
(
    comment_id bigint not null
        constraint comment_answer_pkey
            primary key
        constraint FK_COMMENT_ANSWER_ON_COMMENT
            references comment,
    answer_id  bigint not null
        constraint FK_COMMENT_ANSWER_ON_ANSWER
            references answer
);


create table if not exists comment_question
(
    comment_id  bigint not null
        constraint comment_question_pkey
            primary key
        constraint FK_COMMENT_QUESTION_ON_COMMENT
            references comment,
    question_id bigint not null
        constraint FK_COMMENT_QUESTION_ON_QUESTION
            references question
);


create table if not exists group_chat
(
    chat_id bigint not null
        constraint group_chat_pkey
            primary key
        constraint FK_GROUP_CHAT_ON_CHAT
            references chat
);

create table if not exists groupchat_has_users
(
    chat_id bigint not null
        constraint FK_GROUP_CHAT_HAS_USERS_ON_GROUP_CHAT
            references group_chat,
    user_id bigint not null
        constraint FK_GROUP_CHAT_HAS_USERS_ON_USER
            references user_entity,
    constraint groupchat_has_users_pkey
        primary key (chat_id, user_id)
);


create table if not exists message
(
    id                  bigint    not null
        constraint message_pkey
            primary key,
    last_redaction_date timestamp not null,
    message             text,
    persist_date        timestamp,
    chat_id             bigint    not null
        constraint FK_MESSAGE_ON_CHAT
            references chat,
    user_sender_id      bigint    not null
        constraint FK_MESSAGE_ON_USERSENDER
            references user_entity
);


create table if not exists tag
(
    id           bigint       not null
        constraint tag_pkey
            primary key,
    description  varchar(255),
    name         varchar(255) not null,
    persist_date timestamp
);


create table if not exists question_has_tag
(
    question_id bigint not null
        constraint FK_QUESTION_HAS_TAG_ON_QUESTION
            references question,
    tag_id      bigint not null
        constraint FK_QUESTION_HAS_TAG_ON_TAG
            references tag
);

create table if not exists question_viewed
(
    id           bigint not null
        constraint question_viewed_pkey
            primary key,
    persist_date timestamp,
    question_id  bigint
        constraint FK_QUESTION_VIEWED_ON_QUESTION
            references question,
    user_id      bigint
        constraint FK_QUESTION_VIEWED_ON_USER
            references user_entity
);

create table if not exists related_tag
(
    id        bigint not null
        constraint related_tag_pkey
            primary key,
    child_tag bigint not null
        constraint FK_RELATED_TAG_ON_CHILD_TAG
            references tag,
    main_tag  bigint not null
        constraint FK_RELATED_TAG_ON_MAIN_TAG
            references tag
);

create table if not exists reputation
(
    id           bigint  not null
        constraint reputation_pkey
            primary key,
    count        integer,
    persist_date timestamp,
    type         integer not null,
    answer_id    bigint
        constraint FK_REPUTATION_ON_ANSWER
            references answer,
    author_id    bigint  not null
        constraint FK_REPUTATION_ON_AUTHOR
            references user_entity,
    question_id  bigint
        constraint FK_REPUTATION_ON_QUESTION
            references question,
    sender_id    bigint
        constraint FK_REPUTATION_ON_SENDER
            references user_entity
);

create table if not exists single_chat
(
    chat_id     bigint not null
        constraint single_chat_pkey
            primary key
        constraint FK_SINGLE_CHAT_ON_CHAT
            references chat,
    use_two_id  bigint not null
        constraint FK_SINGLE_CHAT_ON_USERTWO
            references user_entity,
    user_one_id bigint not null
        constraint FK_SINGLE_CHAT_ON_USERONE
            references user_entity
);

create table if not exists tag_ignore
(
    id             bigint not null
        constraint tag_ignore_pkey
            primary key,
    persist_date   timestamp,
    ignored_tag_id bigint not null
        constraint FK_TAG_IGNORE_ON_IGNOREDTAG
            references tag,
    user_id        bigint not null
        constraint FK_TAG_IGNORE_ON_USER
            references user_entity
);

create table if not exists tag_tracked
(
    id             bigint not null
        constraint tag_tracked_pkey
            primary key,
    persist_date   timestamp,
    tracked_tag_id bigint not null
        constraint FK_TAG_TRACKED_ON_TRACKEDTAG
            references tag,
    user_id        bigint not null
        constraint FK_TAG_TRACKED_ON_USER
            references user_entity
);

create table if not exists user_badges
(
    id        bigint not null
        constraint user_badges_pkey
            primary key,
    ready     boolean,
    badges_id bigint
        constraint FK_USER_BADGES_ON_BADGES
            references badges,
    user_id   bigint
        constraint FK_USER_BADGES_ON_USER
            references user_entity
);


create table if not exists user_favorite_question
(
    id           bigint    not null
        constraint user_favorite_question_pkey
            primary key,
    persist_date timestamp not null,
    question_id  bigint    not null
        constraint FK_USER_FAVORITE_QUESTION_ON_QUESTION
            references question,
    user_id      bigint    not null
        constraint FK_USER_FAVORITE_QUESTION_ON_USER
            references user_entity
);

create table if not exists votes_on_answers
(
    id           bigint not null
        constraint votes_on_answers_pkey
            primary key,
    persist_date timestamp,
    vote         integer,
    answer_id    bigint not null
        constraint FK_VOTES_ON_ANSWERS_ON_ANSWER
            references answer,
    user_id      bigint not null
        constraint FK_VOTES_ON_ANSWERS_ON_USER
            references user_entity
);

create table if not exists votes_on_questions
(
    id           bigint  not null
        constraint votes_on_questions_pkey
            primary key,
    persist_date timestamp,
    vote         integer not null,
    question_id  bigint
        constraint FK_VOTES_ON_QUESTIONS_ON_QUESTION
            references question,
    user_id      bigint
        constraint FK_VOTES_ON_QUESTIONS_ON_USER
            references user_entity
);
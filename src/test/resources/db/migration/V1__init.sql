drop sequence if exists answer_seq;
drop sequence if exists answer_vote_seq;
drop sequence if exists badge_seq;
drop sequence if exists chat_seq;
drop sequence if exists comment_seq;
drop sequence if exists ignore_tag_seq;
drop sequence if exists message_seq;
drop sequence if exists question_seq;
drop sequence if exists question_viewed_seq;
drop sequence if exists related_tag_seq;
drop sequence if exists reputation_seq;
drop sequence if exists role_seq;
drop sequence if exists tag_seq;
drop sequence if exists tracked_tag_seq;
drop sequence if exists user_seq;
drop sequence if exists user_badges_seq;
drop sequence if exists user_favorite_question_seq;
drop sequence if exists vote_question_seq;

create sequence answer_seq start 1 increment 1;
create sequence answer_vote_seq start 1 increment 1;
create sequence badge_seq start 1 increment 1;
create sequence chat_seq start 1 increment 1;
create sequence comment_seq start 1 increment 1;
create sequence ignore_tag_seq start 1 increment 1;
create sequence message_seq start 1 increment 1;
create sequence question_seq start 1 increment 1;
create sequence question_viewed_seq start 1 increment 1;
create sequence related_tag_seq start 1 increment 1;
create sequence reputation_seq start 1 increment 1;
create sequence role_seq start 1 increment 1;
create sequence tag_seq start 1 increment 1;
create sequence tracked_tag_seq start 1 increment 1;
create sequence user_seq start 1 increment 1;
create sequence user_badges_seq start 1 increment 1;
create sequence user_favorite_question_seq start 1 increment 1;
create sequence vote_question_seq start 1 increment 1;

drop table if exists answer cascade;
drop table if exists badges cascade;
drop table if exists bookmarks cascade;
drop table if exists chat cascade;
drop table if exists comment cascade;
drop table if exists comment_answer cascade;
drop table if exists comment_question cascade;
drop table if exists group_chat cascade;
drop table if exists groupchat_has_users cascade;
drop table if exists message cascade;
drop table if exists question cascade;
drop table if exists question_has_tag cascade;
drop table if exists question_viewed cascade;
drop table if exists related_tag cascade;
drop table if exists reputation cascade;
drop table if exists role cascade;
drop table if exists singel_chat cascade;
drop table if exists tag cascade;
drop table if exists tag_ignore cascade;
drop table if exists tag_tracked cascade;
drop table if exists user_badges cascade;
drop table if exists user_entity cascade;
drop table if exists user_favorite_question cascade;
drop table if exists votes_on_answers cascade;
drop table if exists votes_on_questions cascade;


create table answer
(
    id                      bigint    not null,
    date_accept_time        timestamp,
    html_body               text      not null,
    is_deleted              boolean   not null,
    is_deleted_by_moderator boolean   not null,
    is_helpful              boolean   not null,
    persist_date            timestamp,
    update_date             timestamp not null,
    question_id             bigint    not null,
    user_id                 bigint    not null,
    primary key (id)
);

create table badges
(
    id                    bigint not null,
    badge_name            varchar(255),
    description           varchar(255),
    reputations_for_merit integer,
    primary key (id)
);

create table bookmarks
(
    id bigserial not null,
    question_id bigint not null,
    user_id     bigint not null,
    primary key (id)
);

create table chat
(
    id           bigint not null,
    chat_type    smallint,
    persist_date timestamp,
    title        varchar(255),
    primary key (id)
);

create table comment
(
    id                  bigint       not null,
    comment_type        smallint     not null,
    last_redaction_date timestamp,
    persist_date        timestamp,
    text                varchar(255) not null,
    user_id             bigint       not null,
    primary key (id)
);

create table comment_answer
(
    comment_id bigint not null,
    answer_id  bigint not null,
    primary key (comment_id)
);

create table comment_question
(
    comment_id  bigint not null,
    question_id bigint not null,
    primary key (comment_id)
);

create table group_chat
(
    chat_id bigint not null,
    primary key (chat_id)
);

create table groupchat_has_users
(
    chat_id bigint not null,
    user_id bigint not null,
    primary key (chat_id, user_id)
);

create table message
(
    id                  bigint    not null,
    last_redaction_date timestamp not null,
    message             text,
    persist_date        timestamp,
    chat_id             bigint    not null,
    user_sender_id      bigint    not null,
    primary key (id)
);

create table question
(
    id                  bigint       not null,
    description         text         not null,
    is_deleted          boolean,
    last_redaction_date timestamp    not null,
    persist_date        timestamp,
    title               varchar(255) not null,
    user_id             bigint       not null,
    primary key (id)
);

create table question_has_tag
(
    question_id bigint not null,
    tag_id      bigint not null
);

create table question_viewed
(
    id           bigint not null,
    persist_date timestamp,
    question_id  bigint,
    user_id      bigint,
    primary key (id)
);

create table related_tag
(
    id        bigint not null,
    child_tag bigint not null,
    main_tag  bigint not null,
    primary key (id)
);

create table reputation
(
    id           bigint  not null,
    count        integer,
    persist_date timestamp,
    type         integer not null,
    answer_id    bigint,
    author_id    bigint  not null,
    question_id  bigint,
    sender_id    bigint,
    primary key (id)
);

create table role
(
    id   bigint not null,
    name varchar(255),
    primary key (id)
);

create table single_chat
(
    chat_id     bigint not null,
    use_two_id  bigint not null,
    user_one_id bigint not null,
    primary key (chat_id)
);

create table tag
(
    id           bigint       not null,
    description  varchar(255),
    name         varchar(255) not null,
    persist_date timestamp,
    primary key (id)
);

create table tag_ignore
(
    id             bigint not null,
    persist_date   timestamp,
    ignored_tag_id bigint not null,
    user_id        bigint not null,
    primary key (id)
);

create table tag_tracked
(
    id             bigint not null,
    persist_date   timestamp,
    tracked_tag_id bigint not null,
    user_id        bigint not null,
    primary key (id)
);

create table user_badges
(
    id        bigint not null,
    ready     boolean,
    badges_id bigint,
    user_id   bigint,
    primary key (id)
);

create table user_entity
(
    id                  bigint    not null,
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
    role_id             bigint    not null,
    primary key (id)
);

create table user_favorite_question
(
    id           bigint    not null,
    persist_date timestamp not null,
    question_id  bigint    not null,
    user_id      bigint    not null,
    primary key (id)
);

create table votes_on_answers
(
    id           bigint not null,
    persist_date timestamp,
    vote         integer,
    answer_id    bigint not null,
    user_id      bigint not null,
    primary key (id)
);

create table votes_on_questions
(
    id           bigint  not null,
    persist_date timestamp,
    vote         integer not null,
    question_id  bigint,
    user_id      bigint,
    primary key (id)
);

alter table answer
    add constraint FK_ANSWER_ON_QUESTION
        foreign key (question_id)
            references question;

alter table answer
    add constraint FK_ANSWER_ON_USER
        foreign key (user_id)
            references user_entity;

alter table bookmarks
    add constraint FK_BOOKMARKS_ON_QUESTION
        foreign key (question_id)
            references question;

alter table bookmarks
    add constraint FK_BOOKMARKS_ON_USER
        foreign key (user_id)
            references user_entity;

alter table comment
    add constraint FK_COMMENT_ON_USER
        foreign key (user_id)
            references user_entity;

alter table comment_answer
    add constraint FK_COMMENT_ANSWER_ON_ANSWER
        foreign key (answer_id)
            references answer;

alter table comment_answer
    add constraint FK_COMMENT_ANSWER_ON_COMMENT
        foreign key (comment_id)
            references comment;

alter table comment_question
    add constraint FK_COMMENT_QUESTION_ON_COMMENT
        foreign key (comment_id)
            references comment;

alter table comment_question
    add constraint FK_COMMENT_QUESTION_ON_QUESTION
        foreign key (question_id)
            references question;

alter table group_chat
    add constraint FK_GROUP_CHAT_ON_CHAT
        foreign key (chat_id)
            references chat;

alter table groupchat_has_users
    add constraint FK_GROUPCHAT_HAS_USERS_ON_USER
        foreign key (user_id)
            references user_entity;

alter table groupchat_has_users
    add constraint FK_GROUPCHAT_HAS_USERS_ON_GROUP_CHAT
        foreign key (chat_id)
            references group_chat;

alter table message
    add constraint FK_MESSAGE_ON_CHAT
        foreign key (chat_id)
            references chat;

alter table message
    add constraint FK_MESSAGE_ON_USER
        foreign key (user_sender_id)
            references user_entity;

alter table question
    add constraint FK_QUESTION_ON_USER
        foreign key (user_id)
            references user_entity;

alter table question_has_tag
    add constraint FK_QUESTION_HAS_TAG_ON_TAG
        foreign key (tag_id)
            references tag;

alter table question_has_tag
    add constraint FK_QUESTION_HAS_TAG_ON_QUESTION
        foreign key (question_id)
            references question;

alter table question_viewed
    add constraint FK_QUESTION_VIEWED_ON_QUESTION
        foreign key (question_id)
            references question;

alter table question_viewed
    add constraint FK_QUESTION_VIEWED_ON_USER
        foreign key (user_id)
            references user_entity;

alter table related_tag
    add constraint FK_RELATED_TAG_ON_TAG_child_tag
    foreign key (child_tag)
    references tag;

alter table related_tag
    add constraint FK_RELATED_TAG_ON_TAG_main_tag
    foreign key (main_tag)
    references tag;

alter table reputation
    add constraint FK_REPUTATION_ON_ANSWER
        foreign key (answer_id)
            references answer;

alter table reputation
    add constraint FK_REPUTATION_ON_USER_author_id
    foreign key (author_id)
    references user_entity;

alter table reputation
    add constraint FK_REPUTATION_ON_QUESTION
        foreign key (question_id)
            references question;

alter table reputation
    add constraint FK_REPUTATION_ON_USER_sender_id
    foreign key (sender_id)
    references user_entity;

alter table single_chat
    add constraint FK_SINGLE_CHAT_ON_CHAT
        foreign key (chat_id)
            references chat;

alter table single_chat
    add constraint FK_SINGLE_CHAT_ON_USER_use_two_id
    foreign key (use_two_id)
    references user_entity;

alter table single_chat
    add constraint FK_SINGLE_CHAT_ON_USER_user_one_id
    foreign key (user_one_id)
    references user_entity;

alter table tag_ignore
    add constraint FK_TAG_IGNORE_ON_TAG
        foreign key (ignored_tag_id)
            references tag;

alter table tag_ignore
    add constraint FK_TAG_IGNORE_ON_USER
        foreign key (user_id)
            references user_entity;

alter table tag_tracked
    add constraint FK_TAG_TRACKED_ON_TAG
        foreign key (tracked_tag_id)
            references tag;

alter table tag_tracked
    add constraint FK_TAG_TRACKED_ON_USER
        foreign key (user_id)
            references user_entity;

alter table user_badges
    add constraint FK_USER_BADGES_ON_BADGES
        foreign key (badges_id)
            references badges;

alter table user_badges
    add constraint FK_USER_BADGES_ON_USER
        foreign key (user_id)
            references user_entity;

alter table user_entity
    add constraint FK_USER_ON_ROLE
        foreign key (role_id)
            references role;

alter table user_favorite_question
    add constraint FK_USER_FAVORITE_QUESTION_ON_QUESTION
        foreign key (question_id)
            references question;

alter table user_favorite_question
    add constraint FK_USER_FAVORITE_QUESTION_ON_USER
        foreign key (user_id)
            references user_entity;

alter table votes_on_answers
    add constraint FK_VOTES_ON_ANSWER_ON_ANSWER
        foreign key (answer_id)
            references answer;

alter table votes_on_answers
    add constraint FK_VOTES_ON_ANSWER_ON_USER
        foreign key (user_id)
            references user_entity;

alter table votes_on_questions
    add constraint FK_VOTES_ON_QUESTIONS_ON_QUESTION
        foreign key (question_id)
            references question;

alter table votes_on_questions
    add constraint FK_VOTES_ON_QUESTIONS_ON_USER
        foreign key (user_id)
            references user_entity;
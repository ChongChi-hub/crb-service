use `sea_turtle_web`;

create table if not exists `category`
(
    id         bigint not null auto_increment,
    `name`     varchar(50),
    `url_name` varchar(255),
    constraint category_pk primary key (id)
);
create table if not exists topic
(
    id          bigint not null auto_increment,
    category_id bigint not null,
    `name`      varchar(100),
    `url_name`  varchar(255),
    description varchar(500),
    icon        varchar(20),
    constraint topic_pk primary key (id),
    constraint topic_category_fk foreign key (category_id) references `category` (id)
);

create table if not exists post
(
    id          bigint not null auto_increment,
    topic_id    bigint  default null,
    `name`      varchar(200),
    description varchar(500),
    title_image text,
    `content`   longtext,
    date_post   datetime,
    url_name    varchar(500),
    author      varchar(50),
    is_hidden   boolean default false,
    category_id bigint  default null,
    icon        VARCHAR(20),
    keyword     varchar(255),
    table_id    longtext,
    constraint post_pk primary key (id),
    constraint post_topic_fk foreign key (topic_id) references topic (id),
    constraint post_category_fk foreign key (category_id) REFERENCES category (id)
);

create table if not exists image
(
    id          bigint not null auto_increment,
    `name`      varchar(250),
    link        text,
    topic_name  varchar(100),
    description text,
    image_category varchar(250),
    type varchar(10) null,
    link_to_post varchar(250) DEFAULT '#',
    is_delete BOOLEAN DEFAULT FALSE,
    year int null,
    constraint image_pk primary key (id)
);

CREATE TABLE if not exists branch
(
    id      INT AUTO_INCREMENT PRIMARY KEY,
    name    VARCHAR(100),
    address VARCHAR(100)
);

CREATE TABLE IF NOT EXISTS role
(
    id   BIGINT NOT NULL AUTO_INCREMENT,
    name VARCHAR(20) UNIQUE,
    CONSTRAINT role_pk PRIMARY KEY (id)
);

CREATE TABLE if not exists `table_price`
(
    id              INT AUTO_INCREMENT PRIMARY KEY,
    title_table     varchar(255),
    `content_table` longtext
);

create table if not exists image_dir
(
    id   int auto_increment primary key,
    name varchar(255)
);

create table if not exists `user`
(
    id             bigint              not null auto_increment,
    `username`     varchar(20)         not null unique,
    `password`     varchar(100)        not null,
    role_id        BIGINT              not null,
    full_name      VARCHAR(50),
    email          VARCHAR(100) UNIQUE NOT NULL,
    phone          VARCHAR(15) UNIQUE  NOT NULL,
    is_deleted     bit default 0,
    is_activated   bit default 1,
    remember_token VARCHAR(200),
    last_login DATETIME,
    verify_code VARCHAR(20),
    verify_code_expiration VARCHAR(50),
    constraint user_pk primary key (id),
    CONSTRAINT user_role_fk FOREIGN KEY (role_id) REFERENCES role (id)
);

CREATE TABLE if not exists contact
(
    id           INT AUTO_INCREMENT PRIMARY KEY,
    phone_number VARCHAR(15),
    description  varchar(100)
);


DROP TABLE IF EXISTS `facebook`.`refresh_token`;
DROP TABLE `facebook`.`confirm_code`;
DROP TABLE `facebook`.`user_accounts`;


create table user_accounts
(
    id         bigint auto_increment primary key,
    email      varchar(50)                                                                  not null,
    password   varchar(500)                                                                  not null,
    last_name  varchar(50)                                                                  not null default 'User',
    first_name varchar(50)                                                                  not null default 'New',
    phone      varchar(20)                                                                  null,
    birthday date not null,
    `gender` enum('male','female','unknown') not null,
    `role` enum('ROLE_USER','ROLE_ADMIN') NOT NULL DEFAULT 'ROLE_USER',
    avatar     json                                                                         null,
    created_at timestamp              default current_timestamp                             null,
    updated_at timestamp              default current_timestamp on update current_timestamp null,
    status varchar(20) default 'not_activated',
    `privacy_default` enum('public','friend','except_friend','specific_friend','only_me','custom') default 'friend',
    login_attempts INT default 0 null,
    unique (email)
);

insert into user_accounts(email,password,first_name,last_name,birthday,gender) values('trananhthi10@gmail.com','123456','thi','tran','2023-10-19','female');
insert into reactions(post_id,user_id,type_reaction) values('1','13','like');


create table refresh_token
(
    id bigint auto_increment primary key,
    user_account_id bigint,
    token varchar(255) not null unique,
    expiry_date timestamp not null,
    foreign key (user_account_id) references user_accounts(id)
);

create table confirm_code
(
    id bigint auto_increment primary key,
    user_account_id bigint,
    code varchar(6) not null unique,
    expiry_date timestamp not null,
    foreign key (user_account_id) references user_accounts(id)
);


CREATE TABLE user_posts (
    id bigint auto_increment primary key,
    author_id bigint not null ,
    content text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    created_at timestamp default current_timestamp null,
    updated_at timestamp default current_timestamp on update current_timestamp null,
    type_post varchar(255) not null,
    video json,
    parent_post bigint,
    view int default 0,
    privacy varchar(255),
    tag varchar(255),
    hashtag varchar(255),
    priority int default 0,
    `status` enum('published','deleted','under_review','archived') default 'published',
    foreign key (author_id) references user_accounts(id),
    foreign key (parent_post) references user_posts(id)
);

ALTER TABLE post_images
DROP COLUMN author_id;

ALTER TABLE post_images
DROP FOREIGN KEY post_images_ibfk_1;

ALTER TABLE post_images
MODIFY COLUMN post_id bigint not null;

ALTER TABLE post_images
ADD CONSTRAINT post_images_ibfk_1
FOREIGN KEY (post_id)
REFERENCES user_posts(id);

create table post_images(
	id bigint auto_increment primary key,
    post_id bigint not null,
    url text not null,
    status varchar(50) default "actived",
    created_at timestamp default current_timestamp null,
	foreign key (post_id) references user_posts(id),
    foreign key (author_id) references user_accounts(id)
);

create table reactions (
	id bigint auto_increment primary key,
    post_id bigint not null,
    user_id bigint not null ,
    `type_reaction` enum('like','love','care','haha','wow','sad','angry') not null,
    created_at timestamp default current_timestamp null,
    status varchar(20) default 'active',
    foreign key (post_id) references user_posts(id),
    foreign key (user_id) references user_accounts(id),
    unique (post_id,user_id)
);


-- insert into reactions(post_id,user_id,type_reaction,created_at,status) values(1,2,'like','2023-11-05 23:21:18','actived');
insert into reactions(post_id,user_id,type_reaction) values(1,3,'haha');

INSERT INTO reactions (post_id, user_id, type_reaction)
VALUES (1,2,'wow')
ON DUPLICATE KEY UPDATE type_reaction = 'wow';




create table comments (
	id bigint auto_increment primary key,
    post_id bigint,
    user_id bigint,
    content text CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci,
    created_at timestamp default current_timestamp null,
    updated_at timestamp default current_timestamp on update current_timestamp null,
    status varchar(20) default 'active',
    foreign key (post_id) references user_posts(id),
    foreign key (user_id) references user_accounts(id)
);

create table shares (
	id bigint auto_increment primary key,
    post_id bigint ,
    user_id bigint ,
    created_at timestamp default current_timestamp null,
    status varchar(20) default 'active',
    foreign key (post_id) references user_posts(id),
    foreign key (user_id) references user_accounts(id)
);

CREATE TABLE chat_room (
    id bigint auto_increment primary key,
    user1_id bigint NOT NULL,
    user2_id bigint NOT NULL,
    room_name text,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status varchar(20) default 'active',
    FOREIGN KEY (user1_id) REFERENCES user_accounts(id),
    FOREIGN KEY (user2_id) REFERENCES user_accounts(id)
);

CREATE TABLE chat_message (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    room_id BIGINT NOT NULL,
    sender_id BIGINT NOT NULL,
    content TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    status VARCHAR(20) DEFAULT 'active',
    FOREIGN KEY (room_id) REFERENCES chat_room(id),
    FOREIGN KEY (sender_id) REFERENCES user_accounts(id)
);




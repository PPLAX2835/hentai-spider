drop database if exists spiderData;

create database spiderData character set 'utf8';

use spiderData;

create table artist (
    id bigint not null auto_increment                                                   comment '主键',
    name varchar(255) not null                                                          comment '作者昵称',
    update_at datetime default current_timestamp on update current_timestamp            comment '更新时间',
    insert_at datetime default current_timestamp                                        comment '添加时间',
    primary key (id)
) comment '总的作者表';

create table platform_artist (
    id bigint not null auto_increment                                                   comment '主键',
    artist_id bigint not null                                                           comment '作者id',
    platform_id bigint not null                                                         comment '平台id',
    name varchar(255) not null                                                          comment '作者昵称',
    homepage_url varchar(300)                                                           comment '主页url',
    update_at datetime default current_timestamp on update current_timestamp            comment '更新时间',
    insert_at datetime default current_timestamp                                        comment '添加时间',
    primary key (id)
) comment '作者在对应平台的信息';

create table platform (
    id bigint not null auto_increment                                                   comment '主键',
    name varchar(50) not null                                                           comment '平台名称',
    url varchar(255) not null                                                           comment '地址',
    update_at datetime default current_timestamp on update current_timestamp            comment '更新时间',
    insert_at datetime default current_timestamp                                        comment '添加时间',
    primary key (id)
) comment '平台表';

create table file (
    id bigint not null auto_increment                                                  comment '主键',
    artist_id bigint not null                                                           comment '作者id',
    platform_id integer not null                                                        comment '平台id',
    id_in_platform varchar(255) unique not null                                         comment '在该平台的id',
    hash varchar(300) unique                                                            comment '该文件的散列值',
    file_url varchar(300)                                                               comment '文件url',
    file_type varchar(30)                                                               comment '文件类型',
    file_name varchar(100)                                                              comment '文件名',
    file_path varchar(300)                                                              comment '文件的存储地址',
    file_size integer                                                                   comment '文件大小',
    page_url varchar(300)                                                               comment '文件的主页',
    update_at datetime default current_timestamp on update current_timestamp            comment '更新时间',
    insert_at datetime default current_timestamp                                        comment '添加时间',
    primary key (id)
) comment '文件表，记录文件信息';

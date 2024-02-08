drop database if exists spiderData;

create database spiderData character set 'utf8';

use spiderData;

create table platform (
    id integer not null auto_increment                                                  comment '主键',
    name varchar(50) not null                                                           comment '平台名称',
    url varchar(100) not null                                                           comment '地址',
    update_at datetime default current_timestamp on update current_timestamp            comment '更新时间',
    insert_at datetime default current_timestamp                                        comment '添加时间',
    primary key (id)
) comment '平台表';

create table artist (
    id bigint not null auto_increment                                                   comment '主键',
    name varchar(100) not null                                                          comment '作者昵称',
    update_at datetime default current_timestamp on update current_timestamp            comment '更新时间',
    insert_at datetime default current_timestamp                                        comment '添加时间',
    primary key (id)
) comment '作者表，只是负责联系另外几个表';

create table artist_homepage_url (
    id integer not null auto_increment                                                  comment '主键',
    artist_id bigint not null                                                           comment '作者id',
    platform_id integer not null                                                        comment '平台id',
    homepage_url varchar(300)                                                           comment '主页url',
    update_at datetime default current_timestamp on update current_timestamp            comment '更新时间',
    insert_at datetime default current_timestamp                                        comment '添加时间',
    primary key (id)
) comment '作者主页地址表，通过platform_id与platform关联';

create table artist_name (
    id integer not null auto_increment                                                  comment '主键',
    artist_id bigint not null                                                           comment '作者id',
    platform_id integer not null                                                        comment '平台id',
    name varchar(100) not null                                                          comment '作者昵称',
    update_at datetime default current_timestamp on update current_timestamp            comment '更新时间',
    insert_at datetime default current_timestamp                                        comment '添加时间',
    primary key (id)
) comment '作者昵称表，通过platform_id与platform关联';

create table file (
    id integer not null auto_increment                                                  comment '主键',
    artist_id bigint not null                                                           comment '作者id',
    platform_id integer not null                                                        comment '平台id',
    id_in_platform varchar(100) unique not null                                         comment '在该平台的id',
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


select artist.id as artist_id, artist.name as artist_name, artist_homepage_url.homepage_url as homepage_url, artist_name.name artist_platform_name, platform.id as platform_id, platform.name as platform_name, platform.url as platform_url
from artist_name
left join artist_homepage_url on artist_name.artist_id = artist_homepage_url.artist_id
left join artist on artist_name.artist_id = artist.id
left join platform on platform.id = artist_name.platform_id
where platform.name = 'FurAffinity'
and artist.name = 'sollyz'
;
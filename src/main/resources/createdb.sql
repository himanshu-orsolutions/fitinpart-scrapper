create database fitinpart_db;

create table fitinpart(
	`id` bigint(20) primary key auto_increment,
	`parent_id` bigint(20),
	`info` text not null,
	`tag` varchar(10) not null,
	`next_url` varchar(500),
	`http_method` varchar(10),
	`http_body` varchar(500),
	`is_scrapped` boolean
);
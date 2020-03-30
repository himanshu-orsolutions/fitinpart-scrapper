create database fitinpart_db;

create table fitinpart(
	`id` bigint(20) primary key auto_increment,
	`info` text not null,
	`parameters` varchar(500),
	`tag` varchar(10) not null,
	`is_scrapped` boolean
);
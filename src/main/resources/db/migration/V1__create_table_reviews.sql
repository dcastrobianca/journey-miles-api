create table reviews(
    id bigint auto_increment,
    name varchar(400) not null,
    description varchar(2000) not null,
    photo_path varchar(800),
    primary key(id)
)
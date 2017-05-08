drop schema if exists project;

create schema project;
use project;

# you should run this schema again after modifying the cart.selling_date to timestamp
# run all triggers, insert statements and procedures
# add a trigger when updating book price
create table book (
	ISBN               int primary key,
    title              varchar(100) not null,
    quantity           int not null,
    threshold          int not null,
    publication_year   date,
    selling_price      real not null,
    category_id        int,
    publisher_name     varchar(100)
);

create table author (
	ISBN         int,
    author_name  varchar(100),
    primary key (ISBN, author_name)
);

create table publisher (
	publisher_name      varchar(100) primary key,
    address             varchar(100) not null,
    phone               varchar(20) not null
);

create table category (
	id              int primary key,
    category_type   varchar(20) unique not null
);

create table customer (
	id                 int primary key auto_increment,
    email              varchar(20) unique not null,
    first_name         varchar(20) not null,
    last_name          varchar(20),
    shipping_address   varchar(100) not null,
    phone              varchar(20),
    is_manager         boolean not null
);

create table book_order (
	id                int primary key auto_increment,
	publisher_name    varchar(100) not null,
    ISBN              int not null,
    quantity          int not null
);

create table cart (
	id                    int auto_increment primary key,
	customer_id           int not null,
    ISBN                  int not null,
    quantity              int not null,
    selling_price         real not null
);

create table sold (
	id            int auto_increment primary key,
    customer_id   int not null,
    ISBN          int not null,
    quantity      int not null,
    selling_price real not null,
    selling_date  date not null
);

# book table
alter table book add constraint fk1 foreign key (publisher_name) references publisher(publisher_name);
alter table book add constraint fk2 foreign key (category_id) references category(id);


# author table
alter table author add constraint fk3 foreign key (ISBN) references book(ISBN) on update cascade;


# book_order table
alter table book_order add constraint fk4 foreign key (ISBN) references book(ISBN) on update cascade;
alter table book_order add constraint fk5 foreign key (publisher_name) references publisher(publisher_name);

# cart table 
alter table cart add constraint fk6 foreign key (ISBN) references book(ISBN) on update cascade;
alter table cart add constraint fk7 foreign key (customer_id) references customer(id);

# sold table
alter table sold add constraint fk8 foreign key (ISBN) references book(ISBN) on update cascade;
alter table sold add constraint fk9 foreign key (customer_id) references customer(id);

# customer table
alter table customer add username varchar(100);
alter table customer add password varchar(20);

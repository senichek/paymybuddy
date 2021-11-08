DROP TABLE IF EXISTS connections cascade;
DROP TABLE IF EXISTS transactions;
DROP TABLE IF EXISTS users cascade;

create table users(
ID int PRIMARY KEY AUTO_INCREMENT,
name varchar(25) NOT NULL,
email varchar(25) NOT NULL,
password varchar(50) NOT NULL,
balance integer(25)
);

create table connections(
ID int PRIMARY KEY AUTO_INCREMENT,
owner_id integer(10),
email varchar(25) NOT NULL,
FOREIGN KEY (owner_id)
REFERENCES users(ID)
);

create table transactions(
ID int PRIMARY KEY AUTO_INCREMENT,
sender_id integer(10) NOT NULL,
sender_email varchar(25) NOT NULL,
receiver_email varchar(25) NOT NULL,
description varchar(150) NOT NULL,
amount integer(25),
FOREIGN KEY (sender_id)
REFERENCES users(ID)
);

insert into users(name,email,password,balance) values('James', 'james@gmail.com', 'pass111', 120);
insert into users(name,email,password,balance) values('Mike', 'mike@gmail.com', 'pass111', 80);
insert into users(name,email,password,balance) values('Carol', 'carol@gmail.com', 'pass111', 0);

insert into connections(owner_id,email) values(1, 'mike@gmail.com');
insert into connections(owner_id,email) values(1, 'carol@gmail.com');
insert into connections(owner_id,email) values(3, 'james@gmail.com');

insert into transactions(sender_id, sender_email,receiver_email,description,amount) VALUES(2, 'mike@gmail.com', 'james@gmail.com', 'Dinner', 20);
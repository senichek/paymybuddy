DROP TABLE IF EXISTS connections cascade;
DROP TABLE IF EXISTS transactions cascade;
DROP TABLE IF EXISTS users cascade;

create table users(
ID int PRIMARY KEY AUTO_INCREMENT,
name varchar(25) NOT NULL,
email varchar(25) NOT NULL UNIQUE,
password varchar(250) NOT NULL,
balance double
);

create table connections(
ID int PRIMARY KEY AUTO_INCREMENT,
owner_id integer(10) NOT NULL,
friend_id integer(10) NOT NULL,
friend_email varchar(25) NOT NULL,
FOREIGN KEY (owner_id)
REFERENCES users(ID) ON DELETE CASCADE,
FOREIGN KEY (friend_id)
REFERENCES users(ID) ON DELETE CASCADE
);

create table transactions(
ID int PRIMARY KEY AUTO_INCREMENT,
sender_id integer(10) NOT NULL,
sender_email varchar(25) NOT NULL,
receiver_id integer(10) NOT NULL,
receiver_email varchar(25) NOT NULL,
description varchar(150) NOT NULL,
amount double,
tax double,
date_time DATETIME NOT NULL,
FOREIGN KEY (sender_id)
REFERENCES users(ID) ON DELETE CASCADE,
FOREIGN KEY (receiver_id)
REFERENCES users(ID) ON DELETE CASCADE
);

/* The un-crypted password for all three users is pass111 */
insert into users(name, email, password, balance) values('James', 'james@gmail.com', '$2a$04$GGTsxpDhsbZS9gDKUUpqPOxXuc/9PU1h56ueiFHk68PhY0flyJJmy', 120);
insert into users(name, email, password, balance) values('Mike', 'mike@gmail.com', '$2a$04$TsT2QU1qEJmpawJsyExnlOspyEeR32AxmkKzDibQwodasckkPp21W', 80);
insert into users(name, email, password, balance) values('Carol', 'carol@gmail.com', '$2a$04$TsT2QU1qEJmpawJsyExnlOspyEeR32AxmkKzDibQwodasckkPp21W', 0);
insert into users(name, email, password, balance) values('Ron', 'ron@gmail.com', '$2a$04$TsT2QU1qEJmpawJsyExnlOspyEeR32AxmkKzDibQwodasckkPp21W', 40);

insert into connections(owner_id, friend_id, friend_email) values(1, 2, 'mike@gmail.com');
insert into connections(owner_id, friend_id, friend_email) values(1, 3, 'carol@gmail.com');
insert into connections(owner_id, friend_id, friend_email) values(3, 1, 'james@gmail.com');

insert into transactions(sender_id, sender_email, receiver_id, receiver_email, description, amount, tax, date_time) VALUES(3, 'carol@gmail.com', 1, 'james@gmail.com', 'Dinner', 20.0, 1.0, '2021-11-07 15:00:00');
insert into transactions(sender_id, sender_email, receiver_id, receiver_email, description, amount, tax, date_time) VALUES(1, 'james@gmail.com', 3, 'carol@gmail.com', 'Dinner', 20.0, 1.0, '2021-11-07 12:00:00');
insert into transactions(sender_id, sender_email, receiver_id, receiver_email, description, amount, tax, date_time) VALUES(1, 'james@gmail.com', 3, 'carol@gmail.com', 'Lunch', 20.0, 1.0, '2021-11-07 16:00:00');
insert into transactions(sender_id, sender_email, receiver_id, receiver_email, description, amount, tax, date_time) VALUES(1, 'james@gmail.com', 3, 'carol@gmail.com', 'Supper', 20.0, 1.0, '2021-11-07 20:00:00');
insert into transactions(sender_id, sender_email, receiver_id, receiver_email, description, amount, tax, date_time) VALUES(1, 'james@gmail.com', 3, 'carol@gmail.com', 'Dinner', 20.0, 1.0, '2021-11-08 10:00:00');
insert into transactions(sender_id, sender_email, receiver_id, receiver_email, description, amount, tax, date_time) VALUES(1, 'james@gmail.com', 3, 'carol@gmail.com', 'Lunch', 20.0, 1.0, '2021-11-08 16:00:00');
insert into transactions(sender_id, sender_email, receiver_id, receiver_email, description, amount, tax, date_time) VALUES(1, 'james@gmail.com', 3, 'carol@gmail.com', 'Supper', 20.0, 1.0, '2021-11-08 20:00:00');
insert into transactions(sender_id, sender_email, receiver_id, receiver_email, description, amount, tax, date_time) VALUES(1, 'james@gmail.com', 3, 'carol@gmail.com', 'Dinner', 20.0, 1.0, '2021-11-09 12:00:00');
insert into transactions(sender_id, sender_email, receiver_id, receiver_email, description, amount, tax, date_time) VALUES(1, 'james@gmail.com', 3, 'carol@gmail.com', 'Lunch', 20.0, 1.0, '2021-11-09 16:00:00');
insert into transactions(sender_id, sender_email, receiver_id, receiver_email, description, amount, tax, date_time) VALUES(1, 'james@gmail.com', 3, 'carol@gmail.com', 'Supper', 20.0, 1.0, '2021-11-09 20:00:00');
insert into transactions(sender_id, sender_email, receiver_id, receiver_email, description, amount, tax, date_time) VALUES(1, 'james@gmail.com', 3, 'carol@gmail.com', 'Dinner', 20.0, 1.0, '2021-11-10 12:00:00');
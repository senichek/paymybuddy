DROP TABLE IF EXISTS connections cascade;
DROP TABLE IF EXISTS users cascade;
DROP TABLE IF EXISTS transactions;

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
senderID integer(10),
receiverID integer(10) NOT NULL,
amount integer(25)
);
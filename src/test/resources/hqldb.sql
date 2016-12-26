drop table user if exists;  
  
create table user (  
  uuid varchar(32),  
  userName varchar(20),
  password varchar(20),
  mobile varchar(20)
);  
  
insert into user (uuid, userName,password,mobile) values('a', 'User1','aaa','13678');
insert into user (uuid, userName,password,mobile) values('aa', 'User2','aaa','13678');
insert into user (uuid, userName,password,mobile) values('aaa', 'User3','aaa','13678');
insert into user (uuid, userName,password,mobile) values('aaaa', 'User4','aaa','13678');
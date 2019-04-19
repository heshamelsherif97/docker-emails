create database emails;
use emails;
create sequence emailID START 1 INCREMENT 1;
create sequence threadID START 1 INCREMENT 1;
create sequence attachmentID START 1 INCREMENT 1;

create table folder (name STRING primary key);

create table email (ID int primary key DEFAULT nextval('emailID'),
                      timest timestamp,
                      sender STRING,
                      subject STRING,
                      body STRING,
                      thread_id int  DEFAULT nextval('threadID'),
                      type STRING,
                      folder STRING,
                      foreign key (folder) references folder(name) ON DELETE NO ACTION ON UPDATE CASCADE  
                   );


create table recipient (
                  email_id int,
                  recipient_email STRING,
                  primary key(email_id, recipient_email),
                  foreign key (email_id) references email(ID) ON DELETE CASCADE ON UPDATE CASCADE  
                );

create table bcc (
                  email_id int ,
                  bcc_email STRING,
                  primary key(email_id, bcc_email),
                  foreign key (email_id) references email(ID) ON DELETE CASCADE ON UPDATE CASCADE  
                );

create table cc (
                  email_id int ,
                  cc_email STRING,
                  primary key(email_id, cc_email),
                  foreign key (email_id) references email(ID) ON DELETE CASCADE ON UPDATE CASCADE  
                );

create table attachment (
                         ID int primary key DEFAULT nextval('attachmentID'),
                         email_id int,
                         attachment_address STRING,
                         foreign key (email_id) references email(ID) ON DELETE CASCADE ON UPDATE CASCADE  
                        );


INSERT INTO folder(name)
VALUES ('Scalable');

INSERT INTO folder(name)
VALUES ('Work');

INSERT INTO folder(name)
VALUES ('Default');

/* Email Table Entries */
INSERT INTO email(timest,sender,subject,body,type,folder)
VALUES ('2019-01-19 03:14:07','habiiba.elghamry@gmail.com','Scalable Milestone','This is the submission link','sent','Scalable');

INSERT INTO email(timest,sender,subject,body,thread_id,type,folder)
VALUES ('2019-01-19 05:20:17','yasmineabdulfattah@gmail.com','Scalable Milestone','I submitted',1,'sent','Scalable');

INSERT INTO email(timest,sender,subject,body,type,folder)
VALUES ('2019-02-10 09:24:37','habiiba.elghamry@gmail.com','Draft','This is a draft','drafts','Default');

INSERT INTO email(timest,sender,subject,body,type,folder)
VALUES ('2019-01-23 13:14:15','habiiba.elghamry@gmail.com','CV','This is my CV','sent','Work');

INSERT INTO email(timest,sender,subject,body,type,folder)
VALUES ('2019-02-09 07:34:45','yasmineabdulfattah@gmail.com','','Check the attachments','sent','Default');



/* Recipient Table Entries */

INSERT INTO recipient(email_id,recipient_email)
VALUES (1 ,'yasmineabdulfattah@gmail.com');

INSERT INTO recipient(email_id,recipient_email)
VALUES (2,'habiiba.elghamry@gmail.com');

INSERT INTO recipient(email_id,recipient_email)
VALUES (3,'faridashafik@gmail.com');

INSERT INTO recipient(email_id,recipient_email)
VALUES (4,'habiiba.elghamry@gmail.com');

INSERT INTO recipient(email_id,recipient_email)
VALUES (5,'faridashafik@gmail.com');


/* CC Table Entries */

INSERT INTO cc(email_id,cc_email)
VALUES (1,'faridashafik@gmail.com');

INSERT INTO cc(email_id,cc_email)
VALUES (1,'mariamrizk@gmail.com');

INSERT INTO cc(email_id,cc_email)
VALUES (2,'faridashafik@gmail.com');

INSERT INTO cc(email_id,cc_email)
VALUES (3,'mariamrizk@gmail.com');

INSERT INTO cc(email_id,cc_email)
VALUES (4,'laila96@gmail.com');

/* BCC Table Entries */

INSERT INTO bcc(email_id,bcc_email)
VALUES (5,'imanemohamed@gmail.com');

INSERT INTO bcc(email_id,bcc_email)
VALUES (4,'dana_mohamed@gmail.com');

INSERT INTO bcc(email_id,bcc_email)
VALUES (3,'mariamrizk@gmail.com');

INSERT INTO bcc(email_id,bcc_email)
VALUES (2,'imanemohamed@gmail.com');

INSERT INTO bcc(email_id,bcc_email)
VALUES (1,'mohamed.hesham@gmail.com');

/* Attachment Table Entries */

INSERT INTO attachment(email_id,attachment_address)
VALUES (1,'https://mail.google.com/mail/u/0/#sent/KtbxLzGHhZzPDDkbJfsGfjrhgjbwu');

INSERT INTO attachment(email_id,attachment_address)
VALUES (2,'https://mail.google.com/mail/u/0/#sent/Redhfrtejgyasbdsm');

INSERT INTO attachment(email_id,attachment_address)
VALUES (4,'https://mail.google.com/mail/u/0/#draft/WjrnerfbhjdffjndfksuFtdbhasj');

INSERT INTO attachment(email_id,attachment_address)
VALUES (4,'https://mail.google.com/mail/u/1/#sent/RhewjyfbdsmFTwtsadwed');

INSERT INTO attachment(email_id,attachment_address)
VALUES (5,'https://mail.google.com/mail/u/3/#sent/YndsjhewRmdnbsTnajd');


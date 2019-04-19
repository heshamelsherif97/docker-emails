create database emails;
use emails;
create sequence emailID START 1 INCREMENT 1;
create sequence threadID START 1 INCREMENT 1;
create sequence attachmentID START 1 INCREMENT 1;


create table email (ID int primary key DEFAULT nextval('emailID'),
                      timest timestamp,
                      sender STRING,
                      subject STRING,
                      body STRING,
                      thread_id int  DEFAULT nextval('threadID'),
                      type STRING
                   );


create table recipient (
                  email_id int,
                  recipient_email STRING,
		  deleted STRING DEFAULT 'false',
		  folder STRING,
		  spam STRING DEFAULT 'false',
                  primary key(email_id, recipient_email),
                  foreign key (email_id) references email(ID) ON DELETE CASCADE ON UPDATE CASCADE
                );

create table bcc (
                  email_id int ,
                  bcc_email STRING,
		  deleted STRING DEFAULT 'false',
		  folder STRING,
	          spam STRING DEFAULT 'false',
                  primary key(email_id, bcc_email),
                  foreign key (email_id) references email(ID) ON DELETE CASCADE ON UPDATE CASCADE
                );

create table cc (
                  email_id int ,
                  cc_email STRING,
		  deleted STRING DEFAULT 'false',
		  folder STRING,
		  spam STRING DEFAULT 'false',
                  primary key(email_id, cc_email),
                  foreign key (email_id) references email(ID) ON DELETE CASCADE ON UPDATE CASCADE
                );

create table attachment (
                         ID int primary key DEFAULT nextval('attachmentID'),
                         email_id int,
                         attachment_address STRING,
                         foreign key (email_id) references email(ID) ON DELETE CASCADE ON UPDATE CASCADE
                        );


/* Email Table Entries */
INSERT INTO email(timest,sender,subject,body,type)
VALUES ('2019-01-19 03:14:07','habiiba.elghamry@gmail.com','Scalable Milestone','This is the submission link','sent');

INSERT INTO email(timest,sender,subject,body,thread_id,type)
VALUES ('2019-01-19 05:20:17','yasmineabdulfattah@gmail.com','Scalable Milestone','I submitted',1,'sent');

INSERT INTO email(timest,sender,subject,body,type)
VALUES ('2019-02-10 09:24:37','habiiba.elghamry@gmail.com','Draft','This is a draft','draft');

INSERT INTO email(timest,sender,subject,body,type)
VALUES ('2019-01-23 13:14:15','habiiba.elghamry@gmail.com','CV','This is my CV','sent');

INSERT INTO email(timest,sender,subject,body,type)
VALUES ('2019-02-09 07:34:45','yasmineabdulfattah@gmail.com','','Check the attachments','sent');



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

GRANT ALL ON emails.email TO testuser;
GRANT ALL ON emails.recipient TO testuser;
GRANT ALL ON emails.bcc TO testuser;
GRANT ALL ON emails.cc TO testuser;
GRANT ALL ON emails.attachment TO testuser;

GRANT UPDATE,INSERT ON TABLE emailID TO testuser;
GRANT UPDATE,INSERT ON TABLE attachmentID TO testuser;
GRANT UPDATE,INSERT ON TABLE threadID TO testuser;
create sequence ruleID START 1 INCREMENT 1;

create table rule (ID int primary key DEFAULT nextval('ruleID'),
                      sent_to STRING,
                      sent_from STRING,
                      subject STRING,
                      words_in STRING,
		      owner STRING,
                      actionName STRING,
		      field1 STRING,
		      field2 STRING
		     );

GRANT ALL ON emails.rule TO testuser;

GRANT UPDATE,INSERT ON TABLE emails.ruleID TO testuser;

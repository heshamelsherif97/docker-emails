use emails;

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

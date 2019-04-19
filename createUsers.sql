-- Creating Database 
create database scalable;

-- Table creation
create Table users(firstName Varchar(50),lastName varchar(50),email varchar(50) unique,username varchar(50) unique, gender bool, birthdate Date, userPassword Varchar(200),
userId  serial primary key );


-- Creating Procedures

-- Inserting a User
create Procedure insertuser(_firstName Varchar(50),_lastName varchar(50),_email varchar(50),_username varchar(50), _gender bool,
_birthdate Date, _password Varchar(200))
Language SQL
as $$
Insert into users (firstName,lastName,email,username,gender,birthDate,userPassword)
values(_firstName,_lastName,_email,_username,_gender,_birthdate,_password)				
$$;
		
-- Get USER
create Procedure getuser(_username varchar(50))
Language SQL
as $$
select * from public.users
where
_username=username
$$;		 
-- Updating password							
create Procedure updatepassword( _password Varchar(200),_username varchar(50),_oldPassword varchar(200))
Language SQL
as $$
update  users
set userPassword=_password
where
_username=username and _oldPassword=userPassword
$$;		
																								  
--Update First Name																								  
create Procedure updatefirstname( _newFirstname Varchar(50),_username varchar(50))
Language SQL
as $$
update  users
set firstName=_newFirstname
where
_username=username 
$$;		
																			  
--Update Second Name																			  
create Procedure updatelastname( _newLastname Varchar(50),_username varchar(50))
Language SQL
as $$
update  users
set lastName=_newLastname
where
_username=username 
$$;
		
--Update Delete User																			
create Procedure deleteUser(_username varchar(50))
Language SQL
as $$
delete from  users
where
_username=username 
$$;		
											  
--Testing and Inserting Dummy Data							  
call InsertUser('hesham','sherif','hesho@gmail.com','hesho96','t','1990-11-12','siiiuuu');
call InsertUser('Mohamed','Medhat','medhat@gmail.com','medhat96','t','1996-04-19','mido');
call InsertUser('Karim','Walid','wello@gmail.com','walid96','t','1994-02-12','wello');								   
call InsertUser('Ali','Hany','loloelmaten@gmail.com','lolo96','t','1995-01-15','loloelgamed');
call InsertUser('Laila','Samir','LolaSoso@gmali.com','lolososo','f','1993-02-12','lolaelamar');
call InsertUser('Khalid','Youssef','laleldostor@gmailcom','yafde7ty','t','1990-02-12','khalod');
		
											  
											  
											  
					
					





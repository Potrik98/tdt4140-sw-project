CREATE TABLE Users (
	PRIMARY KEY PersonID int,
	Name varchar(255),
	Gender varchar(255),
	Password varchar(255),
	PhoneNum int
	);

CREATE TABLE ServiceProvider (
	PRIMARY KEY PersonID int,
	Name varchar(255),
	Password varchar(255),
	PhoneNum int
	Service varchar(255)
	);

CREATE TABLE AccessTo (
	FOREIGN KEY UserID int REFERENCES Users(PersonID),
	FOREIGN KEY ServiceProviderID REFERENCES ServiceProvider(PersonID)
	);

CREATE TABLE Data(
	FOREIGN KEY PersonID REFERENCES Users(PersonID),
	Steps int,
	Pulse int,
	Temperature int,
	Time Timestamp
);
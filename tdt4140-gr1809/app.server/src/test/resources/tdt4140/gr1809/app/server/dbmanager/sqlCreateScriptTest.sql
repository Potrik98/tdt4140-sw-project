CREATE TABLE Users(
  userId varchar(36) NOT NULL PRIMARY KEY,
  firstName varchar(255),
  lastName varchar(255),
  gender varchar(255),
  birthDate TIMESTAMP,
  deleted integer DEFAULT 0
);
CREATE TABLE DataPoints(
  dataId varchar(36) NOT NULL PRIMARY KEY,
  userId varchar(36),
  dataValue integer,
  dataType varchar(30),
  dataTime TIMESTAMP,
  FOREIGN KEY (userId) REFERENCES Users(userId)
);
CREATE TABLE Users(
  userId varchar(36) NOT NULL PRIMARY KEY,
  firstName varchar(255),
  lastName varchar(255),
  gender varchar(255),
  maxPulse integer,
  birthDate TIMESTAMP,
  participatingInAggregatedStatistics BOOLEAN,
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

CREATE TABLE Notifications(
  notificationId varchar(36) NOT NULL PRIMARY KEY,
  userId varchar(36),
  message varchar(255),
  time TIMESTAMP,
  FOREIGN KEY (userID) REFERENCES Users(userId)
);

CREATE TABLE TimeFilters(
  filterId varchar(36) NOT NULL PRIMARY KEY,
  userId varchar(36),
  startTime TIMESTAMP,
  endTime TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
  dataType varchar(30),
  FOREIGN KEY (userId) REFERENCES Users(userId)
);

CREATE TABLE ServiceProviders(
  serviceProviderId varchar(36) NOT NULL PRIMARY KEY,
  firstName varchar(255),
  lastName varchar(255),
  gender varchar(255),
  birthDate TIMESTAMP,
  deleted integer DEFAULT 0
);

CREATE TABLE ServiceProviderAccessToUser(
  userId varchar(36),
  serviceProviderId varchar(36),
  FOREIGN KEY (userId)  REFERENCES Users(userId),
  FOREIGN KEY (serviceProviderId) REFERENCES ServiceProviders(serviceProviderId),
  PRIMARY KEY (userId, serviceProviderId)
);

CREATE TABLE CustomNotificationThresholds(
  thresholdId varchar(36) NOT NULL PRIMARY KEY,
  userId varchar(36),
  dataType varchar(30),
  thresholdType varchar(30),
  thresholdValue integer,
  FOREIGN KEY (userId) REFERENCES Users(userId)
);

# Server documentation

The server module has 3 packages:
 - dbmanger: contains DBManagers which communicate with the db and handles storing of all model objects.
 - resource: contains Resources which serves the REST-api endpoints, interprets requests and communicates with the db managers.
 - module: contains server modules for different logic-related tasks, such as filtering, analyzing or generating statistics.

<h3>Testing:</h3>
The server module has 3 types of tests:
 - DBManagerTest: creates a new, empty in-memory test instance of a h2 database, 
 tests opening db connections,
 and the functionality of each db manager.
 - Module tests: tests the logic components of each logic module.
 - IntegrationTest: tests the entire server stack from client to db manager. 
 A local host instance of the server is started, with a blank in memory test instance of the database.
 The client is then opening a webtarget connection to the server, 
 communication between the client and server over HTTP to the servers REST api is tested,
 together with serialization of user objects.
 
<h3>Settings</h3>
Properties files are located under the resources root.
 - dbconnection.properties: contains the connection info for the database.

<h3>Endpoints overview:</h3>
All data is transferred using JSON, and Jackson is used for serialization.

 - `POST    /user {User object}`: creates an user of the user object.
 - `GET     /user/{userId}`: gets the user with the userId.
 - `GET     /user/{userId}/data`: gets the user object with all related data.
 - `POST    /user/{userId} {User object}`: updates the user with the data in the user object.
 - `DELETE  /user/{userId}`: deletes the user with the userId.
 - `GET     /user/{userId}/datapoints`: gets a list of data points of the user's data points.
 - `GET     /user/{userId}/notifications`: gets a list of notifications of the user's notifications.
 - `GET     /user/{userId}/timefilters`: gets a list of time filters of the user's time filters.
 - `GET     /user/{userId}/customnotificationtresholds`: gets a list of the user's custom notification thresholds.
 - `GET     /user/{userId}/serviceproviders`: gets a list of service providers of the service providers with access to the user.
 - `POST    /user/{userId}/serviceproviders/{serviceProviderId}`: adds the service provider with the serviceProviderId to the list of service providers with access.
 - `DELETE  /user/{userId}/serviceproviders/{serviceProviderId}`: deletes the service provider with the serviceProviderId from the list of service providers with access.
 - <br>
 - `POST    /datapoints/ {DataPoint object}`: creates a data point from the data point object.
 - <br>
 - `POST    /timefilters/ {TimeFilter object}`: creates a time filter from the time filter object.
 - `DELETE  /timefilters/{timeFilterId}`: deletes the time filter with the timeFilterId.
 - <br>
 - `POST    /serviceprovider/ {ServiceProvider object}`: creates a service provider from the service provider object.
 - `GET     /serviceprovider/{serviceProviderId}`: gets the service provider with the serviceProviderId.
 - `POST    /serviceprovider/{serviceProviderId} {ServiceProvider object}`: updates the service provider with the data in the service provider object.
 - `DELETE  /serviceprovider/{serviceProviderId}`: deletes the service provider with the serviceProviderId.
 - `GET     /serviceprovider/{serviceProviderId}/users`: gets a list of users of the users the service provider has access to.
 - <br>
 - `POST    /notifications {Notification object}`: creates a notification from the notification object.
 - `DELETE  /notifications {notificationId}`: deletes the notification with the notificationId.
 - <br>
 - `GET     /statistics/{dataType}`: gets the statistics for the data type.
 - <br>
 - `POST    /customnotificationthresholds {CustomNotificationThreshold object}`: create a custom notification threshold from the given object.
 - `DELETE  /customnotificationthresholds/{thresholdId}`: deletes the custom notification threshold with the thresholdId.

<h3>Class overview:</h3>
 - `Server`: contains the server main method and routing table
 - dbmanger
   - `DBManager`: abstract class handling the properties of and the connection to the db.
   - `NamedParameterStatement`: wrapper for prepared statement that instead of using indexes uses names to identify the variables of the query.
   - `UserDBManager`: handles storing users in the db.
   - `DataDBManager`: handles storing data points in the db.
   - `ServiceProviderDBManager`: handles storing service providers in the db.
   - `AccessDBManager`: handles storing access priviliges of a service provider to a user.
   - `NotificationDBManager`: handles storing notifications in the db.
   - `TimeFilterDBManager`: handles storing time filters in the db.
   - `CustomNotificationThresholdDBManager`: handles storing custom notification threshold in the db.
 - resource
   - `AccessResource`: serves endpoints related to access.
   - `DataResource`: serves endpoints related to data points.
   - `NotificationResource`: serves endpoints related to notifications.
   - `ServiceProviderResource`: serves endpoints related to service providers.
   - `StatisticsResource`: serves endpoints related to statistics.
   - `TimeFilterResource`: serves endpoints related to time filters.
   - `UserResource`: serves endpoints related to users.
 - module
   - `Filter`: filteres datapoints against time filters in the database.
   - `Analyzer`: analyzes datapoints and generates notifications.
   - `Statistics`: generates statistics based on datapoints in the database.
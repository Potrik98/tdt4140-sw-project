# Server documentation

The server module has 3 packages:
 - dbmanger: contains DBManagers which communicate with the db and handles storing of all model objects.
 - resource: contains Resources which serves the REST-api endpoints, interperets requests and communicates with the db managers.
 - module: contains server modules for different logic-related tasks, such as filtering, analyzing or generating statistics.

 <h3>Class overview:</h3>
 - Server: contains the server main method and routing table
 - dbmanger
   - DBManager: abstract class handling the properties of and the connection to the db.
   - NamedParameterStatement: wrapper for prepared statement that instead of using indexes uses names to identify the variables of the query.
   - UserDBManager: handles storing users in the db.
   - DataDBManager: handles storing data points in the db.
   - ServiceProviderDBManager: handles storing service providers in the db.
   - AccessDBManager: handles storing access priviliges of a service provider to a user.
   - NotificationDBManager: handles storing notifications in the db.
   - TimeFilterDBManager: handles storing time filters in the db.
   - CustomNotificationThresholdDBManager: handles storing custom notification threshold in the db.
 - resource
   - AccessResource: serves endpoints related to access.
   - DataResource: serves endpoints related to data points.
   - NotificationResource: serves endpoints related to notifications.
   - ServiceProviderResource: serves endpoints related to service providers.
   - StatisticsResource: serves endpoints related to statistics.
   - TimeFilterResource: serves endpoints related to time filters.
   - UserResource: serves endpoints related to users.
 - module
   - Filter: filteres datapoints against time filters in the database.
   - Analyzer: analyzes datapoints and generates notifications.
   - Statistics: generates statistics based on datapoints in the database.
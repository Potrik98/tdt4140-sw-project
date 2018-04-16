# Sources for the tests of the server Java code 

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

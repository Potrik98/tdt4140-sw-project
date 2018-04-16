#Client documentation

The client module contains clients for each data model class,
to interface with the servers REST api.
The clients are meant to be used by other applications or modules
to easily communicate with the server backend.
All client methods may throw a ClientException - a Runtime Exception -
meaning that something went wrong while communicating with the server.
The program may choose or choose not to handle any exceptions thrown.
All clients extend the abstract class BasicClient,
which handles the webtarget connection to the server.

<h3>Properties</h3>

Properties are located under the resources root.
 - `client.properties`: contains properties for the client about what server to connect to.

<h3>Class overview</h3>

[Class diagram](diagram.png)

 - `BasicClient`: abstract class handling the loading of client properties and webtarget connections.

<h3>Testing</h3>

Client code are tested in the integration tests.
See [Server test documentation](../../app.server/src/test/java/tdt4140/gr1809/app/server/README.md)

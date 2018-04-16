#Datagen documentation

The Datagen module is a module used for generating random data points,
replacing a sensor ship. The module is primarily used for generating test data.

<h3>Class overview</h3>

[Class diagram](diagram.png)

 - `Generator`: Class generating random data points of a specific data type, for a given user.
 - `DataGenerator`: Runnable command line application used to interface with the Generator,
 generating data points and using the client to create the data points in the system.

<h3>Resources<h3>

The Generator properties are stored in the generator.properties file under resources.
The data points are generated from a normal distribution
with the parameters given in the properties.

<h3>Testing</h3>
Testing of the datagen module is done as a local integration test,
using a client connecting to a local database.
This is achieved by injecting specific test properties for the server and client,
found under test resources.

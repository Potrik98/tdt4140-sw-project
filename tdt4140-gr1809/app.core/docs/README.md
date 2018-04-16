# Core documentation

The core module has 3 packages:
 - model: contains model classes for the different types of data objects the program handles.
 - util: contains a set of useful utilities.
 - value: contains classes for enumeration of types, used for converting between a type and a numeric value.

<h3>Class overview</h3>

[Class diagram](diagram.png)

 - model
   - `DataPoint.DataType`: enum of different valid data types.
   Each data type has a duration;
   how often this type of data is measured.
   - `CustomNotificationThreshold`: a threshold with a value, data type,
   message and a user this threshold is for.
   If the user measures a data of the right type exceeding the threshold,
   a notification is generated with the given message.
   - `DataPoint`: a measured data point of a value, time, data type,
   and a user this measurement is related to.
   - `Notification`: a notification containing a message and time,
   and a user this notification is for.
   - `ServiceProvider`: a class containing the profile data of a service provider.
   - `Time Filter`: a time filter contains start and end times, and the data type to filter,
   and the user this filter is for.
   All incoming data points in the time interval of the data type are filtered.
   - `User`: a class containing the profile data of a user.
 - util
   - `NumberUtils`: contains helper methods useful when working with numbers
   - `StreamUtils`: contains helper methods useful when working with streams
   - `TimeUtils` : contains helper methods useful when working with time
 - value
   - `NumberConverter`: generic interface used to convert between a type and a numeric value.
   - `LocalDateTimeNumberConverter`: implementation of the NumberConverter interface for LocalDateTime values.
   - `Numerable`: An generic extension of the Number class to enumerate any type.
    A NumberConverter for the right type must be provided when creating a Numerable.

<h3>Testing</h3>
The model tests test building and serialization of the model classes.<br>
The util and value tests test logic and functionality.

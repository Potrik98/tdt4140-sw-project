#UI documentation

The UI module contains all source for the front end graphical user interface for the program.

The ui module has 4 packages:
 - graph: contains classes related to graphical visualization of data.
 - io: contains classes related to (disk) input and output.
 - login: contains classes related to handling of the log in of users and service providers.
 - view: contains view controllers for the different views of the ui.

<h3>Class overview<h3>

[Class diagram](diagram.png)

 - graph
   - `DataGraph`: class visualizing data points in a 2d graph.
   - `NumerableAxis`: an extension of ValueAxis for representing numerable values on a graph axis.
   - `LocalDateTimeAxis`: a specialization of the NumerableAxis for LocalDateTime.
   This includes special date formatting and other features.
 - io
   - `FileUtils`: contains useful methods for file input and output.
 - view: contains view controllers for the different views of the program.
 - `FXApp` and `FXAppController`: controls the main application.

<h3>Resources</h3>
The resources folder contains fxml implementations of the views of the application,
and other resources such as images or stylesheets.

<h3>Testing</h3>
Testing of the UI is done using test subjects.

mvn clean compile -f "../pom.xml"
mvn exec:java -f "../pom.xml" -Dexec.mainClass="tdt4140.gr1809.app.server.Server"
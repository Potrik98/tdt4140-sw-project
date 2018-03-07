mvn clean compile -f "../../pom.xml" -Denvironment=server
mvn exec:java -f ../pom.xml -Denvironment=server -Dexec.mainClass=tdt4140.gr1809.app.server.Server

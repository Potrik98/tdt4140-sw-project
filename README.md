# TDT4140 Prosjekt gruppe 9
Sette opp prosjektet:\
`mvn clean install -f tdt4140-gr1809/pom.xml`

--------------------------
Kjøre server:\
`mvn exec:java -f tdt4140-gr1809/app.server/pom.xml -Dexec.mainClass=tdt4140.gr1809.app.server.Server`

Serveren kobler seg til databasen med instillingene satt i dbconnection.properties under server resources.

-------------------------------------
Kjøre bruker-applikasjonen:\
`mvn exec:java -f tdt4140-gr1809/app.ui/pom.xml -Dexec.mainClass=tdt4140.gr1809.app.ui.FxApp`

Bruker-applikasjonen kobler seg automatisk til serveren som er definert i client.properties under client resources.


# TDT4140 Prosjekt gruppe 9
<h3>Prosjektbeskrivelse</h3>

Dette programmet er skrevet og utviklet som en del av faget TDT4140 på NTNU. Programmet er utviklet ved bruk av prosjektmetodikken SCRUM.

<h3>Produktbeskrivelse</h3>

Prosjektet har gått ut på å utvikle et program som samler inn data om helsetilstanden til personer. Data skal samles kontinuerlig fra en sensor implantert i datagiverens kropp. Hensikten med programmet er at datagiveren skal kunne benytte seg av innsamlet data til å enkelt kunne vurdere egen helse i samråd med lege.

Gjennom et enkelt brukergrensesnitt vil datagiver selv kunne få tilgang til en grafisk fremstilling av egen data. Det vil være mulig å filtrere og velge hvilke data som lagres til enhver tid. Brukeren vil kunne supplementere med personlig tilleggsinformasjon for å utvide programmets funksjonalitet.

Programmet vil gjøre det mulig å sammenligne egen data i sanntid med aggregerte data fra andre brukere for å vurdere om man ligger innenfor trygge grenseverdier. Ved tilfeller som faller utenfor normalverdiene vil programmet gi et varsel om at man bør ta kontakt med lege for videre utredning. Målet er at mer eller mindre alvorlige helsetilstander kan avdekkes tidlig og at man dermed kan iverksette tiltak for å minske konsekvensene av dem.

For eksempel vil en pasient som tidligere har målt høye verdier for blodtrykk kunne sette på automatisk varsling til lege og seg selv via programmet for å varsle om dette skulle oppstå igjen. Det vil også være mulig å avdekke trender i data samlet over tid og dermed bli klar over forverret helse på et tidlig tidspunkt.

Datagiveren skal til enhver tid kunne velge å dele data med ulike tjenesteytere, i hovedsak leger og annet helsepersonell, og kontrollere hvilke innsyn disse skal ha. Tjenesteyterne vil kunne benytte seg av innsamlet data for å evaluere og veilede datagiveren, samt motta og gi varslinger ved akutt forverring av helsetilstanden til enkeltpersoner.

Ved å benytte programmet vil det være mulig å fange opp helseproblemer på et tidlig stadium og i samråd med lege kunne iverksette tiltak for å spare både seg selv og samfunnet for utgifter og påkjenningen som kunne oppstått om problemene ikke hadde vært mulig å detektere.

<h3>Komme igang</h3>

Når du åpner opp programmet får du opp en oppstartsmeny der du får muligheten til å velge mellom å logge inn på en allerede eksisterende bruker, opprette en bruker eller logge inn som lege (Serviceprovider).

Etter at du har logget inn har du muligheten til å få en grafisk framstilling av dataverdier lagret om deg.

<h3>Tekniske forutsetninger</h3>

Programmet krever maven og java 8 jdk med java fx for å kompilere.

<h3>Tekniske instillinger</h3>

Tekniske instillinger finnes i .properties-filer under resources.<br>
Serveren vil koble seg til databasen med instillingene satt i
dbconnection.properties under server resources.<br>
Bruker-applikasjonen kobler seg automatisk til serveren som er definert i
client.properties under client resources.

<h3>Installering og kjøring<h3>

Sette opp prosjektet:<br>
`mvn clean install -f tdt4140-gr1809/pom.xml`

Kjøre server:<br>
`mvn exec:java -f tdt4140-gr1809/app.server/pom.xml -Dexec.mainClass=tdt4140.gr1809.app.server.Server`

Kjøre bruker-applikasjonen:<br>
`mvn exec:java -f tdt4140-gr1809/app.ui/pom.xml -Dexec.mainClass=tdt4140.gr1809.app.ui.FxApp`

Kjøre datagenerator:<br>
`mvn exec:java -f tdt4140-gr1809/app.datagen/pom.xml -Dexec.mainClass=tdt4140.gr1809.app.datagen.DataGenerator -Dexec.args="DataType UserId Count"`<br>
Hvor argumentene er
 - DataType : Hvilken type data som skal genereres
 - UserId : For hvilken bruker målingene skal tilknyttes
 - Count : Antall målinger som skal genereres

Kjøre tester:<br>
`mvn clean verify -f tdt4140-gr1809/pom.xml`


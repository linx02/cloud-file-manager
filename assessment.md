# Reflektion

## Sammanfattning & Åsikter

Uppgiften har gett mig kunskaper i AWS plattform, tjänster och utvecklarverktyg samt fördjupat mina kunskaper i Java. Jag har jobbat med NoSQL vilket är nytt. Uppgiften gick mestadels smidigt och var lärorik men med några problem som alltid.

**Vad kunde jag gjort annorlunda?**

Till en början skrev jag "Mock-klasser" som fakeade AWS-tjänsterna under tiden jag utvecklade resten för att inte bränna pengar i onödan, men detta ledde i verkligheten till mycket onödig omfaktorering och längre utvecklingstid. Det finns verktyg för att köra AWS-tjänster lokalt vilka jag skulle använt istället.

## Problem

Jag fick till en början inte till sökfunktionen med DynamoDB. Detta insåg jag senare berodde på att taggarna sparas som en sträng istället för separata värden vilket var tanken. Grunden till problemet var att jag missat splittra strängen med taggar som anges av användaren. Jag löste det genom att ändra:
```java
@CommandLine.Option(names = {"-t", "--tags"}, required = true, description = "Tags")
```
till:
```java
@CommandLine.Option(names = {"-t", "--tags"}, required = true, split = ",", description = "Comma-separated tags")
```
Jag stötte på problem vid nedladdning av filer med följande felmeddelande:
```bash
Error downloading file: Unable to unmarshall response (Failed to read response into file: /Users/linuselvius/Documents/skola/cloud-file-manager). Response Code: 200, Response Text: OK (SDK Attempt Count: 1)
```
Detta berodde på att jag glömt lägga till filnamnet på sökvägen, jag ändrade:
```java
Paths.get(destinationPath));
```
till:
```java
Paths.get(destinationPath, fileName));
```

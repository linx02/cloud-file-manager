# Cloud File Manager
CLI Interface för att använda AWS som en form av personlig cloud-lösning där filer kan delas upp och sparas i "projekt".

## Användarmanual

### Kommandon:
- create-project
- list
- upload
- download
- search

### Flaggor
- --project eller -p
- --tags eller -t
- --file eller -f

### Skapa ett projekt
```bash
java -jar target/cloud-file-manager-1.0-SNAPSHOT.jar create-project -p mitt-projekt
```
### Lista filer i ett projekt
```bash
java -jar target/cloud-file-manager-1.0-SNAPSHOT.jar list -p mitt-projekt
```
### Ladda upp en fil
```bash
java -jar target/cloud-file-manager-1.0-SNAPSHOT.jar upload -f min-fil.zip -p mitt-projekt -t viktigt,2025
```
### Ladda ner en fil
```bash
java -jar target/cloud-file-manager-1.0-SNAPSHOT.jar download -p mitt-projekt -f min-fil.zip
```
### Sök efter en fil

#### Med taggar
```bash
java -jar target/cloud-file-manager-1.0-SNAPSHOT.jar search -t hej
```

#### Med filnamn
```bash
java -jar target/cloud-file-manager-1.0-SNAPSHOT.jar search -f min-fil.zip
```

## Kör lokalt
*Förutsätter att man satt upp AWS-tjänsterna*

1. Ladda ner koden
```bash
git clone https://github.com/linx02/cloud-file-manager.git
cd cloud-file-manager
```
2. Skapa miljövariabler
```bash
echo "DB_URL=jdbc:mysql://<endpoint>:3306/<db>
DB_USER=<db username>
DB_PASSWORD=<db password>
AWS_ACCESS_KEY=<aws access key>
AWS_SECRET_KEY=<aws secret key>
AWS_REGION=<aws region>
AWS_BUCKET_NAME=<aws bucket name>
DYNAMODB_TABLE_NAME=<dynamodb table name>" > .env
```
3. Bygg koden
```bash
mvn clean package
```
4. Kör
```bash
java -jar target/cloud-file-manager-1.0-SNAPSHOT.jar --help
```

## Databasarkitektur

**table projects**
- **(PK)id int**
- project_name string
- created_at timestamp

**table files**
- **(PK)id int**
- *(FK) project_id int*
- file_name string
- s3_url string
- uploaded_at timestamp

## Designbeslut

Projektet är uppdelat i 2 delar. **cli/** mappen som huserar klasser för alla kommandon och därmed ansvarar för interfacet som slutanvändaren serveras, samt anrop till service-lagret som ligger i **service/** mappen.

- CLI interfacet är byggt med PicoCLI, service-lagret med Amazons SDK.
- För att utnyttja DynamoDB's möjlighet till snabba sökningar använder sökfunktionen först DynamoDB, och faller tillbaks på vanliga databasen om filen inte hittas.


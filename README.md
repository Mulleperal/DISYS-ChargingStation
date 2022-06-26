# DISYS-ChargingStation

## Start the Distributed Services:
### Folder Structure
```
DISYS-ChargingStation
├───ActiveMQ
│   └───apache-activemq-5.17.1
├───Database
├───DatraCollectionDispatcher
├───JavaFX
├───PDFGenerator
│   └───invoices
└───Worker
    ├───DataCollectionReciever
    └───StationDataCollector
```


### JavaFX
    Open ./JavaFX in IntelliJ
    Run "mvn javafx:run"

Prerequisites:
- JDK 17 or above installed  
- Optional: JavaFX SDK 17 or above installed

Attention: when recently installed an SDK, reboot your machine in order to make JavaFX work under IntelliJ


### Spring Boot

    `Open ./SpringBoot in IntelliJ
    Run "mvn spring-boot:run"`


### Start Workers
#### Worker StationDataCollector

    Open ./Worker/StationDataCollector in IntelliJ
    Run Main

#### Worker DataCollectionReciever

    Open ./Worker/DataCollectionReciever in IntelliJ
    Run Main

#### Worker PDFGenerator

    Open ./PDFGenerator in IntelliJ
    Run Main

### Database

Prerequisite: For starting docker service: ```sudo service docker start```
> docker run --rm --detach --name distorder -e POSTGRES_USER=distuser -e POSTGRES_PASSWORD=distpw -v data:/var/lib/postgresql/data -p 5432:5432 postgres

> docker exec -it distorder bash

>$ psql -U distuser

>$ CREATE DATABASE dist;

>$ GRANT ALL PRIVILEGES ON DATABASE dist TO distuser;

>$ \c dist

>$ paste sql.init contents
    

### Queue

> .\Queue\apache-activemq-5.16.5\bin\win64\activemq.bat start

ApacheMQ Queue Management: http://127.0.0.1:8161/admin/queues.jsp or [ApacheMQ Queue Management](http://127.0.0.1:8161/admin/queues.jsp)


package at.fhtw.disys.DatraCollectionDispatcher.controller;

import at.fhtw.disys.DatraCollectionDispatcher.models.JobInfo;
import at.fhtw.disys.DatraCollectionDispatcher.models.VerbrauchsAnfrage;
import at.fhtw.disys.DatraCollectionDispatcher.queue.communication.Producer;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@RestController
public class InvoiceController {

    private final static String brokerURL = "tcp://localhost:61616";
    private final static String DB_CONNECTION = "jdbc:postgresql://localhost:5432/dist?user=distuser&password=distpw";

    @PostMapping("/invoices/{customer_id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void createInquiry(@PathVariable int customer_id) {

        UUID uniqueKey = UUID.randomUUID();
        String jobID = uniqueKey.toString();

        // Abfrage aller Ladestationen aus der DB
        List<Integer> stationIds = new ArrayList<>();
        try {
            Connection conn = DriverManager.getConnection(DB_CONNECTION);
            String query = "Select station_id from stationlist";
            PreparedStatement preparedStatement = conn.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                stationIds.add(resultSet.getInt(1));
            }
            System.out.println("Alle Ladestationen aus der DB geholt!");
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }

        // Absenden der Messages in die Active MQ für jede Ladestation
        for (int i = 0; i < stationIds.size(); i++) {
            String queueName = "VerbrauchsabfragenQueue";
            VerbrauchsAnfrage verbrauch = new VerbrauchsAnfrage(jobID,customer_id,stationIds.get(i));
            Producer.send(verbrauch.toJson(), queueName, brokerURL);
            System.out.println("Vebrauchsabfrage ".concat(Integer.toString(i).concat(" im MsgBus platziert!")));
        }
        // Absenden der Message in die ActiveMQ für den DataCollectionReciever
        String queueName = "jobInfoQueue";
        JobInfo jobInfo = new JobInfo(jobID,customer_id,stationIds);
        Producer.send(jobInfo.toJson(), queueName, brokerURL);
        System.out.println("JobInfo im MsgBus platziert!");

    }
}

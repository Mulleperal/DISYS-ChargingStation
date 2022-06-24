package at.fhtw.disys.DatraCollectionDispatcher;

import at.fhtw.disys.DatraCollectionDispatcher.models.JobInfo;
import at.fhtw.disys.DatraCollectionDispatcher.queue.communication.Producer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class DatraCollectionDispatcherApplication {

	public static void main(String[] args) {
		SpringApplication.run(DatraCollectionDispatcherApplication.class, args);
	}

	private final static String brokerURL = "tcp://localhost:6616";
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
			conn.close();
		} catch (Exception e) {
			System.out.println(e);
		}

		// Absenden der Messages in die Active MQ für jede Ladestation
		for (int i = 0; i < stationIds.size(); i++) {
			String queueName = "VerbrauchsabfragenQueue";
			JobInfo jobInfo = new JobInfo(jobID,customer_id,stationIds);
			Producer.send(jobInfo.toJson(), queueName, brokerURL);
		}
		// Absenden der Message in die ActiveMQ für den DataCollectionReciever
		String queueName = "jobInfoQueue";
		JobInfo jobInfo = new JobInfo(jobID,customer_id,stationIds);
		Producer.send(jobInfo.toJson(), queueName, brokerURL);

	}

/*
	@GetMapping("/invoices/{customer_id}")
	public void getPdfInvoice(@PathVariable String customer_id) {

		// Check ob PDF schon erstellt wurde
		File f = new File();
		return HttpStatus.NOT_FOUND;

		// Wenn nicht dann
		// Sonst Return Download link and creation time

	}
*/

}

package at.technikum.service;

import at.technikum.models.JobInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.*;
import java.util.HashMap;
import java.util.UUID;

public class JobInfoService extends BaseService {


    private final String id;

    private static final String DB_CONNECTION = "jdbc:postgresql://localhost:5432/dist?user=distuser&password=distpw";


    public JobInfoService(String inDestination, String outDestination, String brokerUrl) {
        super(inDestination, outDestination, brokerUrl);

        this.id = UUID.randomUUID().toString();

        System.out.println("Consumption Worker (" + this.id + ") started...");
    }


    @Override
    protected String executeInternal(String input, String outDestination, String brokerUrl) {
        // input (=jsonString) umwandeln
        JobInfo jobInfo;
        ObjectMapper mapper = new ObjectMapper();
        try {
            jobInfo = mapper.readValue(input, JobInfo.class);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        // Abfrage des Verbrauchs und des Verbrauchszeitpunkt des Kunden X an Station Y aus der DB
        try (Connection conn = connect()) {
            // Insert into Jobtable mit VALUES (jobID, Anzahl angefragter Stationen, Anzahl Rückmeldungen)

            // insert into jobInfo (job_id, station_count, returned_data_count) values (?, ?, 0);
            String sql = "insert into jobInfo (job_id, station_count, returned_data_count) values (?, ?, 0);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, );
            preparedStatement.setInt(2, );
            ResultSet resultSet = preparedStatement.executeQuery();
            HashMap<String, String> consumptions = new HashMap<String, String>();
            while (resultSet.next()) {
                consumptions.put(resultSet.getString(2),Integer.toString(resultSet.getInt(1)));
            }
        } catch (SQLException e) {
            return "error";
        }

        // Rückgabe des JSON-Strings des Objekts JobInfo
        return jobInfo.toJson();
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_CONNECTION);
    }
}

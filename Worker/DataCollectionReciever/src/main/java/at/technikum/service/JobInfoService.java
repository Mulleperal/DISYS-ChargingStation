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

        System.out.println("JobInfo Worker (" + this.id + ") started...");
    }


    @Override
    protected String executeInternal(String input, String outDestination, String brokerUrl) {

        System.out.println("Checkpoint 0 (JobInfoService)");

        // input (=jsonString) umwandeln
        JobInfo jobInfo;
        ObjectMapper mapper = new ObjectMapper();
        try {
            jobInfo = mapper.readValue(input, JobInfo.class);
        } catch (Exception e) {
            System.out.println("Error before Checkpoint 1 (JobInfoService)");
            System.out.println(e);
            throw new RuntimeException(e);
        }

        System.out.println("Checkpoint 1 (JobInfoService)");

        // Abfrage des Verbrauchs und des Verbrauchszeitpunkt des Kunden X an Station Y aus der DB
        try (Connection conn = connect()) {
            String sql = "insert into jobInfo (job_id, station_count, returned_data_count) values (?, ?, 0);";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, jobInfo.getJobId());
            preparedStatement.setInt(2, jobInfo.getStationCount());
            preparedStatement.execute();
        } catch (SQLException e) {
            System.out.println("Error before Checkpoint 2 (JobInfoService)");
            System.out.println(e);
            return "error";
        }
        System.out.println("Checkpoint 2 (JobInfoService)");

        System.out.println("Creation of JobInfo " + jobInfo.getJobId());
        return "DONE";
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_CONNECTION);
    }
}

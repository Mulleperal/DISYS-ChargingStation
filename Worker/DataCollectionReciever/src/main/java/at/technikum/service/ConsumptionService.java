package at.technikum.service;

import at.technikum.communication.Producer;
import at.technikum.models.Customer;
import at.technikum.models.VerbrauchsMeldung;
import at.technikum.models.InvoiceInfo;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.sql.*;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ConsumptionService extends BaseService {

    private final String id;

    private static final String DB_CONNECTION = "jdbc:postgresql://localhost:5432/dist?user=distuser&password=distpw";

    public ConsumptionService(String inDestination, String outDestination, String brokerUrl) {
        super(inDestination, outDestination, brokerUrl);

        this.id = UUID.randomUUID().toString();

        System.out.println("Consumption Worker (" + this.id + ") started...");
    }

    @Override
    protected String executeInternal(String input, String outDestination, String brokerUrl) {

        int stationCount = Integer.MAX_VALUE;
        int meldungsCount = Integer.MIN_VALUE;
        System.out.println("Checkpoint 0 (ConsumptionService)");

        // input (=jsonString) umwandeln
        VerbrauchsMeldung verbrauchsMeldung;
        ObjectMapper mapper = new ObjectMapper();
        try {
            verbrauchsMeldung = mapper.readValue(input, VerbrauchsMeldung.class);
        } catch (Exception e) {
            System.out.println("Error before Checkpoint 1 (ConsumptionService)");
            throw new RuntimeException(e);
        }
        System.out.println("Checkpoint 1 (ConsumptionService)");

        // Eintragen der Verbrauchswerte in Tabelle verbrauchswerte
        HashMap<String, String> verbrauch = verbrauchsMeldung.getVerbrauch();
        for (Map.Entry<String,String> entry : verbrauch.entrySet()) {
            String timestamp = entry.getKey();
            int consumption = Integer.parseInt(entry.getValue());

            try (Connection conn = connect()) {
                // Eintragen aller Werte aus Vebrauchsmeldung.verbrauch -> in Tabelle mit Values (job_id, station_id, datetime, verbrauch)
                String sql = "insert into consumptions (job_id, station_id, consumption, datetime) values (?, ?, ?, ?)";
                PreparedStatement preparedStatement = conn.prepareStatement(sql);
                preparedStatement.setString(1, verbrauchsMeldung.getJobId());
                preparedStatement.setInt(2, verbrauchsMeldung.getStationId());
                preparedStatement.setInt(3, consumption);
                preparedStatement.setTimestamp(4, Timestamp.valueOf(timestamp));
                preparedStatement.execute();
            } catch (SQLException e) {
                System.out.println("Error before Checkpoint 2 (ConsumptionService)");
                return "error";
            }
            System.out.println("Checkpoint 2 (ConsumptionService)");
        }

        // Updaten der JobinfoTabelle, dass eine weitere Station rückgemeldet hat
        try (Connection conn = connect()) {
            // Eintragen aller Werte aus Vebrauchsmeldung.verbrauch -> in Tabelle mit Values (jobId, station_id, datetime, verbrauch)
            String sql = "UPDATE jobinfo set returned_data_count = returned_data_count + 1 WHERE job_id = ? RETURNING returned_data_count, station_count";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, verbrauchsMeldung.getJobId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                stationCount = resultSet.getInt(2);
                meldungsCount = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error before Checkpoint 3 (ConsumptionService)");
            return "error";
        }
        System.out.println("Checkpoint 3 (ConsumptionService)");


        // Exit wenn noch nicht alle Rückmeldungen da sind.
        if (meldungsCount < stationCount) {
            return "";
        }

        HashMap<String, String> consumptions = new HashMap<String, String>();
        Customer customer = new Customer();

        // Verbrauchsdaten abfragen
        try (Connection conn = connect()) {
            String sql = "Select consumption, datetime from consumptions WHERE job_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setString(1, verbrauchsMeldung.getJobId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                consumptions.put(resultSet.getString(2),Integer.toString(resultSet.getInt(1)));
            }
        } catch (SQLException e) {
            System.out.println("Error before Checkpoint 4 (ConsumptionService)");
            return "error";
        }
        System.out.println("Checkpoint 4 (ConsumptionService)");

        // Kundendaten abfragen

        try (Connection conn = connect()) {
            String sql = "SELECT customer_id, name, postalcode, city, street FROM customers where customer_id = ?";
            PreparedStatement preparedStatement = conn.prepareStatement(sql);
            preparedStatement.setInt(1, verbrauchsMeldung.getCustomerId());
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                customer = new Customer(
                        Integer.toString(resultSet.getInt(1)),
                        resultSet.getString(2),
                        Integer.toString(resultSet.getInt(3)),
                        resultSet.getString(4),
                        resultSet.getString(5)
                );
            }
        } catch (SQLException e) {
            System.out.println("Error before Checkpoint 5 (ConsumptionService)");
            return "error";
        }
        System.out.println("Checkpoint 5 (ConsumptionService)");

        InvoiceInfo invoice = new InvoiceInfo(customer,consumptions);

        // Invoice Info absenden
        Producer.send(invoice.toJson(), outDestination, brokerUrl);

        return "";
    }

    private Connection connect() throws SQLException {
        return DriverManager.getConnection(DB_CONNECTION);
    }
}

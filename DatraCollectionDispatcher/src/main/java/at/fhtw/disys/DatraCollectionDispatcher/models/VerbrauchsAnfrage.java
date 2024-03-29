package at.fhtw.disys.DatraCollectionDispatcher.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class VerbrauchsAnfrage {
    private String jobId;
    private int customerId;
    private int stationId;

    public VerbrauchsAnfrage(String jobId, int customerId, int stationId) {
        this.jobId = jobId;
        this.customerId = customerId;
        this.stationId = stationId;
    }

    public String getJobId() {
        return jobId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public int getStationId() {
        return stationId;
    }


    // https://stackoverflow.com/questions/15786129/converting-java-objects-to-json-with-jackson

    public String toJson() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            // convert user object to json string and return it
            return mapper.writeValueAsString(this);
        }
        catch (JsonProcessingException e) {
            // catch various errors
            e.printStackTrace();
        }
        return null;
    }

}

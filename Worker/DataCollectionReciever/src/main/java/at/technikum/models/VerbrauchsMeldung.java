package at.technikum.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

public class VerbrauchsMeldung {
    @JsonProperty("jobId")
    private String jobId;
    @JsonProperty("customerId")
    private int customerId;
    @JsonProperty("stationId")
    private int stationId;
    @JsonProperty("consumption")
    private HashMap<String, String> verbrauch;

    public VerbrauchsMeldung() {}

    public VerbrauchsMeldung(String jobId, int customerId, int stationId, HashMap<String, String> verbrauch) {
        this.jobId = jobId;
        this.customerId = customerId;
        this.stationId = stationId;
        this.verbrauch = verbrauch;
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

    public HashMap<String, String> getVerbrauch() {
        return verbrauch;
    }

}

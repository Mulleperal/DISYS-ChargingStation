package at.technikum.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;
import java.util.List;

public class JobInfo {
    @JsonProperty("jobId")
    private String jobId;
    @JsonProperty("customerId")
    private int customerId;

    @JsonProperty("stationIds")
    private List<Integer> stationIds;

    public JobInfo() {}

    public JobInfo(String jobId, int customerId) {
        this.jobId = jobId;
        this.customerId = customerId;
    }

    public String getJobId() {
        return jobId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public List<Integer> getStationIds() {
        return stationIds;
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

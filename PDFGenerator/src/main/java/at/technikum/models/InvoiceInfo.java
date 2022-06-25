package at.technikum.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.HashMap;

public class InvoiceInfo {
    @JsonProperty("customer")
    private Customer customer;
    @JsonProperty("consumption")
    private HashMap<String, String> consumption;

    public InvoiceInfo() {
    }

    public InvoiceInfo(Customer customer, HashMap<String, String> consumption) {
        this.customer = customer;
        this.consumption = consumption;
    }

    public Customer getCustomer() {
        return customer;
    }

    public void setCustomer(Customer customer) {
        this.customer = customer;
    }

    public HashMap<String, String> getConsumption() {
        return consumption;
    }

    public void setConsumption(HashMap<String, String> consumption) {
        this.consumption = consumption;
    }

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

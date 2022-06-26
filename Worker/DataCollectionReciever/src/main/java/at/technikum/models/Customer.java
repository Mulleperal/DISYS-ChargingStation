package at.technikum.models;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class Customer {
    private String id;
    private String name;
    private String postalCode;
    private String city;
    private String street;

    public Customer() {
    }

    public Customer(String id, String name, String postalCode, String city, String street) {
        this.id = id;
        this.name = name;
        this.postalCode = postalCode;
        this.city = city;
        this.street = street;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

}

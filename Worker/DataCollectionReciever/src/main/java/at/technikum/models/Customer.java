package at.technikum.models;

public class Customer {
    private String name;
    private String postalCode;
    private String city;
    private String street;

    public Customer() {
    }

    public Customer(String name, String postalCode, String city, String street) {
        this.name = name;
        this.postalCode = postalCode;
        this.city = city;
        this.street = street;
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

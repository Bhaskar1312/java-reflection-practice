package fields.data;

public class Company {
    private String name;
    private String city;
    private Address address;

    public Company(String name, String city, Address address) {
        this.name = name;
        this.city = city;
        this.address = address;
    }
}

package dynamicproxy.test;

import java.io.IOException;
import java.util.Date;

public interface DatabaseReader {

    void connectToDatabase();

    @Cacheable
    String readCustomerIdByName(String firstName, String lastName) throws IOException();

    int countRowsInCustomersTable();

    void addCustomer(String id, String firstName, String lastName) throws IOException();

    @Cacheable
    Date readCustomerBirthday(String id);
}

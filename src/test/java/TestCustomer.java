import com.edge.example.database.DatabaseFactory;
import com.edge.example.database.IDatabase;
import com.edge.example.model.Customer;
import org.junit.jupiter.api.Test;

public class TestCustomer {

    private final IDatabase<Customer> customerDatabase = DatabaseFactory.getDatabase("mongo", Customer.class);

    public TestCustomer() throws Exception {
    }

    @Test
    public void testInsertCustomer() {
        // Create a Customer object
        Customer customer = new Customer("John Doe", "asdw@gmail.com", "03123456789");
        customer.setCustomerId("1");

        // Insert the customer object into the database
        customerDatabase.insert(customer);

        // Read the customer object back from the database

        Customer retrievedCustomer = customerDatabase.read(customer.getCustomerId());
        // Assert that the retrieved customer matches the original
        assert retrievedCustomer != null;
        assert retrievedCustomer.getName().equals("John Doe");
    }

    @Test
    public void testUpdateCustomer() {

        // Create and insert a Customer object
        Customer customer = new Customer("Jane Doe", "test@gmail.com", "03123456789");
        customer.setCustomerId("2");
        customerDatabase.insert(customer);

        // Update the customer object
        customer.setEmail("nottest@gmail.com");
        customerDatabase.update(customer);

        // Read the updated customer object
        Customer updatedCustomer = customerDatabase.read(customer.getCustomerId());

        // Assert that the email has been updated
        assert updatedCustomer != null;
        assert updatedCustomer.getEmail().equals("nottest@gmail.com");
    }

    @Test
    public void testDeleteCustomer() {
        // Create and insert a Customer object
        Customer targetCustomer = new Customer();
        targetCustomer.setCustomerId("2");
        customerDatabase.delete(targetCustomer);
        // Read the deleted customer object
    }


}

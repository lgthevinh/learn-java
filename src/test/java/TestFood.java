import com.edge.example.database.DatabaseFactory;
import com.edge.example.database.IDatabase;
import com.edge.example.model.Food;
import org.junit.jupiter.api.Test;


public class TestFood {

    IDatabase<Food> foodDatabase = DatabaseFactory.getDatabase("sqlite", Food.class);

    public TestFood() throws Exception {
    }

    @Test
    public void testInsertFood() {
        // Create a Food object
        Food food = new Food("1", "Pizza", 9.99);

        // Insert the food object into the database
        foodDatabase.insert(food);

        // Read the food object back from the database
        Food retrievedFood = foodDatabase.read(food.getFoodId());

        // Assert that the retrieved food matches the original
        assert retrievedFood != null;
        assert retrievedFood.getName().equals("Pizza");
        assert retrievedFood.getPrice() == 9.99;
    }

    @Test
    public void testUpdateFood() {

        // Create and insert a Food object
        Food food = new Food("2", "Burger", 5.99);
        foodDatabase.insert(food);

        // Update the food object
        food.setPrice(6.99);
        foodDatabase.update(food);

        // Read the updated food object
        Food updatedFood = foodDatabase.read(food.getFoodId());

        // Assert that the price has been updated
        assert updatedFood != null;
        assert updatedFood.getPrice() == 6.99;
    }

    @Test
    public void testDeleteFood() {

        // Create and insert a Food object
        Food food = new Food("3", "Pasta", 7.99);
        foodDatabase.insert(food);

        // Delete the food object
        foodDatabase.delete(food);
    }

}

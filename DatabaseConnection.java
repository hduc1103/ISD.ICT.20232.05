package something;

public class DatabaseConnection {
    //We can use the Singleton design pattern. 
    //This pattern restricts the instantiation of a class to one "single" instance. The database connection class can be designed as a singleton to ensure that only one instance of the class is created and used throughout the application.

    // Private static variable to hold the single instance
    private static DatabaseConnection instance;

    // Private constructor to prevent instantiation
    private DatabaseConnection() {
        // Initialize the database connection
    }

    // Public static method to provide access to the instance
    public static DatabaseConnection getInstance() {
        if (instance == null) {
            synchronized (DatabaseConnection.class) {
                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }
        return instance;
    }

    // Method to connect to the database
    public void connect() {
        System.out.println("Connecting to the database...");
        // Connection logic here
    }

    // Method to disconnect from the database
    public void disconnect() {
        System.out.println("Disconnecting from the database...");
        // Disconnection logic here
    }
}

public class Main {
    public static void main(String[] args) {
        // Get the single instance of DatabaseConnection
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();

        // Call methods on the DatabaseConnection instance
        dbConnection.connect();
        dbConnection.disconnect();
    }
}

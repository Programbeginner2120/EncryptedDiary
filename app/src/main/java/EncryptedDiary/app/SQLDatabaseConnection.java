package EncryptedDiary.app;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLDatabaseConnection {

    private final String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=residentialLivingComplex;" +
            "user=sa;password=sql_server_2019_password";

    public Connection connectToDatabase() {
        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            return connection;
        }
        catch (Exception e) {
            return null;
        }
    }

}

package EncryptedDiary.app;

import java.sql.Connection;
import java.sql.DriverManager;

public class SQLDatabaseConnection {

    private final String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=ENCRYPTED_DIARY_DB;" +
            "user=sa;password=sql_server_2019_password";
    private Connection conn;

    public SQLDatabaseConnection(){
        this.conn = connectToDatabase();
    }

    private Connection connectToDatabase() {
        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            return connection;
        }
        catch (Exception e) {
            return null;
        }
    }

    public Connection getConn() {
        return conn;
    }
}

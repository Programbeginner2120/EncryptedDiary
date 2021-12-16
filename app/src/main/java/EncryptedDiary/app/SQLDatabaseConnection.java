package EncryptedDiary.app;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.*;
import java.sql.SQLException;

public class SQLDatabaseConnection {

    public static void main (String [] args) {

        String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=residentialLivingComplex;user=alternateUser;password=sql_server_2019_password";

        try (Connection connection = DriverManager.getConnection(connectionUrl)) {
            // Code here.
        }
        // Handle any errors that may have occurred.
        catch (Exception e) {
            e.printStackTrace();
        }

    }


}

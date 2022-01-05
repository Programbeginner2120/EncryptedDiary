package EncryptedDiary.app;

import java.sql.*;

public class SQLDatabaseConnection {

    private static final String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=ENCRYPTED_DIARY_DB;" +
            "user=sa;password=Mk16914004006425529";
    private Connection conn = null;

    public static Connection openConnection(){
        Connection conn = null;
        try{
            conn = DriverManager.getConnection(connectionUrl);
        }

        catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        return conn;
    }

    public static void closeConnection(Connection conn){
        try{
            conn.close();
        }

        catch(SQLException ex){
            System.out.println(ex.getMessage());
        }

        conn = null;
    }

}

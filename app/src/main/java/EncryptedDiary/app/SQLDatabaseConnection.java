package EncryptedDiary.app;

import java.sql.*;

public class SQLDatabaseConnection {

    private static final String connectionUrl = "jdbc:sqlserver://localhost:1433;databaseName=ENCRYPTED_DIARY_DB;" +
            "user=sa;password=Mk16914004006425529";
    private static Connection conn = null;

    public static Connection openConnection(){
        if (conn == null){
            try{ conn = DriverManager.getConnection(connectionUrl); }

            catch(SQLException ex){}
        }
        return conn;
    }

    public static void closeConnection(Connection conn){
        try{
            conn.close();
        }

        catch(SQLException ex){}

        conn = null;
    }

}

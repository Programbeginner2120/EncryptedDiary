package EncryptedDiary.app;

import java.sql.*;

public class SQLDatabaseConnection {

    private static final String connectionUrl = "jdbc:mysql://(localhost=33060,user=Matthewkilleen99," +
        "password=Mk16914004006425529)/ENCRYPTED_DIARY_DB";
    private Connection conn = null;

    /**
     * Opens a connection to the SQL Server Database specified by the String connectionUrl
     * @return Connection conn, a Connection to the database
     */
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

    /**
     * Closes a connection to the SQL Server database
     * @param conn - A Connection to the database
     */
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

//class main{
//
//    public static void main(String [] args){
//        Connection conn = SQLDatabaseConnection.openConnection();
//
//        try{
//            System.out.println(conn.isClosed());
//        }
//        catch (Exception ex){
//            ;
//        }
//
//    }
//}

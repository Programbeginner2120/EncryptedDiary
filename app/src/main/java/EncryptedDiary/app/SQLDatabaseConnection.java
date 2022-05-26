package EncryptedDiary.app;

import java.sql.*;

public class SQLDatabaseConnection {

    private static final String connectionUrl = "jdbc:mysql://(localhost=33060,user=Matthewkilleen99," +
        "password=Mk16914004006425529)/ENCRYPTED_DIARY_DB";
    private Connection conn = null;

    public Connection getConn() {
        return this.conn;
    }

    /**
     * Opens a connection to the SQL Server Database specified by the String connectionUrl
     * @return Connection conn, a Connection to the database
     */
    public void openConnection(){
        try{
            this.conn = DriverManager.getConnection(connectionUrl);
        }
        catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
    }

    /**
     * Closes a connection to the SQL Server database
     * @param conn - A Connection to the database
     */
    public void closeConnection(){
        try{
            this.conn.close();
        }

        catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
    }

    public ResultSet executeSQLQuery(String query) {
        try(Statement stmt = conn.createStatement()){
            ResultSet rs = stmt.executeQuery(query);
            System.out.println(rs.getString("username"));
            return rs;
        }
        catch (SQLException sqlEx){
            System.out.println(sqlEx.getMessage());
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        return null;
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

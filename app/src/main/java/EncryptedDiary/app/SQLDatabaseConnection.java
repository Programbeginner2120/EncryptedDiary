//package EncryptedDiary.app;
//
//import java.sql.*;
//import java.util.ArrayList;
//
//public class SQLDatabaseConnection {
//
//    private static final String connectionUrl = "jdbc:mysql://(localhost=33060,user=Matthewkilleen99," +
//        "password=Mk16914004006425529)/ENCRYPTED_DIARY_DB";
//    private Connection conn = null;
//
//    public Connection getConn() {
//        return this.conn;
//    }
//
//    /**
//     * Opens a connection to the SQL Server Database specified by the String connectionUrl
//     * @return Connection conn, a Connection to the database
//     */
//    public void openConnection(){
//        try{
//            this.conn = DriverManager.getConnection(connectionUrl);
//        }
//        catch(SQLException ex){
//            System.out.println(ex.getMessage());
//        }
//    }
//
//    /**
//     * Closes a connection to the SQL Server database
//     */
//    public void closeConnection(){
//        try{
//            this.conn.close();
//        }
//        catch(SQLException ex){
//            System.out.println(ex.getMessage());
//        }
//    }
//
//    /**
//     * Executes a query on the database and returns the results in an array list
//     * @param query - SQL query to be executed in database
//     * @return results - array list of results
//     */
//    public ArrayList<String []> executeSQLQuery(String query) {
//        ArrayList<String []> resultList = new ArrayList<>();
//        Statement stmt = null;
//        ResultSet rs = null;
//        try{
//            stmt = conn.createStatement();
//            rs = stmt.executeQuery(query);
//            ResultSetMetaData rsmd = rs.getMetaData();
//            final int columnCount = rsmd.getColumnCount();
//            while (rs.next()){
//                String [] rowValues = new String[columnCount];
//                for (int i = 1; i <= columnCount; i++){
//                    rowValues[i-1] = rs.getString(i);
//                }
//                resultList.add(rowValues);
//            }
//            return resultList;
//        }
//        catch (SQLException sqlEx){
//            System.out.println(sqlEx.getMessage());
//        }
//        catch (Exception ex){
//            System.out.println(ex.getMessage());
//        }
//        finally {
//            if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
//            if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
//        }
//        return null;
//    }
//
//    public boolean executeSQLUpdate(String query){
//        Statement stmt = null;
//        int numberOfAffectedRows = 0;
//        try{
//            stmt = conn.createStatement();
//            numberOfAffectedRows = stmt.executeUpdate(query); // not used right now but for documentation purposes assigned
//            return true;
//        }
//        catch (SQLException sqlEx){
//            System.out.println(sqlEx.getMessage());
//            return false;
//        }
//        catch (Exception ex){
//            System.out.println(ex.getMessage());
//            return false;
//        }
//        finally {
//            if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
//        }
//    }
//
//}

// END OF PREVIOUS CODE IMPLEMENTATION

package EncryptedDiary.app;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SQLDatabaseConnection {

    private static final String connectionUrl = "jdbc:mysql://(localhost=33060,user=Matthewkilleen99," +
        "password=Mk16914004006425529)/ENCRYPTED_DIARY_DB";
    private Connection conn = null;

    public SQLDatabaseConnection(){
        this.openConnectionObject();
    }

    public void openConnectionObject(){
        try{
            this.conn = DriverManager.getConnection(connectionUrl);
        }
        catch(SQLException ex){
            System.out.println("Error establishing connection to SQL database...");
            System.out.println(ex.getMessage());
        }
    }

    public void resetConnectionObject(){
        this.deconstructConnectionObject();
        this.openConnectionObject();
    }

    public void deconstructConnectionObject(){
        try{
            this.conn.close();
        }
        catch(SQLException ex){
            System.out.println(ex.getMessage());
        }
        finally {
            this.conn = null;
        }
    }

    public PreparedStatement generatePreparedStatement(String query, Object ... parameters) throws SQLException {
        PreparedStatement ps = this.conn.prepareStatement(query);

        long numOfParameters = query.chars().filter(ch -> ch == '?').count();
        for (int i = 0; i < numOfParameters; i++){
            ps.setObject(i+1, parameters[i]);
        }

        return ps;
    }

//    public boolean tempSQlUpdate(PreparedStatement ps) throws SQLException {
//        return ps.executeUpdate() == 1;
//    }

    public List<String []> executeSQLQuery(String query, Object ... parameters){
        List<String []> resultList = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps = this.generatePreparedStatement(query, parameters);
            rs = ps.executeQuery(query);
            ResultSetMetaData rsmd = rs.getMetaData();
            final int columnCount = rsmd.getColumnCount();
            while (rs.next()){
                String [] rowValues = new String[columnCount];
                for (int i = 1; i <= columnCount; i++){
                    rowValues[i-1] = rs.getString(i);
                }
                resultList.add(rowValues);
            }
            return resultList;
        }
        catch (SQLException sqlEx){
            System.out.println(sqlEx.getMessage());
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
        }
        finally {
            if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
            if (ps != null) try { ps.close(); } catch (SQLException ignore) {}
        }
        return null;
    }

//    /**
//     * Executes a query on the database and returns the results in an array list
//     * @param query - SQL query to be executed in database
//     * @return results - array list of results
//     */
//    public List<String []> executeSQLQuery(String query) {
//        List<String []> resultList = new ArrayList<>();
//        Statement stmt = null;
//        ResultSet rs = null;
//        try{
//            stmt = conn.createStatement();
//            rs = stmt.executeQuery(query);
//            ResultSetMetaData rsmd = rs.getMetaData();
//            final int columnCount = rsmd.getColumnCount();
//            while (rs.next()){
//                String [] rowValues = new String[columnCount];
//                for (int i = 1; i <= columnCount; i++){
//                    rowValues[i-1] = rs.getString(i);
//                }
//                resultList.add(rowValues);
//            }
//            return resultList;
//        }
//        catch (SQLException sqlEx){
//            System.out.println(sqlEx.getMessage());
//        }
//        catch (Exception ex){
//            System.out.println(ex.getMessage());
//        }
//        finally {
//            if (rs != null) try { rs.close(); } catch (SQLException ignore) {}
//            if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
//        }
//        return null;
//    }

    public boolean executeSQLUpdate(String query, Object ... parameters){
        PreparedStatement ps = null;
        int numberOfAffectedRows = 0;
        try{
            ps = this.generatePreparedStatement(query, parameters);
            numberOfAffectedRows = ps.executeUpdate(query); // not used right now but for documentation purposes assigned
            return true;
        }
        catch (SQLException sqlEx){
            System.out.println(sqlEx.getMessage());
            return false;
        }
        catch (Exception ex){
            System.out.println(ex.getMessage());
            return false;
        }
        finally {
            if (ps != null) try { ps.close(); } catch (SQLException ignore) {}
        }
    }

//    public boolean executeSQLUpdate(String query){
//        Statement stmt = null;
//        int numberOfAffectedRows = 0;
//        try{
//            stmt = conn.createStatement();
//            numberOfAffectedRows = stmt.executeUpdate(query); // not used right now but for documentation purposes assigned
//            return true;
//        }
//        catch (SQLException sqlEx){
//            System.out.println(sqlEx.getMessage());
//            return false;
//        }
//        catch (Exception ex){
//            System.out.println(ex.getMessage());
//            return false;
//        }
//        finally {
//            if (stmt != null) try { stmt.close(); } catch (SQLException ignore) {}
//        }
//    }

}
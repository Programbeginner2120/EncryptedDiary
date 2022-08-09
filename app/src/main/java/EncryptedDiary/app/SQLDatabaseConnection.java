package EncryptedDiary.app;

import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class SQLDatabaseConnection {

    private static String connectionUrl;
    private Connection conn = null;

    static {
        Gson gson = null;
        Reader reader = null;
        try{
            gson = new Gson();
            reader = Files.newBufferedReader(Paths.get("app/src/main/java/EncryptedDiary/app/usercredentials/Credentials.json"));
            Map<?, ?> map = gson.fromJson(reader, Map.class);
            SQLDatabaseConnection.connectionUrl = (String) map.get("connectionUrl");
        }
        catch (Exception ex){
            ex.printStackTrace();
        }
        finally{
            if (reader != null){
                try{
                    reader.close();
                }
                catch (Exception ex){
                    System.out.println("Could not close resources");
                }

            }
        }
    }

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

    public List<Object []> executeSQLQuery(String query, Object ... parameters){
        List<Object []> resultList = new ArrayList<>();
        PreparedStatement ps = null;
        ResultSet rs = null;
        try{
            ps = this.generatePreparedStatement(query, parameters);
            rs = ps.executeQuery();
            ResultSetMetaData rsmd = rs.getMetaData();
            final int columnCount = rsmd.getColumnCount();
            while (rs.next()){
                Object [] rowValues = new Object[columnCount];
                for (int i = 1; i <= columnCount; i++){
                    rowValues[i-1] = rs.getObject(i);
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

    public boolean executeSQLUpdate(String query, Object ... parameters){
        PreparedStatement ps = null;
        int numberOfAffectedRows = 0;
        try{
            ps = this.generatePreparedStatement(query, parameters);
            numberOfAffectedRows = ps.executeUpdate(); // not used right now but for documentation purposes assigned
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
}

//TODO: TAKE ANYTHING OUT THAT IS CONFIDENTIAL, IMPLEMENT TRY THEN CLOSE FUNCTIONALITY WITH DATABASE RESOURCES WHERE
// NOT DONE ALREADY
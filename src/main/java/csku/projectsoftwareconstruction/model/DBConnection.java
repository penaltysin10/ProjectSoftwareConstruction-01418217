package csku.projectsoftwareconstruction.model;

import java.sql.*;

public class DBConnection {
    private static final String DB_URL = "jdbc:sqlite:RegisterInfo";

    public DBConnection(){}

    public Connection getConnectionDB(){
        Connection connect = null;
        try {
            Class.forName("org.sqlite.JDBC");
            if (connect == null){
                connect = DriverManager.getConnection(DB_URL);
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e){
            e.printStackTrace();
        }
        return connect;
    }

    public void closeConnectionDB(Connection connect, Statement statement, ResultSet result){
        try { result.close(); } catch (Exception e) { /* ignored */ }
        try { statement.close(); } catch (Exception e) { /* ignored */ }
        try { connect.close(); } catch (Exception e) { /* ignored */ }
    }

}

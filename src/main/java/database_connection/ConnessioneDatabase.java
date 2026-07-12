package database_connection;

import java.sql.*;

public class ConnessioneDatabase {
    private static ConnessioneDatabase instance;
    public Connection connection = null;
    private static final String NOME = "postgres";
    private static final String PASSWORD = "pswpostgres";
    private static final String URL = "jdbc:postgresql://localhost:5432/Orario";

    private ConnessioneDatabase() throws SQLException {

        connection = DriverManager.getConnection(URL, NOME, PASSWORD);

    }

    public static ConnessioneDatabase getInstance() throws SQLException {
        if (instance == null || instance.connection.isClosed())
        {
            instance = new ConnessioneDatabase();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }



}
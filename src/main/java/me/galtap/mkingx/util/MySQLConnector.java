package me.galtap.mkingx.util;


import org.bukkit.ChatColor;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnector {
    private Connection connection;

    public MySQLConnector(String host, int port, String userName, String password, String databaseName){
        this.connection = getConnect(host,port,databaseName,userName,password);
    }

    private Connection getConnect(String host, int port, String databaseName, String userName, String password) {
        try {

            if (connection != null && !connection.isClosed()) return connection;
            LoggerManager.CONNECT_PROCESS.logWarning();
            String url = "jdbc:mysql://" + host + ":" + port + "/" + databaseName;
            connection = DriverManager.getConnection(url, userName, password);

        } catch (SQLException e) {
            LoggerManager.CONNECTION_FAILED.logFatalError(e);
        }

        LoggerManager.CONNECTED.logColorMessage(ChatColor.GREEN);
        return connection;
    }
    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            LoggerManager.CLOSE_CONNECTION_ERROR.logFatalError(e);

        }
    }

    public Connection getConnection() {
        return connection;
    }
}

package me.galtap.mkingx.loader.impl;

import me.galtap.mkingx.loader.DataLoader;
import me.galtap.mkingx.model.Winner;
import me.galtap.mkingx.util.LoggerManager;
import me.galtap.mkingx.util.MySQLConnector;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class DatabaseLoader implements DataLoader {
    private final Connection connection;
    private final MySQLConnector mySQLConnector;
    public DatabaseLoader(String host, int port, String userName, String password, String databaseName) {
        mySQLConnector = new MySQLConnector(host,port,userName,password,databaseName);
        connection = mySQLConnector.getConnection();
        createTable();
    }

    private void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS kingWinners (uuid VARCHAR(37), name VARCHAR(36), win INT, prize LONG);";
        CompletableFuture.runAsync(() -> {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.executeUpdate();
            } catch (SQLException e) {
                LoggerManager.MYSQL_TABLE_CREATE_ERROR.logFatalError(e);
            }
        });
    }

    @Override
    public void setWinner(String uuid, String name, int winners, long prizes) {
        String sql = "INSERT INTO kingWinners (uuid, name, win, prize) VALUES (?, ?, ?, ?) ON DUPLICATE KEY UPDATE win = win + VALUES(win), prize = prize + VALUES(prize);";
        CompletableFuture.runAsync(() -> {
            try (PreparedStatement statement = connection.prepareStatement(sql)) {
                statement.setString(1, uuid);
                statement.setString(2, name);
                statement.setInt(3, winners);
                statement.setLong(4, prizes);
                statement.executeUpdate();
            } catch (SQLException e) {
                LoggerManager.MYSQL_DATA_UPDATE_ERROR.logFatalError(e);
            }
        });
    }

    @Override
    public Winner getWinner(String uuid) {
        String sql = "SELECT uuid, name, win, prize FROM kingWinners WHERE uuid = ?;";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1,uuid);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    String name = resultSet.getString("name");
                    int winCount = resultSet.getInt("win");
                    long prizeCount = resultSet.getLong("prize");

                    return new Winner(uuid, name, winCount, prizeCount);
                }
            }
        } catch (SQLException e) {
            LoggerManager.MYSQL_GET_PLAYER_DATA_ERROR.logFatalError(e);
        }

        return null;
    }

    public List<Winner> getAllWinners() {
        List<Winner> winners = new ArrayList<>();

        String sql = "SELECT uuid, name, win, prize FROM kingWinners;";
        try (PreparedStatement statement = connection.prepareStatement(sql); ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                String uuid = resultSet.getString("uuid");
                String name = resultSet.getString("name");
                int winCount = resultSet.getInt("win");
                long prizeCount = resultSet.getLong("prize");

                Winner winner = new Winner(uuid, name, winCount, prizeCount);
                winners.add(winner);
            }
        } catch (SQLException e) {
            LoggerManager.MYSQL_GET_PLAYER_DATA_ERROR.logFatalError(e);
        }

        if(!winners.isEmpty()){
            winners.sort(Comparator.comparingInt(Winner::getWinnings));
        }

        return winners;
    }
    public void closeConnection(){
        mySQLConnector.closeConnection();
    }

}

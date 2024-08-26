package com.example.pingpong.Controller;

import com.example.pingpong.Model.Game;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages database operations related to game data.
 * Provides methods to save, update, and load game states from the database.
 */
public class DatabaseManager {

    private static DatabaseManager instance;

    /**
     * Private constructor to prevent direct instantiation.
     */
    private DatabaseManager() {}

    /**
     * Singleton pattern implementation.
     * Provides a global point of access to the instance of the DatabaseManager.
     *
     * @return The single instance of DatabaseManager.
     */
    public static synchronized DatabaseManager getInstance() {
        if (instance == null) {
            instance = new DatabaseManager();
        }
        return instance;
    }

    /**
     * Saves a new game state to the database.
     *
     * @param gameName Name of the game.
     * @param player1Name Name of Player 1.
     * @param player1Score Score of Player 1.
     * @param player2Name Name of Player 2.
     * @param player2Score Score of Player 2.
     * @param gameLimit The score limit for the game.
     * @throws SQLException If an SQL error occurs.
     */
    public void saveNewGame(String gameName, String player1Name, int player1Score, String player2Name, int player2Score, int gameLimit) throws SQLException {
        String sql = "INSERT INTO Game (game_name, player1_name, player1_score, player2_name, player2_score, game_limit) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, gameName);
            pstmt.setString(2, player1Name);
            pstmt.setInt(3, player1Score);
            pstmt.setString(4, player2Name);
            pstmt.setInt(5, player2Score);
            pstmt.setInt(6, gameLimit);
            pstmt.executeUpdate();
        }
    }

    /**
     * Updates an existing game's data in the database.
     *
     * @param gameName Name of the game to update.
     * @param player1Name New name of Player 1.
     * @param player1Score New score of Player 1.
     * @param player2Name New name of Player 2.
     * @param player2Score New score of Player 2.
     * @param gameLimit New game score limit.
     * @throws SQLException If an SQL error occurs.
     */
    public void updateExistingGame(String gameName, String player1Name, int player1Score, String player2Name, int player2Score, int gameLimit) throws SQLException {
        String sql = "UPDATE Game SET player1_name = ?, player1_score = ?, player2_name = ?, player2_score = ?, game_limit = ? WHERE game_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, player1Name);
            pstmt.setInt(2, player1Score);
            pstmt.setString(3, player2Name);
            pstmt.setInt(4, player2Score);
            pstmt.setInt(5, gameLimit);
            pstmt.setString(6, gameName);
            pstmt.executeUpdate();
        }
    }

    /**
     * Checks if a game exists in the database.
     *
     * @param gameName Name of the game to check.
     * @return true if the game exists, false otherwise.
     * @throws SQLException If an SQL error occurs.
     */
    public boolean gameExists(String gameName) throws SQLException {
        String query = "SELECT COUNT(*) FROM Game WHERE game_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, gameName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }
        return false;
    }

    /**
     * Loads the most recent game state from the database.
     *
     * @return The most recently saved game, or null if no games are found.
     * @throws SQLException If an SQL error occurs or the game cannot be loaded.
     */
    public Game loadLatestGame() throws SQLException {
        Game latestGame = null;
        // Adjust the SQL to order by the primary key column assuming it's named 'id'
        String sql = "SELECT * FROM Game ORDER BY id DESC LIMIT 1";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                latestGame = buildGameFromResultSet(rs);
            }
        } catch (SQLException e) {
            System.err.println("Failed to load the latest game: " + e.getMessage());
            throw e;
        }
        return latestGame;
    }

    public Game loadGameByName(String gameName) throws SQLException {
        String sql = "SELECT * FROM Game WHERE game_name = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, gameName);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return buildGameFromResultSet(rs);
                }
            }
        }
        return null; // Handle case where game is not found
    }

    /**
     * Builds a Game object from the result set.
     *
     * @param rs ResultSet containing game data.
     * @return A new Game object built from the result set.
     * @throws SQLException If there is an issue reading from the result set.
     */
    private Game buildGameFromResultSet(ResultSet rs) throws SQLException {
        // Extract data from ResultSet and use it to build your Game object
        GameBuilder builder = new GameBuilder()
                .withPlayer1Name(rs.getString("player1_name"))
                .withPlayer1Score(rs.getInt("player1_score"))
                .withPlayer2Name(rs.getString("player2_name"))
                .withPlayer2Score(rs.getInt("player2_score"))
                .withTarget(rs.getInt("game_limit"));
        return builder.build();
    }

    /**
     * Retrieves a list of all game names stored in the database.
     *
     * @return A list of game names.
     * @throws SQLException If an SQL error occurs.
     */
    public List<String> loadGameNames() throws SQLException {
        List<String> gameNames = new ArrayList<>();
        String sql = "SELECT game_name FROM Game";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                gameNames.add(rs.getString("game_name"));
            }
        }
        return gameNames;
    }
}

package com.example.pingpong.Controller;

import com.example.pingpong.Model.Game;

import java.sql.*;
import java.util.List;

/**
 * Data Access Object (DAO) for handling game persistence operations.
 * This class provides an abstraction layer between the business logic and database management,
 * delegating database interactions to the {@link DatabaseManager}.
 */
public class GameDAO implements IGameDAO {

    private DatabaseManager dbManager;

    /**
     * Constructs a GameDAO and initializes its connection to the database through {@link DatabaseManager}.
     */
    public GameDAO() {
        this.dbManager = DatabaseManager.getInstance();
    }

    /**
     * Saves a new game to the database.
     *
     * @param game The game object containing the current state to be saved.
     * @param gameName The name under which the game will be saved, acting as a unique identifier.
     * @throws SQLException If an SQL error occurs during the operation.
     */
    @Override
    public void saveGame(Game game, String gameName) throws SQLException {
        // Uses DatabaseManager to save game details to the database
        dbManager.saveNewGame(gameName, game.getPlayer1().getName(), game.getPlayer1().getScore(),
                game.getPlayer2().getName(), game.getPlayer2().getScore(), game.getMaxScore());
    }

    /**
     * Updates an existing game's state in the database.
     *
     * @param game The game object containing the current state to be updated.
     * @param gameName The name of the game used as a unique identifier.
     * @throws SQLException If an SQL error occurs during the operation.
     */
    @Override
    public void updateGame(Game game, String gameName) throws SQLException {
        // Uses DatabaseManager to save game details to the database
        dbManager.updateExistingGame(gameName, game.getPlayer1().getName(), game.getPlayer1().getScore(),
                game.getPlayer2().getName(), game.getPlayer2().getScore(), game.getMaxScore());
    }

    /**
     * Checks if a game with the specified name exists in the database.
     *
     * @param gameName The name of the game to check.
     * @return true if the game exists, false otherwise.
     * @throws SQLException If an SQL error occurs during the operation.
     */
    @Override
    public boolean gameExists(String gameName) throws SQLException {
        return dbManager.gameExists(gameName);
    }

    /**
     * Loads a game from the database using the given name.
     *
     * @param gameName The name of the game to load.
     * @return A {@link Game} object representing the loaded game state or null if no game is found.
     * @throws SQLException If an SQL error occurs during the operation.
     */
    @Override
    public Game loadGame(String gameName) throws SQLException {
        // Uses DatabaseManager to load game details from the database
        return dbManager.loadGameByName(gameName);
    }

    /**
     * Loads the latest saved game from the database.
     *
     * @return A {@link Game} object representing the latest game state or null if no game is found.
     * @throws SQLException If an SQL error occurs during the operation.
     */
    @Override
    public Game loadLatestGame() throws SQLException {
        return dbManager.loadLatestGame();
    }

    /**
     * Retrieves a list of all game names stored in the database.
     *
     * @return A list of strings containing all the game names.
     * @throws SQLException If an SQL error occurs during the operation.
     */
    @Override
    public List<String> loadGameNames() throws SQLException {
        // Calls DatabaseManager to retrieve all game names
        return dbManager.loadGameNames();
    }
}
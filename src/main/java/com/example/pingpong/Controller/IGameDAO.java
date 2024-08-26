package com.example.pingpong.Controller;

import com.example.pingpong.Model.Game;
import java.sql.SQLException;
import java.util.List;

/**
 * Interface for data access object to handle database operations for {@link Game} objects.
 * Provides methods to save, update, check existence, and load games from the database.
 */
public interface IGameDAO {
    /**
     * Saves a new game into the database.
     *
     * @param game The game object to be saved.
     * @param gameName The name of the game for identification.
     * @throws SQLException If there is a problem executing the SQL commands.
     */
    void saveGame(Game game, String gameName) throws SQLException;

    /**
     * Updates an existing game in the database.
     *
     * @param game The game object with updated values.
     * @param gameName The name of the game to identify which game to update.
     * @throws SQLException If there is a problem executing the SQL commands.
     */
    void updateGame(Game game, String gameName) throws SQLException;

    /**
     * Checks if a game exists in the database by its name.
     *
     * @param gameName The name of the game to check.
     * @return true if the game exists, false otherwise.
     * @throws SQLException If there is a problem executing the SQL commands.
     */
    boolean gameExists(String gameName) throws SQLException;

    /**
     * Loads a game from the database by its name.
     *
     * @param gameName The name of the game to load.
     * @return The loaded game object, or null if no game with that name exists.
     * @throws SQLException If there is a problem executing the SQL commands.
     */
    Game loadGame(String gameName) throws SQLException;

    /**
     * Retrieves a list of all game names stored in the database.
     *
     * @return A list of game names.
     * @throws SQLException If there is a problem accessing the database.
     */
    List<String> loadGameNames() throws SQLException;

    /**
     * Loads the most recently saved game from the database.
     *
     * @return The latest game object.
     * @throws SQLException If there is a problem executing the SQL commands.
     */
    Game loadLatestGame() throws SQLException;

}
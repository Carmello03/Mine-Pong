package com.example.pingpong.Controller;

import com.example.pingpong.Model.GameSettings;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;

/**
 * Singleton class responsible for saving and loading game settings to and from files.
 * This class provides functionality to serialize {@link GameSettings} objects into files
 * and deserialize them back into objects.
 */
public class GameSaver {
    private static GameSaver instance;

    /**
     * Private constructor to prevent instantiation from outside ensuring a Singleton pattern.
     */
    private GameSaver() {}

    /**
     * Provides access to the singleton instance of {@link GameSaver}.
     *
     * @return The single instance of the {@link GameSaver}.
     */
    public static synchronized GameSaver getInstance() {
        if (instance == null) {
            instance = new GameSaver();
        }
        return instance;
    }

    /**
     * Saves the provided {@link GameSettings} to a specified file.
     *
     * @param filename The name of the file where the settings will be saved.
     * @param settings The {@link GameSettings} object to be serialized and saved.
     * @throws IOException If an I/O error occurs during the writing process.
     */
    public void saveGameSettings(String filename, GameSettings settings) throws IOException {
        String fullPath = "C:/Users/mghos/MTU/Year2-Semester2/OO_Prog/testing/Ping-Pong/Saves/" + filename;
        try (ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(fullPath))) {
            out.writeObject(settings);
            System.out.println("Saving to: " + new File(fullPath).getAbsolutePath());
        }
    }

    /**
     * Loads {@link GameSettings} from a specified file.
     *
     * @param fullPath The full path to the file from which to load the settings.
     * @return The {@link GameSettings} object deserialized from the file.
     * @throws IOException If an I/O error occurs during the reading process.
     * @throws ClassNotFoundException If the class of the serialized object cannot be found.
     */
    public GameSettings loadSettings(String fullPath) throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new FileInputStream(fullPath))) {
            return (GameSettings) in.readObject();
        }
    }

    /**
     * Checks if a file at the specified path is valid for loading.
     * A file is considered valid if it exists and is not empty.
     *
     * @param fullPath The full path to the file to be validated.
     * @return true if the file exists and is not empty, false otherwise.
     */
    public static boolean isFileValid(String fullPath) {
        File file = new File(fullPath);
        return file.exists() && file.length() > 0;
    }

}


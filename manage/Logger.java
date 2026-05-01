package manage;

import java.io.*;
import java.time.LocalDateTime;

public class Logger {
    private static final String LOG_FILE = "file/logs.txt";

    public static void log(String message) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(LOG_FILE, true))) {
            writer.println(LocalDateTime.now() + " - " + message);
        } catch (IOException e) {
            System.err.println("Error writing to log: " + e.getMessage());
        }
    }
}

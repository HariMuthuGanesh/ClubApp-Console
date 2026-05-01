package clubs;

import java.io.*;
import java.util.List;

public class RegistrationManager {
    private static final String REGISTRATIONS_FILE = "file/registrations.txt";

    public static void saveRegistrations(List<Club> clubs) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(REGISTRATIONS_FILE))) {
            for (Club club : clubs) {
                for (Event e : club.getEvents()) {
                    for (manage.Person attendee : e.getAttendees()) {
                        writer.println(club.getClubName() + "|" + e.getName() + "|" + attendee.getEmail());
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving registrations: " + e.getMessage());
        }
    }
}

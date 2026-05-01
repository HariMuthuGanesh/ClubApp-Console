package clubs;

import java.io.*;
import java.util.List;

public class MemberManager {
    private static final String MEMBERS_FILE = "file/members.txt";

    public static void saveMembers(List<Club> clubs) {
        try (PrintWriter writer = new PrintWriter(new FileWriter(MEMBERS_FILE))) {
            for (Club club : clubs) {
                for (manage.Person m : club.getMembers()) {
                    writer.println(club.getClubName() + "|" + m.getEmail());
                }
            }
        } catch (IOException e) {
            System.err.println("Error saving members: " + e.getMessage());
        }
    }
    
    // Logic for loading is still in ClubManager for now to maintain consistency with existing structure
    // but can be moved here if needed.
}

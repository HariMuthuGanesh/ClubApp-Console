
public class ClubManager {
    private Club[] clubs = new Club[50];
    private int clubCount = 0;

    public void addClub(Club club) {
        if (clubCount < 50) {
            clubs[clubCount++] = club;
        }
    }

    public void listAllClubs() {
        if (clubCount == 0) {
            System.out.println("No clubs registered.");
        } else {
            for (int i = 0; i < clubCount; i++) {
                System.out.println((i + 1) + ". " + clubs[i].getClubName());
            }
        }
    }

    public Club findClubByName(String name) {
        for (int i = 0; i < clubCount; i++) {
            if (clubs[i].getClubName().equalsIgnoreCase(name)) {
                return clubs[i];
            }
        }
        return null;
    }

    public Club getClubByCoordinator(User user) {
        for (int i = 0; i < clubCount; i++) {
            if (clubs[i].getCoordinator() == user) {
                return clubs[i];
            }
        }
        return null;
    }

    public Club getClubByIndex(int index) {
        if (index >= 0 && index < clubCount) {
            return clubs[index];
        }
        return null;
    }

    public int getClubCount() {
        return clubCount;
    }

    public void listJoinedClubs(User student) {
        boolean found = false;
        for (int i = 0; i < clubCount; i++) {
            if (clubs[i].isMember(student)) {
                System.out.println("- " + clubs[i].getClubName());
                found = true;
            }
        }
        if (!found) {
            System.out.println("You haven't joined any clubs yet.");
        }
    }

    public void joinClub(User student, int clubIdx) {
        Club club = getClubByIndex(clubIdx);
        if (club != null) {
            if (club.isMember(student)) {
                System.out.println("You are already a member of this club!");
            } else {
                club.addMember(student);
                System.out.println("Successfully joined " + club.getClubName() + "!");
            }
        } else {
            System.out.println("Invalid club selection.");
        }
    }
}

import java.util.Scanner;
import manage.*;
import clubs.*;
import details.*;

public class clubApp {
    private static UserManager userManager = new UserManager();
    private static ClubManager clubManager = new ClubManager();
    private static AttendanceManager attendanceManager = new AttendanceManager();
    private static Scanner sc = new Scanner(System.in);

    public static void main(String[] args) {
        clubManager.loadAll(userManager);
        // if (userManager.validateAdmin("admin@gmail.com", "admin@123") == null) {
        // userManager.addAdmin(new Admin("Hari", "admin@gmail.com", "admin@123"));
        // }
        // if (userManager.findCoordinatorByEmail("coordinator@gmail.com") == null) {
        // userManager.addCoordinator(new User("coordinator", "coordinator@gmail.com",
        // "Muthu@123"));
        // }
        // if (userManager.findStudentByEmail("student@gmail.com") == null) {
        // userManager.addStudent(new User("Student", "student@gmail.com",
        // "Student@123"));
        // }

        while (true) {
            System.out.println("\n========================================================");
            System.out.println("   Welcome to ClubApp - Dynamic Club Management System");
            System.out.println("========================================================");
            System.out.println("1. Login");
            System.out.println("2. Register");
            System.out.println("3. Exit");
            System.out.print("Select Choice: ");
            String choice = sc.nextLine();

            switch (choice) {
                case "1":
                    handleLogin();
                    break;
                case "2":
                    handleRegistration();
                    break;
                case "3":
                    System.out.println("Thank you for using ClubApp!");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice!");
            }
        }
    }

    private static void handleLogin() {
        System.out.println("\n--- Login ---");
        System.out.println("1. Admin");
        System.out.println("2. Coordinator");
        System.out.println("3. Student");
        System.out.print("Select Role: ");
        String role = sc.nextLine();

        System.out.print("Enter Email: ");
        String email = sc.nextLine();
        System.out.print("Enter Password: ");
        String password = sc.nextLine();

        switch (role) {
            case "1":
                Admin admin = userManager.validateAdmin(email, password);
                if (admin != null)
                    adminMenu(admin);
                else
                    System.out.println("Invalid credentials!");
                break;
            case "2":
                User coord = userManager.validateCoordinator(email, password);
                if (coord != null)
                    coordinatorMenu(coord);
                else
                    System.out.println("Invalid credentials!");
                break;
            case "3":
                User student = userManager.validateStudent(email, password);
                if (student != null)
                    studentMenu(student);
                else
                    System.out.println("Invalid credentials!");
                break;
            default:
                System.out.println("Invalid role!");
        }
    }

    private static void handleRegistration() {
        System.out.println("\n--- Register ---");
        System.out.println("1. Admin");
        System.out.println("2. Coordinator");
        System.out.println("3. Student");
        System.out.print("Select Role: ");
        String role = sc.nextLine();

        switch (role) {
            case "1":
                AdminDetails.register(userManager);
                break;
            case "2":
                CoordinatorDetails.register(userManager);
                break;
            case "3":
                StudentDetails.register(userManager);
                break;
            default:
                System.out.println("Invalid role!");
        }
    }

    private static void adminMenu(Admin admin) {
        while (true) {
            System.out.println("\n--- Admin Menu (Welcome " + admin.getName() + ") ---");
            System.out.println("1. Create New Club");
            System.out.println("2. View All Clubs / Join / Manage Events");
            System.out.println("3. View My Joined Clubs");
            System.out.println("4. Delete a Club");
            System.out.println("5. Delete a User");
            System.out.println("6. Logout");
            System.out.print("Select Choice: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                System.out.print("Enter Club Name: ");
                String name = sc.nextLine();
                System.out.print("Enter Description: ");
                String desc = sc.nextLine();
                System.out.print("Enter Coordinator Email: ");
                String email = sc.nextLine();

                User coordinator = userManager.findCoordinatorByEmail(email);
                if (coordinator != null) {
                    Club newClub = new Club(name, desc, coordinator);
                    clubManager.addClub(newClub);
                    System.out.println("Club created successfully!");
                } else {
                    System.out.println("Coordinator not found! Register them first.");
                }
            } else if (choice.equals("2")) {
                clubManager.listAllClubs();
                if (clubManager.getClubCount() > 0) {
                    System.out.print("Enter club number to interact: ");
                    try {
                        int idx = Integer.parseInt(sc.nextLine()) - 1;
                        Club selected = clubManager.getClubByIndex(idx);
                        if (selected != null) {
                            clubInteractionMenu(admin, selected, true);
                        } else {
                            System.out.println("Invalid club number!");
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid input!");
                    }
                }
            } else if (choice.equals("3")) {
                System.out.println("\n--- Your Joined Clubs ---");
                clubManager.listJoinedClubs(admin);
            } else if (choice.equals("4")) {
                clubManager.listAllClubs();
                if (clubManager.getClubCount() > 0) {
                    System.out.print("Enter club number to DELETE: ");
                    try {
                        int idx = Integer.parseInt(sc.nextLine()) - 1;
                        Club club = clubManager.getClubByIndex(idx);
                        if (club != null) {
                            String cname = club.getClubName();
                            clubManager.removeClub(idx);
                            attendanceManager.removeAttendanceByClub(cname);
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid input!");
                    }
                }
            } else if (choice.equals("5")) {
                System.out.print("Enter User Email to DELETE: ");
                String email = sc.nextLine();
                userManager.removeUser(email);
                clubManager.removeUserFromAll(email);
                attendanceManager.removeAttendanceByUser(email);
                System.out.println("User and all associated records removed.");
            } else if (choice.equals("6")) {
                break;
            } else {
                System.out.println("Invalid choice!");
            }
        }
    }

    private static void clubInteractionMenu(manage.Person person, Club club, boolean fullAccess) {
        while (true) {
            System.out.println("\n--- Club: " + club.getClubName() + " ---");
            System.out.println("1. View Club Details");
            System.out.println("2. View Events");
            System.out.println("3. Join Club");
            System.out.println("4. Register for an Event");

            int option = 5;
            boolean isAdmin = person instanceof Admin;

            if (fullAccess || isAdmin) {
                System.out.println(option++ + ". Add Event");
            }

            // Restricted functions
            if (fullAccess) {
                System.out.println(option++ + ". Remove Event");
                System.out.println(option++ + ". Update Event");
                System.out.println(option++ + ". Mark Attendance");
                System.out.println(option++ + ". View Attendance");
            }

            System.out.println("0. Back");
            System.out.print("Select Choice: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                club.viewDetails();
            } else if (choice.equals("2")) {
                club.listEvents();
            } else if (choice.equals("3")) {
                int cIdx = -1;
                for (int i = 0; i < clubManager.getClubCount(); i++) {
                    if (clubManager.getClubByIndex(i) == club) {
                        cIdx = i;
                        break;
                    }
                }
                clubManager.joinClub(person, cIdx);
            } else if (choice.equals("4")) {
                club.listEvents();
                if (!club.getEvents().isEmpty()) {
                    System.out.print("Enter event number to register: ");
                    try {
                        int eIdx = Integer.parseInt(sc.nextLine()) - 1;
                        if (club.registerStudentForEvent(person, eIdx)) {
                            clubManager.saveAll();
                            System.out.println("Successfully registered for the event!");
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid input!");
                    }
                }
            } else if (choice.equals("5") && (fullAccess || isAdmin)) {
                System.out.print("Event Name: ");
                String ename = sc.nextLine();
                System.out.print("Description: ");
                String edesc = sc.nextLine();
                System.out.print("Venue: ");
                String venue = sc.nextLine();
                System.out.print("Date: ");
                String date = sc.nextLine();
                System.out.print("Is this event for club members only? (yes/no): ");
                boolean mOnly = sc.nextLine().equalsIgnoreCase("yes");
                club.addEvent(new Event(ename, edesc, venue, date, mOnly));
                clubManager.saveAll();
            } else if (choice.equals("6") && fullAccess) {
                club.listEvents();
                System.out.print("Enter event number to remove: ");
                try {
                    int idx = Integer.parseInt(sc.nextLine()) - 1;
                    Event event = club.getEvents().get(idx);
                    String ename = event.getName();
                    club.removeEvent(idx);
                    clubManager.saveAll();
                    attendanceManager.removeAttendanceByEvent(club.getClubName(), ename);
                } catch (Exception e) {
                    System.out.println("Invalid input!");
                }
            } else if (choice.equals("7") && fullAccess) {
                club.listEvents();
                System.out.print("Enter event number to update: ");
                try {
                    int index = Integer.parseInt(sc.nextLine()) - 1;
                    System.out.print("New Name: ");
                    String nname = sc.nextLine();
                    System.out.print("New Description: ");
                    String ndesc = sc.nextLine();
                    System.out.print("New Venue: ");
                    String nvenue = sc.nextLine();
                    System.out.print("New Date: ");
                    String ndate = sc.nextLine();
                    System.out.print("Is this event for club members only? (yes/no): ");
                    boolean nmOnly = sc.nextLine().equalsIgnoreCase("yes");
                    club.updateEvent(index, new Event(nname, ndesc, nvenue, ndate, nmOnly));
                    clubManager.saveAll();
                } catch (Exception e) {
                    System.out.println("Invalid input!");
                }
            } else if (choice.equals("8") && fullAccess) {
                club.listEvents();
                System.out.print("Enter event number to mark attendance: ");
                try {
                    int eIdx = Integer.parseInt(sc.nextLine()) - 1;
                    Event event = club.getEvents().get(eIdx);
                    for (manage.Person p : event.getAttendees()) {
                        System.out.print("Mark " + p.getName() + " (" + p.getEmail() + ") as (P)resent / (A)bsent: ");
                        String status = sc.nextLine().equalsIgnoreCase("P") ? "PRESENT" : "ABSENT";
                        attendanceManager.markAttendance(club.getClubName(), event.getName(), p.getEmail(), status);
                    }
                    System.out.println("Attendance marked successfully.");
                } catch (Exception e) {
                    System.out.println("Invalid input!");
                }
            } else if (choice.equals("9") && fullAccess) {
                club.listEvents();
                System.out.print("Enter event number to view attendance: ");
                try {
                    int eIdx = Integer.parseInt(sc.nextLine()) - 1;
                    Event event = club.getEvents().get(eIdx);
                    java.util.List<String> records = attendanceManager.getAttendance(club.getClubName(),
                            event.getName());
                    if (records.isEmpty()) {
                        System.out.println("No attendance records found.");
                    } else {
                        for (String r : records) {
                            String[] p = r.split("\\|");
                            System.out.println(p[2] + " : " + p[3]);
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Invalid input!");
                }
            } else if (choice.equals("0")) {
                break;
            } else {
                System.out.println("Invalid choice!");
            }
        }
    }

    private static void coordinatorMenu(User coordinator) {
        Club club = clubManager.getClubByCoordinator(coordinator);
        if (club == null) {
            System.out.println("You are not assigned to any club yet. Contact Admin.");
            return;
        }
        clubInteractionMenu(coordinator, club, true);
    }

    private static void studentMenu(User student) {
        while (true) {
            System.out.println("\n--- Student Menu ---");
            System.out.println("1. View All Clubs / Join / View Events");
            System.out.println("2. View My Joined Clubs");
            System.out.println("3. Logout");
            System.out.print("Select Choice: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                clubManager.listAllClubs();
                if (clubManager.getClubCount() > 0) {
                    System.out.print("Enter club number: ");
                    try {
                        int idx = Integer.parseInt(sc.nextLine()) - 1;
                        Club selected = clubManager.getClubByIndex(idx);
                        if (selected != null) {
                            clubInteractionMenu(student, selected, false);
                        } else {
                            System.out.println("Invalid club number!");
                        }
                    } catch (Exception e) {
                        System.out.println("Invalid input!");
                    }
                }
            } else if (choice.equals("2")) {
                System.out.println("\n--- Your Joined Clubs ---");
                clubManager.listJoinedClubs(student);
            } else if (choice.equals("3")) {
                break;
            } else {
                System.out.println("Invalid choice!");
            }
        }
    }
}

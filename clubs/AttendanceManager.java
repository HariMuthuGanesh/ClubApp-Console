package clubs;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class AttendanceManager {
    private static final String ATTENDANCE_FILE = "file/attendance.txt";
    private List<String> attendanceRecords = new ArrayList<>();

    public AttendanceManager() {
        loadAttendance();
    }

    public void markAttendance(String clubName, String eventName, String studentEmail, String status) {
        // Remove existing record if any
        removeAttendance(clubName, eventName, studentEmail);
        attendanceRecords.add(clubName + "|" + eventName + "|" + studentEmail + "|" + status);
        saveAttendance();
    }

    public List<String> getAttendance(String clubName, String eventName) {
        List<String> results = new ArrayList<>();
        for (String record : attendanceRecords) {
            if (record.startsWith(clubName + "|" + eventName + "|")) {
                results.add(record);
            }
        }
        return results;
    }

    public void removeAttendance(String clubName, String eventName, String studentEmail) {
        attendanceRecords.removeIf(record -> record.equalsIgnoreCase(clubName + "|" + eventName + "|" + studentEmail + "|PRESENT") ||
                                           record.equalsIgnoreCase(clubName + "|" + eventName + "|" + studentEmail + "|ABSENT"));
    }

    public void removeAttendanceByEvent(String clubName, String eventName) {
        attendanceRecords.removeIf(record -> record.startsWith(clubName + "|" + eventName + "|"));
        saveAttendance();
    }

    public void removeAttendanceByClub(String clubName) {
        attendanceRecords.removeIf(record -> record.startsWith(clubName + "|"));
        saveAttendance();
    }

    public void removeAttendanceByUser(String email) {
        attendanceRecords.removeIf(record -> record.contains("|" + email + "|"));
        saveAttendance();
    }

    public void loadAttendance() {
        attendanceRecords.clear();
        File file = new File(ATTENDANCE_FILE);
        if (!file.exists()) return;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.trim().isEmpty()) {
                    attendanceRecords.add(line);
                }
            }
        } catch (IOException e) {
            System.err.println("Error loading attendance: " + e.getMessage());
        }
    }

    public void saveAttendance() {
        try (PrintWriter writer = new PrintWriter(new FileWriter(ATTENDANCE_FILE))) {
            for (String record : attendanceRecords) {
                writer.println(record);
            }
        } catch (IOException e) {
            System.err.println("Error saving attendance: " + e.getMessage());
        }
    }
}

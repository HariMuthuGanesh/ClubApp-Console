package clubs;


public class Event {
    private String name;
    private String description;
    private String venue;
    private String date;
    private java.util.List<manage.Person> attendees = new java.util.ArrayList<>();

    public Event(String name, String description, String venue, String date) {
        this.name = name;
        this.description = description;
        this.venue = venue;
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getVenue() {
        return venue;
    }

    public String getDate() {
        return date;
    }

    public java.util.List<manage.Person> getAttendees() {
        return attendees;
    }

    public void addAttendee(manage.Person student) {
        if (!isRegistered(student)) {
            attendees.add(student);
        }
    }

    public boolean isRegistered(manage.Person student) {
        for (manage.Person u : attendees) {
            if (u.getEmail().equalsIgnoreCase(student.getEmail())) {
                return true;
            }
        }
        return false;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public String toString() {
        return "Event: " + name + " | Description: " + description + " | Venue: " + venue + " | Date: " + date
                + " | Registered: " + attendees.size();
    }
}

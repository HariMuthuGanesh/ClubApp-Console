I already have a Java console project called ClubApp.
Do NOT rewrite the whole project.
Do NOT change existing class names.
Only extend the project.

Current features exist:
- Admin / Coordinator / Student roles
- Clubs
- Events
- Members
- Event registration
- File persistence
- Manager classes
- ArrayList usage

I want to add new features without breaking existing system.

--------------------------------
NEW FEATURE 1 — Member-only events
--------------------------------

Each event must have a new field:

boolean membersOnly

When creating an event, coordinator/admin should choose:

Is this event for club members only? (yes/no)

Store this in events.txt

New format:

ClubName|EventName|Description|Venue|Date|MembersOnly

Example:

CSI|Hackathon|Coding|Lab|12-06|true
CSI|Seminar|Talk|Hall|15-06|false

During registration:

If membersOnly == true
    check if student is member of club
    if not → reject registration

If membersOnly == false
    allow registration

Do not apply member check for all events.
Only apply when membersOnly is true.

--------------------------------
NEW FEATURE 2 — Attendance tracking
--------------------------------

Add attendance system.

File:

attendance.txt

Format:

ClubName|EventName|StudentEmail|Status

Status:
PRESENT
ABSENT

Coordinator can mark attendance after event.

Add AttendanceManager class.

Functions:

markAttendance
getAttendance
removeAttendance
loadAttendance
saveAttendance

--------------------------------
NEW FEATURE 3 — Separate record files
--------------------------------

Use separate files:

users.txt
clubs.txt
members.txt
events.txt
registrations.txt
attendance.txt
logs.txt

Do not merge data.

--------------------------------
NEW FEATURE 4 — Cascade delete
--------------------------------

When deleting event:
remove from events.txt
remove from registrations.txt
remove from attendance.txt

When deleting user:
remove from users.txt
remove from members.txt
remove from registrations.txt
remove from attendance.txt

When deleting club:
remove from clubs.txt
remove from members.txt
remove from events.txt
remove from registrations.txt
remove from attendance.txt

--------------------------------
NEW FEATURE 5 — Manager classes
--------------------------------

Add new classes:

MemberManager
RegistrationManager
AttendanceManager

Do not remove existing managers.

--------------------------------
IMPORTANT RULES
--------------------------------

Do not rewrite entire project.
Do not change UI.
Modify only required classes:
Event
Club
ClubManager
Registration logic
File handling

Generate only changed parts and new classes.
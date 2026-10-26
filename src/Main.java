import exceptions.ClubNotFoundException;
import exceptions.DuplicateMemberException;
import exceptions.InvalidClubDataException;
import managers.ClubManager;
import managers.ReportGenerator;
import models.*;
import utils.InputHelper;

import java.util.List;

public class Main {

    private static final ClubManager    manager   = new ClubManager();
    private static final ReportGenerator reporter = new ReportGenerator(manager);

    public static void main(String[] args) {
        loadSampleData();

        boolean running = true;
        while (running) {
            printMainMenu();
            int choice = InputHelper.readIntInRange("  Enter your choice: ", 0, 10);

            switch (choice) {
                case 1  -> addClub();
                case 2  -> viewAllClubs();
                case 3  -> registerMember();
                case 4  -> assignClubLeader();
                case 5  -> addActivity();
                case 6  -> removeMember();
                case 7  -> updateClub();
                case 8  -> deleteClub();
                case 9  -> searchMenu();
                case 10 -> reportsMenu();
                case 0  -> { running = false; printGoodbye(); }
            }
        }
    }

    private static void printMainMenu() {
        System.out.println();
        printBanner();
        System.out.println("  ╔══════════════════════════════════════════╗");
        System.out.println("  ║         MAIN MENU                        ║");
        System.out.println("  ╠══════════════════════════════════════════╣");
        System.out.println("  ║  1. Add Club                             ║");
        System.out.println("  ║  2. View All Clubs                       ║");
        System.out.println("  ║  3. Register Member                      ║");
        System.out.println("  ║  4. Assign Club Leader                   ║");
        System.out.println("  ║  5. Add Activity to Club                 ║");
        System.out.println("  ║  6. Remove Member from Club              ║");
        System.out.println("  ║  7. Update Club Details                  ║");
        System.out.println("  ║  8. Delete Club                          ║");
        System.out.println("  ║  9. Search (Club / Member)               ║");
        System.out.println("  ║ 10. Reports                              ║");
        System.out.println("  ║  0. Exit                                 ║");
        System.out.println("  ╚══════════════════════════════════════════╝");
    }

    private static void printBanner() {
        System.out.println("  ╔══════════════════════════════════════════╗");
        System.out.println("  ║    UCA CLUB MANAGEMENT SYSTEM  v1.0      ║");
        System.out.println("  ║    University of Central Asia            ║");
        System.out.println("  ╚══════════════════════════════════════════╝");
    }

    private static void printGoodbye() {
        System.out.println();
        System.out.println("  ╔══════════════════════════════════════════╗");
        System.out.println("  ║  Thank you for using UCA Club Manager!   ║");
        System.out.println("  ║  Goodbye!                                ║");
        System.out.println("  ╚══════════════════════════════════════════╝");
    }

    private static void separator() {
        System.out.println("  ------------------------------------------");
    }


    private static void addClub() {
        System.out.println("\n  === ADD NEW CLUB ===");
        System.out.println("  Club Types:");
        System.out.println("    1. Academic Club");
        System.out.println("    2. Sports Club");
        int typeChoice = InputHelper.readIntInRange("  Choose type: ", 1, 2);

        String id          = manager.generateClubId();
        String name        = InputHelper.readNonEmptyString("  Enter Club Name        : ");
        String established = InputHelper.readNonEmptyString("  Year Established       : ");
        String description = InputHelper.readNonEmptyString("  Description            : ");
        int    maxMembers  = InputHelper.readPositiveInt("  Max Members            : ");

        try {
            if (typeChoice == 1) {
                // Academic Club
                String subject  = InputHelper.readNonEmptyString("  Academic Subject       : ");
                String advisor  = InputHelper.readNonEmptyString("  Faculty Advisor Name   : ");
                Club club = new AcademicClub(id, name, established, description, maxMembers, subject, advisor);
                manager.addClub(club); // Exception Handling: may throw InvalidClubDataException
                System.out.println("\n  [✓] Academic Club added successfully! ID: " + id);
            } else {
                // Sports Club
                String sport  = InputHelper.readNonEmptyString("  Sport Type             : ");
                String venue  = InputHelper.readNonEmptyString("  Home Venue             : ");
                String coach  = InputHelper.readNonEmptyString("  Coach Name             : ");
                Club club = new SportsClub(id, name, established, description, maxMembers, sport, venue, coach);
                manager.addClub(club);
                System.out.println("\n  [✓] Sports Club added successfully! ID: " + id);
            }
        } catch (InvalidClubDataException e) {
            // Exception Handling: custom exception caught here
            System.out.println("\n  [✗] Error: " + e);
        }
        InputHelper.pressEnterToContinue();
    }

    private static void viewAllClubs() {
        System.out.println("\n  === ALL CLUBS ===");
        List<Club> clubs = manager.getAllClubs();

        if (clubs.isEmpty()) {
            System.out.println("  No clubs registered yet.");
        } else {
            for (Club club : clubs) {
                separator();
                club.displayDetails(); // Polymorphism in action
            }
            separator();
            System.out.println("  Total clubs: " + clubs.size());
        }
        InputHelper.pressEnterToContinue();
    }

    private static void registerMember() {
        System.out.println("\n  === REGISTER NEW MEMBER ===");
        String id    = manager.generateMemberId();
        String name  = InputHelper.readNonEmptyString("  Full Name              : ");
        String email = InputHelper.readNonEmptyString("  Email Address          : ");
        int    age   = InputHelper.readPositiveInt("  Age                    : ");
        String major = InputHelper.readNonEmptyString("  Academic Major         : ");

        StudentMember member = new StudentMember(id, name, email, age, major);

        try {
            manager.registerMember(member);

            if (InputHelper.confirm("  Enroll in a club now?")) {
                viewAllClubsSummary();
                String clubId = InputHelper.readNonEmptyString("  Enter Club ID to join  : ").toUpperCase();
                manager.enrollMemberInClub(id, clubId);
                System.out.println("  [✓] Enrolled in club " + clubId);
            }
            System.out.println("\n  [✓] Member registered successfully! ID: " + id);
        } catch (DuplicateMemberException e) {
            System.out.println("\n  [✗] " + e);
        } catch (InvalidClubDataException e) {

            System.out.println("\n  [✗] " + e.getMessage());
        }
        InputHelper.pressEnterToContinue();
    }


    private static void assignClubLeader() {
        System.out.println("\n  === ASSIGN CLUB LEADER ===");
        viewAllClubsSummary();
        String clubId = InputHelper.readNonEmptyString("  Enter Club ID          : ").toUpperCase();
        viewMembersSummary();
        String memberId = InputHelper.readNonEmptyString("  Enter Member ID        : ").toUpperCase();
        String date     = InputHelper.readNonEmptyString("  Leadership Start Date  : ");

        try {

            manager.assignClubLeader(memberId, clubId, date);
            System.out.println("\n  [✓] Member [" + memberId + "] is now the leader of Club [" + clubId + "]!");
        } catch (InvalidClubDataException e) {
            System.out.println("\n  [✗] " + e.getMessage());
        }
        InputHelper.pressEnterToContinue();
    }


    private static void addActivity() {
        System.out.println("\n  === ADD ACTIVITY TO CLUB ===");
        viewAllClubsSummary();
        String clubId = InputHelper.readNonEmptyString("  Enter Club ID          : ").toUpperCase();
        String actId  = manager.generateActivityId();
        String title  = InputHelper.readNonEmptyString("  Activity Title         : ");

        System.out.println("  Activity Types: 1. Meeting  2. Workshop  3. Competition  4. Other");
        int typeChoice = InputHelper.readIntInRange("  Choose type            : ", 1, 4);
        String[] types = {"Meeting", "Workshop", "Competition", "Other"};
        String type = types[typeChoice - 1];

        String date    = InputHelper.readNonEmptyString("  Date (YYYY-MM-DD)      : ");
        String desc    = InputHelper.readNonEmptyString("  Description            : ");
        int    maxPart = InputHelper.readPositiveInt("  Max Participants       : ");

        Activity activity = new Activity(actId, title, type, date, desc, clubId, maxPart);

        try {
            manager.addActivityToClub(clubId, activity); // Exception Handling
            System.out.println("\n  [✓] Activity [" + actId + "] added to club [" + clubId + "]!");
        } catch (ClubNotFoundException e) {
            System.out.println("\n  [✗] " + e.getMessage());
        }
        InputHelper.pressEnterToContinue();
    }

    private static void removeMember() {
        System.out.println("\n  === REMOVE MEMBER FROM CLUB ===");
        viewAllClubsSummary();
        String clubId   = InputHelper.readNonEmptyString("  Enter Club ID          : ").toUpperCase();
        viewMembersSummary();
        String memberId = InputHelper.readNonEmptyString("  Enter Member ID        : ").toUpperCase();

        if (InputHelper.confirm("  Confirm removal of member [" + memberId + "] from club [" + clubId + "]?")) {
            try {
                manager.removeMemberFromClub(memberId, clubId);
                System.out.println("\n  [✓] Member removed from club successfully.");
            } catch (InvalidClubDataException e) {
                System.out.println("\n  [✗] " + e.getMessage());
            }
        } else {
            System.out.println("  Operation cancelled.");
        }
        InputHelper.pressEnterToContinue();
    }

    private static void updateClub() {
        System.out.println("\n  === UPDATE CLUB DETAILS ===");
        viewAllClubsSummary();
        String clubId = InputHelper.readNonEmptyString("  Enter Club ID to update: ").toUpperCase();

        try {
            Club club = manager.findClubById(clubId);
            System.out.println("  Current Name      : " + club.getClubName());
            System.out.println("  Current Desc      : " + club.getDescription());
            System.out.println("  Current MaxMembers: " + club.getMaxMembers());
            System.out.println("  (Leave blank to keep current value)");

            String newName       = InputHelper.readOptionalString("  New Name         : ");
            String newDesc       = InputHelper.readOptionalString("  New Description  : ");
            String newMaxStr     = InputHelper.readOptionalString("  New Max Members  : ");
            int    newMax        = newMaxStr.isBlank() ? -1 :
                    Integer.parseInt(newMaxStr);

            manager.updateClub(clubId, newName, newDesc, newMax);
            System.out.println("\n  [✓] Club [" + clubId + "] updated successfully.");
        } catch (ClubNotFoundException e) {
            System.out.println("\n  [✗] " + e.getMessage());
        } catch (NumberFormatException e) {
            System.out.println("\n  [✗] Invalid number entered. Update cancelled.");
        }
        InputHelper.pressEnterToContinue();
    }

    private static void deleteClub() {
        System.out.println("\n  === DELETE CLUB ===");
        viewAllClubsSummary();
        String clubId = InputHelper.readNonEmptyString("  Enter Club ID to delete: ").toUpperCase();

        try {
            Club club = manager.findClubById(clubId);
            System.out.println("  About to delete: " + club.getSummary());
            if (InputHelper.confirm("  Are you sure?")) {
                manager.deleteClub(clubId); // Exception Handling
                System.out.println("\n  [✓] Club [" + clubId + "] deleted successfully.");
            } else {
                System.out.println("  Deletion cancelled.");
            }
        } catch (ClubNotFoundException e) {
            System.out.println("\n  [✗] " + e.getMessage());
        }
        InputHelper.pressEnterToContinue();
    }

    private static void searchMenu() {
        System.out.println("\n  === SEARCH ===");
        System.out.println("  1. Search Club by Name");
        System.out.println("  2. Search Member by Name");
        System.out.println("  3. Find Club by ID");
        System.out.println("  4. Find Member by ID");
        System.out.println("  5. Filter Clubs by Type");
        int choice = InputHelper.readIntInRange("  Enter choice: ", 1, 5);

        switch (choice) {
            case 1 -> {
                String keyword = InputHelper.readNonEmptyString("  Enter keyword: ");
                List<Club> results = manager.searchClubsByName(keyword);
                if (results.isEmpty()) System.out.println("  No clubs found matching: " + keyword);
                else results.forEach(c -> { separator(); c.displayDetails(); });
            }
            case 2 -> {
                String keyword = InputHelper.readNonEmptyString("  Enter keyword: ");
                List<StudentMember> results = manager.searchMembersByName(keyword);
                if (results.isEmpty()) System.out.println("  No members found matching: " + keyword);
                else results.forEach(m -> { separator(); m.displayDetails(); });
            }
            case 3 -> {
                String clubId = InputHelper.readNonEmptyString("  Enter Club ID: ").toUpperCase();
                try {
                    Club club = manager.findClubById(clubId);
                    separator();
                    club.displayDetails();
                    System.out.println("\n  Activities:");
                    if (club.getActivities().isEmpty()) System.out.println("  (None)");
                    else club.getActivities().forEach(a -> System.out.println("    -> " + a));
                } catch (ClubNotFoundException e) {
                    System.out.println("  [✗] " + e.getMessage());
                }
            }
            case 4 -> {
                String memberId = InputHelper.readNonEmptyString("  Enter Member ID: ").toUpperCase();
                try {
                    StudentMember m = manager.findMemberById(memberId);
                    separator();
                    m.displayDetails();
                } catch (InvalidClubDataException e) {
                    System.out.println("  [✗] " + e.getMessage());
                }
            }
            case 5 -> {
                System.out.println("  Type: 1. Academic  2. Sports");
                int t = InputHelper.readIntInRange("  Choose: ", 1, 2);
                String type = (t == 1) ? "Academic" : "Sports";
                List<Club> results = manager.filterClubsByType(type);
                if (results.isEmpty()) System.out.println("  No " + type + " clubs found.");
                else results.forEach(c -> System.out.println("  " + c.getSummary()));
            }
        }
        InputHelper.pressEnterToContinue();
    }

    private static void reportsMenu() {
        System.out.println("\n  === REPORTS ===");
        System.out.println("  1. Full Summary Report");
        System.out.println("  2. Club Membership Report");
        System.out.println("  3. Activity Summary Report");
        System.out.println("  4. Member Report");
        System.out.println("  5. Active Clubs Report");
        int choice = InputHelper.readIntInRange("  Enter choice: ", 1, 5);

        switch (choice) {
            case 1 -> reporter.generateReport();
            case 2 -> reporter.generateClubMembershipReport();
            case 3 -> reporter.generateActivitySummaryReport();
            case 4 -> reporter.generateMemberReport();
            case 5 -> reporter.generateActiveClubsReport();
        }
        InputHelper.pressEnterToContinue();
    }

    private static void viewAllClubsSummary() {
        System.out.println("  --- Clubs ---");
        if (manager.getAllClubs().isEmpty()) {
            System.out.println("  (No clubs yet)");
        } else {
            manager.getAllClubs().forEach(c -> System.out.println("  " + c.getSummary()));
        }
    }

    private static void viewMembersSummary() {
        System.out.println("  --- Members ---");
        if (manager.getAllMembers().isEmpty()) {
            System.out.println("  (No members yet)");
        } else {
            manager.getAllMembers().forEach(m -> System.out.println("  " + m.getSummary()));
        }
    }

    private static void loadSampleData() {
        try {

            manager.addClub(new AcademicClub(
                    manager.generateClubId(), "Coding Club", "2019",
                    "Programming and software development club", 20,
                    "Computer Science", "Demo Faculty A"));

            manager.addClub(new AcademicClub(
                    manager.generateClubId(), "Debate Club", "2018",
                    "Public speaking and argumentation", 15,
                    "Liberal Arts", "Demo Faculty B"));

            manager.addClub(new SportsClub(
                    manager.generateClubId(), "Football Club", "2017",
                    "Competitive and recreational football", 25,
                    "Football", "UCA Main Field", "Demo Coach A"));

            manager.addClub(new SportsClub(
                    manager.generateClubId(), "Basketball Club", "2020",
                    "Basketball training and tournaments", 15,
                    "Basketball", "UCA Sports Hall", "Demo Coach B"));

            StudentMember m1 = new StudentMember(
                    manager.generateMemberId(), "Demo Student A", "student.a@example.com", 20, "CS");
            StudentMember m2 = new StudentMember(
                    manager.generateMemberId(), "Demo Student B", "student.b@example.com", 21, "Math");
            StudentMember m3 = new StudentMember(
                    manager.generateMemberId(), "Demo Student C", "student.c@example.com", 19, "Economics");
            StudentMember m4 = new StudentMember(
                    manager.generateMemberId(), "Demo Student D", "student.d@example.com", 22, "CS");

            manager.registerMember(m1);
            manager.registerMember(m2);
            manager.registerMember(m3);
            manager.registerMember(m4);

            manager.enrollMemberInClub("M001", "C001"); // Demo A → Coding Club
            manager.enrollMemberInClub("M002", "C001"); // Demo B → Coding Club
            manager.enrollMemberInClub("M003", "C002"); // Demo C → Debate Club
            manager.enrollMemberInClub("M004", "C003"); // Demo D → Football Club
            manager.enrollMemberInClub("M001", "C003"); // Demo A → Football Club too


            manager.assignClubLeader("M001", "C001", "2024-09-01");
            manager.assignClubLeader("M003", "C002", "2024-09-01");


            manager.addActivityToClub("C001", new Activity(
                    manager.generateActivityId(), "Python Bootcamp", "Workshop",
                    "2024-10-15", "Intro to Python programming", "C001", 20));
            manager.addActivityToClub("C001", new Activity(
                    manager.generateActivityId(), "Weekly Meeting", "Meeting",
                    "2024-11-01", "Club weekly sync", "C001", 20));
            manager.addActivityToClub("C002", new Activity(
                    manager.generateActivityId(), "Inter-University Debate", "Competition",
                    "2024-11-20", "Annual debate championship", "C002", 15));
            manager.addActivityToClub("C003", new Activity(
                    manager.generateActivityId(), "Friendly Match", "Competition",
                    "2024-10-30", "Football match vs AUCA", "C003", 25));

            System.out.println("  [INFO] Sample data loaded. Welcome to UCA Club Manager!");

        } catch (Exception e) {
            System.out.println("  [WARN] Sample data load error: " + e.getMessage());
        }
    }
}

package managers;

import interfaces.Reportable;
import models.*;

import java.util.List;


public class ReportGenerator implements Reportable {

    private final ClubManager manager;


    public ReportGenerator(ClubManager manager) {
        this.manager = manager;
    }

    @Override
    public void generateReport() {
        printSeparator("=");
        System.out.println("         UCA CLUB MANAGEMENT SYSTEM — FULL SUMMARY REPORT");
        printSeparator("=");
        System.out.println("  Total Clubs      : " + manager.getTotalClubs());
        System.out.println("    Academic Clubs : " + manager.countAcademicClubs());
        System.out.println("    Sports Clubs   : " + manager.countSportsClubs());
        System.out.println("  Total Members    : " + manager.getTotalMembers());
        System.out.println("    Club Leaders   : " + manager.countLeaders());
        System.out.println("    Regular Members: " + (manager.getTotalMembers() - manager.countLeaders()));
        System.out.println("  Total Activities : " + manager.getTotalActivities());
        printSeparator("=");
    }

    @Override
    public String getReportTitle() {
        return "UCA Club Management System — Full Summary Report";
    }


    public void generateClubMembershipReport() {
        printSeparator("=");
        System.out.println("               CLUB MEMBERSHIP REPORT");
        printSeparator("=");

        List<Club> clubs = manager.getAllClubs();
        if (clubs.isEmpty()) {
            System.out.println("  No clubs found in the system.");
        } else {
            for (Club club : clubs) {
                printSeparator("-");
                System.out.println("  Club : " + club.getClubName() + " [" + club.getId() + "]");
                System.out.println("  Type : " + club.getClubType());
                System.out.println("  Leader ID : " + (club.getLeaderId() == null ? "Not Assigned" : club.getLeaderId()));
                System.out.println("  Members (" + club.getMemberIds().size() + "/" + club.getMaxMembers() + "):");
                if (club.getMemberIds().isEmpty()) {
                    System.out.println("    (No members yet)");
                } else {
                    for (String memberId : club.getMemberIds()) {
                        try {
                            StudentMember m = manager.findMemberById(memberId);
                            System.out.println("    -> " + m.getSummary());
                        } catch (Exception e) {
                            System.out.println("    -> [Unknown Member: " + memberId + "]");
                        }
                    }
                }
            }
        }
        printSeparator("=");
    }


    public void generateActivitySummaryReport() {
        printSeparator("=");
        System.out.println("               ACTIVITY SUMMARY REPORT");
        printSeparator("=");

        List<Activity> activities = manager.getAllActivities();
        if (activities.isEmpty()) {
            System.out.println("  No activities recorded in the system.");
        } else {
            System.out.printf("  %-10s %-25s %-15s %-12s %-8s%n",
                    "ID", "Title", "Type", "Date", "Club");
            printSeparator("-");
            for (Activity a : activities) {
                System.out.printf("  %-10s %-25s %-15s %-12s %-8s%n",
                        a.getActivityId(), a.getTitle(), a.getType(),
                        a.getDate(), a.getClubId());
            }
        }
        printSeparator("=");
    }

    public void generateMemberReport() {
        printSeparator("=");
        System.out.println("               MEMBER REPORT");
        printSeparator("=");

        List<StudentMember> members = manager.getAllMembers();
        if (members.isEmpty()) {
            System.out.println("  No members found.");
        } else {
            System.out.printf("  %-8s %-20s %-15s %-12s %-15s%n",
                    "ID", "Name", "Major", "Role", "Clubs");
            printSeparator("-");
            for (StudentMember m : members) {
                System.out.printf("  %-8s %-20s %-15s %-12s %-15s%n",
                        m.getId(), m.getName(), m.getMajor(),
                        m.getRole(), String.join(", ", m.getClubIds()));
            }
        }
        printSeparator("=");
    }


    public void generateActiveClubsReport() {
        printSeparator("=");
        System.out.println("               ACTIVE CLUBS REPORT (clubs with members)");
        printSeparator("=");

        int count = 0;
        for (Club c : manager.getAllClubs()) {
            if (!c.getMemberIds().isEmpty()) {
                System.out.printf("  %-8s %-25s %-12s Members: %d/%d%n",
                        c.getId(), c.getClubName(), c.getClubType(),
                        c.getMemberIds().size(), c.getMaxMembers());
                count++;
            }
        }
        if (count == 0) System.out.println("  No active clubs found.");
        printSeparator("=");
    }

    private void printSeparator(String ch) {
        System.out.println("  " + ch.repeat(62));
    }
}

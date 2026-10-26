import managers.ClubManager;
import models.*;
import exceptions.ClubNotFoundException;

public class ClubDeletionTest {
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
    public static void main(String[] args) throws Exception {
        ClubManager m = new ClubManager();
        m.addClub(new AcademicClub("C001", "Coding", "2025", "Demo", 10, "CS", "Advisor"));
        m.addClub(new AcademicClub("C002", "Debate", "2025", "Demo", 10, "Arts", "Advisor"));
        m.registerMember(new StudentMember("M001", "Demo A", "a@example.com", 20, "CS"));
        m.registerMember(new StudentMember("M002", "Demo B", "b@example.com", 20, "CS"));
        for (String id : new String[]{"M001", "M002"}) {
            m.enrollMemberInClub(id, "C001");
            m.enrollMemberInClub(id, "C002");
        }
        m.assignClubLeader("M001", "C001", "2025-01-01");
        m.assignClubLeader("M002", "C002", "2025-01-01");
        m.addActivityToClub("C001", new Activity("A1", "Deleted activity", "Meeting", "2025-01-01", "Demo", "C001", 10));
        m.addActivityToClub("C002", new Activity("A2", "Kept activity", "Meeting", "2025-01-01", "Demo", "C002", 10));
        m.deleteClub("c001");
        check(m.getTotalActivities() == 1, "Deleted club activities must leave global reports");
        check("A2".equals(m.getAllActivities().get(0).getActivityId()), "Other club activities must survive");
        check(m.getTotalClubs() == 1 && "C002".equals(m.getAllClubs().get(0).getId()), "Only target club must be deleted");
        for (StudentMember member : m.getAllMembers()) {
            check(!member.isMemberOf("C001"), "Deleted club must leave every member record");
            check(member.isMemberOf("C002"), "Other club memberships must survive");
        }
        check(!(m.findMemberById("M001") instanceof ClubLeader), "Deleted club leader must become regular member");
        check(m.findMemberById("M002") instanceof ClubLeader, "Other club leader must remain leader");
        check(m.countLeaders() == 1 && m.getTotalMembers() == 2, "Member and leader totals must remain consistent");
        boolean missing = false;
        try { m.findClubById("C001"); } catch (ClubNotFoundException expected) { missing = true; }
        check(missing, "Deleted club must be absent from ID lookup");
        boolean rejected = false;
        try { m.deleteClub("missing"); } catch (ClubNotFoundException expected) { rejected = true; }
        check(rejected && m.getTotalActivities() == 1, "Rejected deletion must not mutate remaining activities");
        m.addClub(new AcademicClub("C003", "Empty", "2025", "Demo", 10, "CS", "Advisor"));
        m.deleteClub("C003");
        check(m.getTotalClubs() == 1 && m.getTotalActivities() == 1, "Deleting empty club must preserve unrelated records");
        System.out.println("PASS: club deletion, activities, memberships, demotion, other leaders, empty and missing clubs");
    }
}

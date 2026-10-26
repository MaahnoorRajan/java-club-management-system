import managers.ClubManager;
import models.*;
import exceptions.InvalidClubDataException;

public class LeaderRemovalTest {
    private static void check(boolean condition, String message) {
        if (!condition) throw new AssertionError(message);
    }
    private static ClubManager fixture() throws Exception {
        ClubManager manager = new ClubManager();
        manager.addClub(new AcademicClub("C001", "Coding", "2025", "Demo", 10, "CS", "Demo Advisor"));
        manager.addClub(new AcademicClub("C002", "Debate", "2025", "Demo", 10, "Arts", "Demo Advisor"));
        manager.registerMember(new StudentMember("M001", "Demo A", "a@example.com", 20, "CS"));
        manager.registerMember(new StudentMember("M002", "Demo B", "b@example.com", 20, "CS"));
        manager.enrollMemberInClub("M001", "C001");
        manager.enrollMemberInClub("M001", "C002");
        manager.enrollMemberInClub("M002", "C001");
        manager.assignClubLeader("M001", "C001", "2025-01-01");
        return manager;
    }
    public static void main(String[] args) throws Exception {
        ClubManager m = fixture();
        m.removeMemberFromClub("m001", "c001");
        check(m.findClubById("C001").getLeaderId() == null, "Removed leader must be cleared from club");
        check(!m.findClubById("C001").hasMember("M001"), "Removed leader must leave club membership");
        check(!(m.findMemberById("M001") instanceof ClubLeader), "Removed leader must become a regular member");
        check(!m.findMemberById("M001").isMemberOf("C001"), "Member's removed club must be cleared");
        check(m.findMemberById("M001").isMemberOf("C002"), "Other memberships must survive demotion");
        check(m.getAllMembers().contains(m.findMemberById("M001")), "Member lookup and list must agree");
        check(m.getTotalMembers() == 2 && m.countLeaders() == 0, "Member count and leader report must agree");
        m = fixture();
        m.removeMemberFromClub("M002", "C001");
        check("M001".equals(m.findClubById("C001").getLeaderId()), "Removing a regular member must preserve leader");
        check(m.findMemberById("M001") instanceof ClubLeader, "Unrelated leader must retain role");
        m = fixture();
        m.removeMemberFromClub("M001", "C002");
        check(m.findMemberById("M001") instanceof ClubLeader, "Leaving another club must preserve leadership");
        check("C001".equals(((ClubLeader)m.findMemberById("M001")).getLeadingClubId()), "Leadership target must be preserved");
        boolean rejected = false;
        try { m.removeMemberFromClub("M001", "C002"); }
        catch (InvalidClubDataException expected) { rejected = true; }
        check(rejected, "Removing a non-member must be rejected");
        check("M001".equals(m.findClubById("C001").getLeaderId()), "Rejected removal must preserve other club state");
        System.out.println("PASS: leader removal, lowercase IDs, other memberships, regular-member removal, rejected removal");
    }
}

package models;

public class ClubLeader extends StudentMember {

    // Encapsulation: private fields specific to leadership
    private String leadingClubId;         // ID of the club being led
    private String leadershipStartDate;   // Date when leadership started

    public ClubLeader(String id, String name, String email, int age,
                      String major, String leadingClubId, String leadershipStartDate) {
        super(id, name, email, age, major); // Inheritance: call StudentMember constructor
        this.leadingClubId       = leadingClubId;
        this.leadershipStartDate = leadershipStartDate;
        addClubId(leadingClubId); // A leader also is a member of their club
    }

    public String getLeadingClubId() { return leadingClubId; }

    public void setLeadingClubId(String leadingClubId) {
        this.leadingClubId = leadingClubId;
        addClubId(leadingClubId);
    }

    public String getLeadershipStartDate() { return leadershipStartDate; }

    public void setLeadershipStartDate(String leadershipStartDate) {
        this.leadershipStartDate = leadershipStartDate;
    }

    @Override
    public String getRole() {
        return "Club Leader";
    }

    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("  Leading Club    : " + leadingClubId);
        System.out.println("  Leadership Since: " + leadershipStartDate);
    }

    @Override
    public String getSummary() {
        return "[" + getId() + "] " + getName() + " | Leader of Club: " + leadingClubId;
    }
}

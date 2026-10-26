package models;

import java.util.ArrayList;
import java.util.List;

public class StudentMember extends User {

    private List<String> clubIds;

    private String major;

    public StudentMember(String id, String name, String email, int age, String major) {
        super(id, name, email, age);
        this.major   = major;
        this.clubIds = new ArrayList<>();
    }

    public String getMajor() { return major; }

    public void setMajor(String major) { this.major = major; }

    public List<String> getClubIds() { return clubIds; }

    public void addClubId(String clubId) {
        if (!clubIds.contains(clubId)) {
            clubIds.add(clubId);
        }
    }

    public void removeClubId(String clubId) {
        clubIds.remove(clubId);
    }
    public boolean isMemberOf(String clubId) {
        return clubIds.contains(clubId);
    }

    @Override
    public String getRole() {
        return "Student Member";
    }

    @Override
    public void displayDetails() {
        super.displayDetails(); // Reuse parent's display logic (Inheritance)
        System.out.println("  Major : " + major);
        System.out.println("  Clubs : " + (clubIds.isEmpty() ? "None" : String.join(", ", clubIds)));
    }

    @Override
    public String getSummary() {
        return "[" + getId() + "] " + getName() + " | Major: " + major + " | Clubs: " + clubIds.size();
    }
}

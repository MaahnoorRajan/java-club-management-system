package models;

import interfaces.Manageable;
import java.util.ArrayList;
import java.util.List;

public abstract class Club implements Manageable {


    private String       clubId;
    private String       clubName;
    private String       established;
    private String       description;
    private List<String> memberIds;
    private List<Activity> activities;
    private int          maxMembers;
    private String       leaderId;

    public Club(String clubId, String clubName, String established,
                String description, int maxMembers) {
        this.clubId      = clubId;
        this.clubName    = clubName;
        this.established = established;
        this.description = description;
        this.maxMembers  = maxMembers;
        this.memberIds   = new ArrayList<>();
        this.activities  = new ArrayList<>();
        this.leaderId    = null;
    }

    @Override
    public String getId()          { return clubId; }
    public String getClubName()    { return clubName; }
    public String getEstablished() { return established; }
    public String getDescription() { return description; }
    public int    getMaxMembers()  { return maxMembers; }
    public String getLeaderId()    { return leaderId; }
    public List<String>   getMemberIds()  { return memberIds; }
    public List<Activity> getActivities() { return activities; }

    public void setClubName(String clubName)       { this.clubName    = clubName;    }
    public void setEstablished(String established) { this.established = established; }
    public void setDescription(String description) { this.description = description; }
    public void setMaxMembers(int maxMembers)       { this.maxMembers  = maxMembers;  }
    public void setLeaderId(String leaderId)        { this.leaderId    = leaderId;    }

    public boolean isFull() { return memberIds.size() >= maxMembers; }

    public boolean hasMember(String memberId) { return memberIds.contains(memberId); }

    public void addMember(String memberId) { memberIds.add(memberId); }

    public boolean removeMember(String memberId) { return memberIds.remove(memberId); }

    public void addActivity(Activity activity) { activities.add(activity); }

    public Activity findActivity(String activityId) {
        for (Activity a : activities) {
            if (a.getActivityId().equalsIgnoreCase(activityId)) {
                return a;
            }
        }
        return null;
    }

    public abstract String getClubType();

    @Override
    public void displayDetails() {
        System.out.println("  Club ID     : " + clubId);
        System.out.println("  Name        : " + clubName);
        System.out.println("  Type        : " + getClubType());
        System.out.println("  Established : " + established);
        System.out.println("  Description : " + description);
        System.out.println("  Max Members : " + maxMembers);
        System.out.println("  Member Count: " + memberIds.size());
        System.out.println("  Leader ID   : " + (leaderId == null ? "Not Assigned" : leaderId));
        System.out.println("  Activities  : " + activities.size());
    }

    @Override
    public String getSummary() {
        return "[" + clubId + "] " + clubName + " | " + getClubType()
                + " | Members: " + memberIds.size() + "/" + maxMembers;
    }

    @Override
    public String toString() {
        return getSummary();
    }
}

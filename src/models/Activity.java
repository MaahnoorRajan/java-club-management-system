package models;

public class Activity {


    private String activityId;
    private String title;
    private String type;
    private String date;
    private String description;
    private String clubId;
    private int    maxParticipants;

    public Activity(String activityId, String title, String type,
                    String date, String description, String clubId, int maxParticipants) {
        this.activityId      = activityId;
        this.title           = title;
        this.type            = type;
        this.date            = date;
        this.description     = description;
        this.clubId          = clubId;
        this.maxParticipants = maxParticipants;
    }


    public String getActivityId()      { return activityId; }
    public String getTitle()           { return title; }
    public String getType()            { return type; }
    public String getDate()            { return date; }
    public String getDescription()     { return description; }
    public String getClubId()          { return clubId; }
    public int    getMaxParticipants() { return maxParticipants; }


    public void setTitle(String title)                   { this.title           = title; }
    public void setType(String type)                     { this.type            = type;  }
    public void setDate(String date)                     { this.date            = date;  }
    public void setDescription(String description)       { this.description     = description; }
    public void setMaxParticipants(int maxParticipants)  { this.maxParticipants = maxParticipants; }


    public void displayDetails() {
        System.out.println("  Activity ID  : " + activityId);
        System.out.println("  Title        : " + title);
        System.out.println("  Type         : " + type);
        System.out.println("  Date         : " + date);
        System.out.println("  Description  : " + description);
        System.out.println("  Club ID      : " + clubId);
        System.out.println("  Max Parts    : " + maxParticipants);
    }

    @Override
    public String toString() {
        return "[" + activityId + "] " + title + " (" + type + ") on " + date;
    }
}

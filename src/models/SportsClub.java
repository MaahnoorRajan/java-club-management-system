package models;
public class SportsClub extends Club {

    private String sport;
    private String homeVenue;
    private String coachName;

    public SportsClub(String clubId, String clubName, String established,
                      String description, int maxMembers,
                      String sport, String homeVenue, String coachName) {
        super(clubId, clubName, established, description, maxMembers); // Inheritance
        this.sport     = sport;
        this.homeVenue = homeVenue;
        this.coachName = coachName;
    }

    public String getSport()     { return sport;     }
    public String getHomeVenue() { return homeVenue; }
    public String getCoachName() { return coachName; }
    public void setSport(String sport)         { this.sport     = sport;     }
    public void setHomeVenue(String homeVenue) { this.homeVenue = homeVenue; }
    public void setCoachName(String coachName) { this.coachName = coachName; }

    @Override
    public String getClubType() {
        return "Sports"; // Polymorphism: overridden method
    }

    @Override
    public void displayDetails() {
        super.displayDetails(); // Reuse parent logic (Inheritance)
        System.out.println("  Sport        : " + sport);
        System.out.println("  Home Venue   : " + homeVenue);
        System.out.println("  Coach        : " + coachName);
    }
}

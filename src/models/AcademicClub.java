package models;

public class AcademicClub extends Club {


    private String subject;
    private String facultyAdvisor;

    public AcademicClub(String clubId, String clubName, String established,
                        String description, int maxMembers,
                        String subject, String facultyAdvisor) {
        super(clubId, clubName, established, description, maxMembers); // Inheritance
        this.subject        = subject;
        this.facultyAdvisor = facultyAdvisor;
    }


    public String getSubject()        { return subject; }
    public String getFacultyAdvisor() { return facultyAdvisor; }
    public void setSubject(String subject)               { this.subject        = subject;        }
    public void setFacultyAdvisor(String facultyAdvisor) { this.facultyAdvisor = facultyAdvisor; }


    @Override
    public String getClubType() {
        return "Academic";
    }


    @Override
    public void displayDetails() {
        super.displayDetails();
        System.out.println("  Subject      : " + subject);
        System.out.println("  Advisor      : " + facultyAdvisor);
    }
}

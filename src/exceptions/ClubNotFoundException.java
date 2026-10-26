package exceptions;


public class ClubNotFoundException extends InvalidClubDataException {

    private final String searchedClubId;


    public ClubNotFoundException(String clubId) {
        super("Club not found with ID: " + clubId, "CLUB_NOT_FOUND");
        this.searchedClubId = clubId;
    }


    public String getSearchedClubId() {
        return searchedClubId;
    }
}

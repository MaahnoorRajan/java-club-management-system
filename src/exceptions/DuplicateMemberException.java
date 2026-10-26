package exceptions;


public class DuplicateMemberException extends InvalidClubDataException {

    private final String memberId;
    private final String clubId;


    public DuplicateMemberException(String memberId, String clubId) {
        super("Member [" + memberId + "] is already registered in Club [" + clubId + "]", "DUPLICATE_MEMBER");
        this.memberId = memberId;
        this.clubId   = clubId;
    }

    public String getMemberId() { return memberId; }
    public String getClubId()   { return clubId;   }
}

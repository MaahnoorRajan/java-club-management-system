package exceptions;


public class InvalidClubDataException extends Exception {

    private final String errorCode;


    public InvalidClubDataException(String message) {
        super(message);
        this.errorCode = "INVALID_DATA";
    }


    public InvalidClubDataException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }

    @Override
    public String toString() {
        return "[" + errorCode + "] " + getMessage();
    }
}

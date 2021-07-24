package com.sismed.exception;

public class CustomException extends Exception {

    public static final String GENERAL_ERROR_CODE = "500";
    public static final String GENERAL_ERROR_MESSAGE = "Internal Server Error";

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private final String errorCode;

    public CustomException() {
        super(GENERAL_ERROR_MESSAGE);
        this.errorCode = GENERAL_ERROR_CODE;
    }


    public CustomException(String message, Throwable cause, String errorCode) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public CustomException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public CustomException(Throwable cause, String errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}

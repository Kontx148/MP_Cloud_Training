package edu.bbte.idde.vnim2413.service;

public class ServiceException extends RuntimeException {
    private final int status;

    public ServiceException(int status, String message) {
        super(message);
        this.status = status;
    }

    public ServiceException(int status, String message, Throwable cause) {
        super(message, cause);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}

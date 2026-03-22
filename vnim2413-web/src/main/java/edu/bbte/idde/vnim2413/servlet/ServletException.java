package edu.bbte.idde.vnim2413.servlet;

public class ServletException extends RuntimeException {
    private final int status;

    public ServletException(int status, String message) {
        super(message);
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}

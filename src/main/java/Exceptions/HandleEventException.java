package Exceptions;

public class HandleEventException extends Exception {
    public HandleEventException() { super(); }
    public HandleEventException(String message) { super(message); }
    public HandleEventException(String message, Throwable cause) { super(message, cause); }
    public HandleEventException(Throwable cause) { super(cause); }
}
